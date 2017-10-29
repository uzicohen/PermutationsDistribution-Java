package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.Algorithm;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import pattern.Graph;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.cache.DeltasCache;
import topmatching.delta.cache.DeltasCache.DeltasCacheInfo;

public class LiftedTopMatchingAlgorithm extends Algorithm {

	private static final Logger logger = Logger.getLogger(LiftedTopMatchingAlgorithm.class.getName());

	private TopMatchingArgs topMatchingArgs;

	private ConcurrentHashMap<Integer, HashMap<Double, Double>> phiToProbability;

	private static DeltasCache deltasCache = new DeltasCache();

	private DeltasCacheInfo deltasCacheInfo;

	private class DeltaProbCalculator implements Runnable {
		private int threadId;
		private DeltasContainer r;

		public DeltaProbCalculator(int threadId, DeltasContainer r) {
			this.threadId = threadId;
			this.r = r;
		}

		@Override
		public void run() {
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				DeltasContainer newDc = new DeltasContainer();
				newDc.addDelta(delta);
				calculateProbabilityForSubsetOfDeltas(threadId, newDc);
			}
		}
	}

	public LiftedTopMatchingAlgorithm(Graph graph, ArrayList<Distribution> distributions) {
		super(graph, distributions);
		this.phiToProbability = new ConcurrentHashMap<>();
		this.deltasCacheInfo = new DeltasCacheInfo();
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

		DeltasContainer r = null;
		if (GeneralArgs.commonPrefixOptimization) {
			ArrayList<String> originalModal = this.originalDistributions.get(0).getModel().getModal();
			r = deltasCache.getFromCache(this.graph.getId(), originalModal, deltasCacheInfo);
		}
		if (r == null) {
			r = new LiftedTopMatchingDeltasContainerGenerator().getInitialDeltas(this.topMatchingArgs);
		}

		if (GeneralArgs.commonPrefixOptimization && GeneralArgs.verbose) {
			if (this.deltasCacheInfo.numberOfItem != -1) {
				logger.info(String.format("Got initial deltas from cache (starting from item number %d)",
						this.deltasCacheInfo.numberOfItem));
			} else {
				logger.info("Initial deltas generated from scratch");
			}
		}

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
			int i = 0;
			while (iter.hasNext()) {
				Delta delta = iter.next();
				DeltasContainer newDc = new DeltasContainer();
				newDc.addDelta(delta);
				executor.execute(new DeltaProbCalculator(i % GeneralArgs.numOfThreads, newDc));
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

		} else {
			calculateProbabilityForSubsetOfDeltas(0, r);
		}

		if (GeneralArgs.verbose) {
			logger.info("Done calculating the probability");
		}

		HashMap<Double, Double> result = new HashMap<>();
		for (HashMap<Double, Double> threadsMap : this.phiToProbability.values()) {
			for (Double phi : threadsMap.keySet()) {
				Double current = result.get(phi);
				if (current == null) {
					current = 0.0;
				}
				current += threadsMap.get(phi);
				result.put(phi, current);
			}
		}

		return result;
	}

	@Override
	public HashMap<Double, Double> calculateProbability(int itemNumToStoreInCache) {
		return calculateProbability();
	}

	private void updateProb(int threadId, double phi, double prob) {
		double newProb = 0.0;
		HashMap<Double, Double> threadMap = this.phiToProbability.get(threadId);
		if (threadMap == null) {
			threadMap = new HashMap<>();
		}
		if (this.phiToProbability.containsKey(phi)) {
			newProb = threadMap.get(phi);
		}
		threadMap.put(phi, newProb + prob);
	}

	// For multi thread purposes
	private void calculateProbabilityForSubsetOfDeltas(int threadId, DeltasContainer dc) {
		DeltasContainer r = dc;

		ArrayList<String> modal = this.topMatchingArgs.getDistributions().get(0).getModel().getModal();
		ArrayList<String> originalModal = this.originalDistributions.get(0).getModel().getModal();
		for (int i = this.deltasCacheInfo.numberOfItem + 1; i < modal.size(); i++) {
			if (GeneralArgs.verbose && !GeneralArgs.runMultiThread) {
				logger.info(String.format("Calculating probability over %d deltas", r.getNumOfDeltas()));
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

			if (GeneralArgs.commonPrefixOptimization) {
				deltasCache.storeInCache(graph.getId(), i, originalModal, r);
			}

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
			if (!GeneralArgs.earlyPrunningOptimization && !delta.isFull()) {
				continue;
			}
			for (double phi : delta.getPhiToProbability().keySet()) {
				double prob = delta.getProbabilityOfPhi(phi);
				updateProb(threadId, phi, prob);
			}
		}
	}

}
