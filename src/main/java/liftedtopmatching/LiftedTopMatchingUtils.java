package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import general.main.GeneralArgs;
import pattern.Graph;
import pattern.Node;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;

public class LiftedTopMatchingUtils {

	private static TopMatchingArgs topMatchingArgs;

	public static void init(TopMatchingArgs topMatchingArgsInput) {
		topMatchingArgs = topMatchingArgsInput;
	}

	private static void preProcessGraphAux(Node node, HashMap<String, HashSet<String>> labelToParentsMap,
			HashMap<String, HashSet<String>> lambda) {
		String label = node.getLabel();
		for (String sigma : node.getItems()) {
			HashSet<String> currentSigmaLabels = lambda.containsKey(sigma) ? lambda.get(sigma) : new HashSet<>();
			currentSigmaLabels.add(label);
			lambda.put(sigma, currentSigmaLabels);
		}

		for (Node child : node.getChildren()) {
			HashSet<String> currentChildParents = labelToParentsMap.containsKey(child.getLabel())
					? labelToParentsMap.get(child.getLabel()) : new HashSet<>();
			currentChildParents.add(label);
			labelToParentsMap.put(child.getLabel(), currentChildParents);

			preProcessGraphAux(child, labelToParentsMap, lambda);
		}
	}

	public static void preProcessGraph(Graph graph, HashMap<String, HashSet<String>> labelToParentsMap,
			HashMap<String, HashSet<String>> lambda) {
		for (Node root : graph.getRoots()) {
			preProcessGraphAux(root, labelToParentsMap, lambda);
		}
	}

	public static int getPossibleRangeOfDelta(int i, Delta delta) {
		return i + delta.getNumOfDistinctNonAssignedLabels();
	}

	public static HashSet<Integer> getIllegalIndices(Delta delta, String sigma) {
		if (!topMatchingArgs.getLambda().containsKey(sigma)) {
			return new HashSet<>();
		}
		HashSet<Integer> result = new HashSet<>();
		HashSet<String> currentSigmaLambdas = topMatchingArgs.getLambda().get(sigma);

		for (String label : currentSigmaLambdas) {
			int labelDelta = delta.getLabelPosition(label);
			int maxParentDelta = 0;
			HashSet<String> parents = topMatchingArgs.getLabelToParentsMap().containsKey(label)
					? topMatchingArgs.getLabelToParentsMap().get(label) : new HashSet<>();
			for (String parentLable : parents) {
				maxParentDelta = Math.max(maxParentDelta, delta.getLabelPosition(parentLable));
			}
			maxParentDelta++;

			for (; maxParentDelta <= labelDelta; maxParentDelta++) {
				result.add(maxParentDelta);
			}
		}

		return result;
	}

	public static HashMap<Double, Double> getInsertionProbs(Delta delta, String sigma, int j) {
		// Get the i from "s{i}"
		int i = Integer.parseInt(sigma.split("s")[1]);
		int jTag = j;
		HashSet<Integer> seen = new HashSet<>();
		for (String label : delta.getKeySet()) {
			if (delta.getLabelPosition(label) < j && delta.getLabelState(label) == 0
					&& !seen.contains(delta.getLabelPosition(label))) {
				seen.add(delta.getLabelPosition(label));
				jTag--;
			}
		}
		HashMap<Double, Double> result = new HashMap<>();
		HashSet<Double> setOfPhi = new HashSet<>();
		topMatchingArgs.getDistributions().forEach(dist -> setOfPhi.add(dist.getModel().getPhi()));
		for (double phi : setOfPhi) {
			result.put(phi, topMatchingArgs.getPhiToInsertionProbs().get(phi).get(i - 1).get(jTag - 1));
		}
		return result;
	}

	public static HashSet<Integer> getIllegalLables(String sigma, Delta delta,
			HashMap<Integer, HashSet<String>> jToSetOfLabels) {

		// For each label, make sure that the following applies:
		// for each l' s.t l' in lambda(sigma) and delta(l) < delta(l'),
		// parents(l') != empty_set AND delta(l) < Max delta(u), where u in
		// parents(l')
		//
		//
		// (if parents(l') = empty_set, then l' should be the one that takes
		// sigma)

		HashSet<Integer> result = new HashSet<>();
		for (int j : jToSetOfLabels.keySet()) {
			// Get the labels that are associated to j
			HashSet<String> labels = jToSetOfLabels.get(j);
			for (String l : labels) {
				// Get the labels, lTags, we need to check against
				HashSet<String> lTags = getSetOfLTag(delta, sigma, l);
				for (String lTag : lTags) {
					if (topMatchingArgs.getLabelToParentsMap().get(lTag) == null
							|| topMatchingArgs.getLabelToParentsMap().get(lTag).isEmpty()) {
						result.add(j);
						break;
					}
					int maxParentPosition = -1;
					for (String parentLabel : topMatchingArgs.getLabelToParentsMap().get(lTag)) {
						maxParentPosition = Math.max(maxParentPosition, delta.getLabelPosition(parentLabel));
					}
					if (delta.getLabelPosition(l) > maxParentPosition) {
						result.add(j);
					}
				}
				// If j was added, we can break
				if (result.contains(j)) {
					break;
				}
			}
		}
		return result;
	}

	// return l' s.t l' in lambda(sigma) and delta(l) < delta(l'),
	private static HashSet<String> getSetOfLTag(Delta delta, String sigma, String l) {
		HashSet<String> result = new HashSet<>();
		HashSet<String> candidates = topMatchingArgs.getLambda().get(sigma);
		for (String lTag : candidates) {
			if (delta.getLabelPosition(l) < delta.getLabelPosition(lTag)) {
				result.add(lTag);
			}
		}
		return result;
	}

	public static ArrayList<Integer> rangeNotWithinLabels(Delta delta, String sigma) {
		ArrayList<Integer> result = new ArrayList<>();
		// Get the i from "s{i}"
		int i = Integer.parseInt(sigma.split("s")[1]);
		int s = getPossibleRangeOfDelta(i, delta);

		HashSet<Integer> illegalIndices = getIllegalIndices(delta, sigma);

		for (int j = 1; j <= s; j++) {
			if (!illegalIndices.contains(j)) {
				result.add(j);
			}
		}
		return result;
	}

	public static DeltasContainer getNewR(ArrayList<String> modal, DeltasContainer r, int i) {
		String sigma = modal.get(i);
		DeltasContainer newR = new DeltasContainer();

		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();

			// Take the non-assigned j's
			HashMap<Integer, HashSet<String>> jToSetOfLabels = delta
					.getNonAssignedJsToLabels(topMatchingArgs.getLambda(), sigma);

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
				HashMap<Double, Double> phiToInsertionProb = LiftedTopMatchingUtils.getInsertionProbs(deltaTag, sigma,
						j);
				for (double phi : phiToInsertionProb.keySet()) {
					deltaTag.setProbabilityOfPhi(phi, deltaTag.getProbabilityOfPhi(phi) * phiToInsertionProb.get(phi));
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

			// If earlyPrunningOptimization is turned on, we calculate the extra
			// possible deltas only if there is a possibility that the delta
			// will be full of 1's. o.w., we calculate it anyway

			boolean continueExtraction = true;
			if (GeneralArgs.earlyPrunningOptimization) {
				continueExtraction = i + delta.getNumOfDistinctNonAssignedLabels() < modal.size();
			} 

			if (continueExtraction) {
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
		return newR;
	}

}
