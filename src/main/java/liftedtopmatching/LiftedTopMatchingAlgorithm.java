package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.IAlgorithm;
import general.main.GeneralArgs;
import liftedtopmatching.delta.LiftedTopMatchingDeltasContainerGenerator;
import pattern.Graph;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.EnhancedDeltasContainer;

public class LiftedTopMatchingAlgorithm implements IAlgorithm {

	private static final Logger logger = Logger.getLogger(LiftedTopMatchingAlgorithm.class.getName());

	private TopMatchingArgs topMatchingArgs;

	private double probability;

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
				DeltasContainer newDc = new EnhancedDeltasContainer(topMatchingArgs);
				newDc.addDelta(delta);
				calculateProbabilityForSubsetOfDeltas(newDc);
			}
		}

	}

	@Override
	public double calculateProbability(Graph graph, Distribution distribution) {

		// For each label, we keep in a dictionary it's parents
		HashMap<String, HashSet<String>> labelToParentsMap = new HashMap<>();

		// For each sigma, we keep in a dictionary it's possible labels
		HashMap<String, HashSet<String>> lambda = new HashMap<>();

		// Create the data structures in the level of top matching
		LiftedTopMatchingUtils.preProcessGraph(graph, labelToParentsMap, lambda);

		this.topMatchingArgs = new TopMatchingArgs(graph, distribution, labelToParentsMap, lambda,
				GeneralUtils.getInsertionProbabilities(distribution.getModel()));

		LiftedTopMatchingUtils.init(this.topMatchingArgs);

		DeltasContainer r = new LiftedTopMatchingDeltasContainerGenerator().getInitialDeltas(this.topMatchingArgs);

		if (GeneralArgs.runMultiThread) {
			ExecutorService executor = Executors.newFixedThreadPool(GeneralArgs.numOfThreads);
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				DeltasContainer newDc = new EnhancedDeltasContainer(topMatchingArgs);
				newDc.addDelta(delta);
				executor.execute(new DeltaProbCalculator(newDc));
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

		} else {
			calculateProbabilityForSubsetOfDeltas(r);
		}
		return this.probability;
	}

	private synchronized void updateProb(double prob) {
		this.probability += prob;
	}

	// For multi thread purposes
	private void calculateProbabilityForSubsetOfDeltas(DeltasContainer dc) {
		DeltasContainer r = dc;

		for (int i = 0; i < this.topMatchingArgs.getRim().getModel().getModal().size(); i++) {
			String sigma = this.topMatchingArgs.getRim().getModel().getModal().get(i);
			DeltasContainer newR = new EnhancedDeltasContainer(topMatchingArgs);

			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();

				// Take the non-assigned j's
				HashMap<Integer, HashSet<String>> jToSetOfLabels = delta
						.getNonAssignedJsToLabels(this.topMatchingArgs.getLambda(), sigma);

				// For each j, test if the labels that are mapped to it are legal
				HashSet<Integer> illegalIndices = LiftedTopMatchingUtils.getIllegalLables(sigma, delta, jToSetOfLabels);

				for (int j : jToSetOfLabels.keySet()) {
					if (illegalIndices.contains(j)) {
						continue;
					}
					Delta deltaTag = new Delta(delta);
					for (String label : jToSetOfLabels.get(j)) {
						deltaTag.addAssignmentToLabel(label);
					}

					// Update the probability
					double insertionProb = LiftedTopMatchingUtils.getInsertionProb(deltaTag, sigma, j);

					deltaTag.setProbability(deltaTag.getProbability() * insertionProb);

					// Search in newR the constructed delta
					Delta exisitingDelta = newR.getDelta(deltaTag);

					if (exisitingDelta != null) {
						exisitingDelta.setProbability(exisitingDelta.getProbability() + deltaTag.getProbability());
					} else {
						// insert the new delta into the new R
						newR.addDelta(deltaTag);
					}
				}

				// Finally, we insert the item to all possible indices in a case it is not
				// assigned to any label

				if (i + delta.getNumOfDistinctNonAssignedLabels() < topMatchingArgs.getRim().getModel().getModal()
						.size()) {
					ArrayList<Integer> rangeNotWithinLabels = LiftedTopMatchingUtils.rangeNotWithinLabels(delta, sigma);
					for (int j : rangeNotWithinLabels) {
						Delta deltaTag = new Delta(delta);
						deltaTag.insertNewItem(j);

						// Update the probability
						double insertionProb = LiftedTopMatchingUtils.getInsertionProb(deltaTag, sigma, j);

						deltaTag.setProbability(deltaTag.getProbability() * insertionProb);

						// Search in newR the constructed delta
						Delta exisitingDelta = newR.getDelta(deltaTag);

						if (exisitingDelta != null) {
							exisitingDelta.setProbability(exisitingDelta.getProbability() + deltaTag.getProbability());
						} else {
							// insert the new delta into the new R
							newR.addDelta(deltaTag);
						}
					}
				}
			}
			r = newR;
		}

		double prob = 0.0;

		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			prob += delta.getProbability();
		}
		updateProb(prob);
	}

}
