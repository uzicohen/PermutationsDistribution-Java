package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.IAlgorithm;
import liftedtopmatching.delta.LiftedTopMatchingDeltasContainerGenerator;
import pattern.Graph;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.EnhancedDeltasContainer;

public class LiftedTopMatchingAlgorithm implements IAlgorithm {

	private static final Logger logger = Logger.getLogger(LiftedTopMatchingAlgorithm.class.getName());

	private TopMatchingArgs topMatchingArgs;

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

		double probability = 0.0;
		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			probability += delta.getProbability();
		}

		return probability;
	}
}
