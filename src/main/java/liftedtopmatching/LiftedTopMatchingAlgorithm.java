package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.Algorithm;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import graph.Graph;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;

public class LiftedTopMatchingAlgorithm extends Algorithm {

	private static final Logger logger = Logger.getLogger(LiftedTopMatchingAlgorithm.class.getName());

	private TopMatchingArgs topMatchingArgs;

	private HashMap<Double, Double> phiToProbability;

	private class DeltaProbCalculator implements Runnable {
		private DeltasContainer r;

		public DeltaProbCalculator(DeltasContainer r) {
			this.r = r;
		}

		@Override
		public void run() {
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				DeltasContainer newDc = new DeltasContainer();
				newDc.addDelta(delta);
				calculateProbabilityForSubsetOfDeltas(newDc);
			}
		}
	}

	public LiftedTopMatchingAlgorithm(Graph graph, ArrayList<Distribution> distributions) {
		super(graph, distributions);
		this.phiToProbability = new HashMap<>();
	}

	@Override
	public HashMap<Double, Double> calculateProbability() {
		GeneralArgs.currentAlgorithm = AlgorithmType.LIFTED_TOP_MATCHING;

		// For each label, we keep in a dictionary it's parents
		HashMap<String, HashSet<String>> labelToParentsMap = new HashMap<>();

		// For each sigma, we keep in a dictionary it's possible labels
		HashMap<String, HashSet<String>> lambda = new HashMap<>();

		// Create the data structures in the level of top matching
		LiftedTopMatchingUtils.preProcessGraph(graph, labelToParentsMap, lambda);

		this.topMatchingArgs = new TopMatchingArgs(graph, distributions, labelToParentsMap, lambda,
				GeneralUtils.getPhiToInsertionProbabilities(distributions));

		LiftedTopMatchingUtils.init(this.topMatchingArgs);

		if (GeneralArgs.verbose) {
			logger.info("Generating initial deltas");
		}

		DeltasContainer r = new LiftedTopMatchingDeltasContainerGenerator().getInitialDeltas(this.topMatchingArgs);

		int numOfDeltas = r.getNumOfDeltas();

		if (GeneralArgs.verbose) {
			logger.info(String.format("Calculating probability over %d deltas", numOfDeltas));
			if (GeneralArgs.runMultiThread) {
				logger.info(String.format("Multithread is true. Running with %d threads", GeneralArgs.numOfThreads));
			} else {
				logger.info("Multithread is false");
			}
		}

		if (GeneralArgs.runMultiThread) {
			ExecutorService executor = Executors.newFixedThreadPool(GeneralArgs.numOfThreads);
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				DeltasContainer newDc = new DeltasContainer();
				newDc.addDelta(delta);
				executor.execute(new DeltaProbCalculator(newDc));
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

		} else {
			calculateProbabilityForSubsetOfDeltas(r);
		}

		if (GeneralArgs.verbose) {
			logger.info("Done calculating the probability");
		}

		return this.phiToProbability;
	}

	private synchronized void updateProb(double phi, double prob) {
		double newProb = 0.0;
		if (this.phiToProbability.containsKey(phi)) {
			newProb = this.phiToProbability.get(phi);
		}
		this.phiToProbability.put(phi, newProb + prob);
	}

	// For multi thread purposes
	private void calculateProbabilityForSubsetOfDeltas(DeltasContainer dc) {
		DeltasContainer r = dc;

		ArrayList<String> modal = this.topMatchingArgs.getDistributions().get(0).getModel().getModal();
		for (int i = 0; i < modal.size(); i++) {
			if (GeneralArgs.verbose && !GeneralArgs.runMultiThread) {
				logger.info(String.format("Calculating probability over %d deltas", r.getNumOfDeltas()));
				this.phiToProbability.put((double) i + 1, (double) r.getNumOfDeltas());
			}

			// labelWithFutureItems contains labels that can be assigned by
			// items greater than i
			HashSet<String> labelsWithFutureItems = new HashSet<>();
			for (String sigma : topMatchingArgs.getLambda().keySet()) {
				int sigmaNum = Integer.parseInt(sigma.split("s")[1]);
				if (sigmaNum > i + 1) {
					labelsWithFutureItems.addAll(topMatchingArgs.getLambda().get(sigma));
				}
			}

			r = LiftedTopMatchingUtils.getNewR(modal, r, i, labelsWithFutureItems);

			if (GeneralArgs.verbose && !GeneralArgs.runMultiThread) {
				logger.info(String.format("Done with %d out of %d items", i + 1, modal.size()));
			}
		}

		if (GeneralArgs.verbose && !GeneralArgs.runMultiThread) {
			logger.info(String.format("Calculating probability over %d deltas", r.getNumOfDeltas()));
		}

		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			for (double phi : delta.getPhiToProbability().keySet()) {
				double prob = delta.getProbabilityOfPhi(phi);
				updateProb(phi, prob);
			}
		}
	}

}
