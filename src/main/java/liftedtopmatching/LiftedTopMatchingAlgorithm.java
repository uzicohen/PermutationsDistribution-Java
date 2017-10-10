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
import general.IAlgorithm;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import pattern.Graph;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;

public class LiftedTopMatchingAlgorithm implements IAlgorithm {

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
				DeltasContainer newDc = new DeltasContainer(topMatchingArgs);
				newDc.addDelta(delta);
				calculateProbabilityForSubsetOfDeltas(newDc);
			}
		}
	}

	public LiftedTopMatchingAlgorithm() {
		this.phiToProbability = new HashMap<>();
	}

	@Override
	public HashMap<Double, Double> calculateProbability(Graph graph, ArrayList<Distribution> distributions) {
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

		DeltasContainer r = new LiftedTopMatchingDeltasContainerGenerator().getInitialDeltas(this.topMatchingArgs);

		if (GeneralArgs.runMultiThread) {
			ExecutorService executor = Executors.newFixedThreadPool(GeneralArgs.numOfThreads);
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				DeltasContainer newDc = new DeltasContainer(topMatchingArgs);
				newDc.addDelta(delta);
				executor.execute(new DeltaProbCalculator(newDc));
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

		} else {
			calculateProbabilityForSubsetOfDeltas(r);
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
			String sigma = modal.get(i);
			DeltasContainer newR = new DeltasContainer(topMatchingArgs);

			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();

				// Take the non-assigned j's
				HashMap<Integer, HashSet<String>> jToSetOfLabels = delta
						.getNonAssignedJsToLabels(this.topMatchingArgs.getLambda(), sigma);

				// For each j, test if the labels that are mapped to it are
				// legal
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
					HashMap<Double, Double> phiToInsertionProb = LiftedTopMatchingUtils.getInsertionProbs(deltaTag,
							sigma, j);
					for (double phi : phiToInsertionProb.keySet()) {
						deltaTag.setProbabilityOfPhi(phi,
								deltaTag.getProbabilityOfPhi(phi) * phiToInsertionProb.get(phi));
					}

					// Search in newR the constructed delta
					Delta exisitingDelta = newR.getDelta(deltaTag);

					if (exisitingDelta != null) {
						for (double phi : phiToInsertionProb.keySet()) {
							exisitingDelta.setProbabilityOfPhi(phi,
									exisitingDelta.getProbabilityOfPhi(phi) + deltaTag.getProbabilityOfPhi(phi));
						}
					} else {
						// insert the new delta into the new R
						newR.addDelta(deltaTag);
					}
				}

				// Finally, we insert the item to all possible indices in a case
				// it is not
				// assigned to any label

				if (i + delta.getNumOfDistinctNonAssignedLabels() < modal.size()) {
					ArrayList<Integer> rangeNotWithinLabels = LiftedTopMatchingUtils.rangeNotWithinLabels(delta, sigma);
					for (int j : rangeNotWithinLabels) {
						Delta deltaTag = new Delta(delta);
						deltaTag.insertNewItem(j);

						// Update the probability
						HashMap<Double, Double> phiToInsertionProb = LiftedTopMatchingUtils.getInsertionProbs(deltaTag,
								sigma, j);
						for (double phi : phiToInsertionProb.keySet()) {
							deltaTag.setProbabilityOfPhi(phi,
									deltaTag.getProbabilityOfPhi(phi) * phiToInsertionProb.get(phi));
						}

						// Search in newR the constructed delta
						Delta exisitingDelta = newR.getDelta(deltaTag);

						if (exisitingDelta != null) {
							for (double phi : phiToInsertionProb.keySet()) {
								exisitingDelta.setProbabilityOfPhi(phi,
										exisitingDelta.getProbabilityOfPhi(phi) + deltaTag.getProbabilityOfPhi(phi));
							}
						} else {
							// insert the new delta into the new R
							newR.addDelta(deltaTag);
						}
					}
				}
			}
			r = newR;
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
