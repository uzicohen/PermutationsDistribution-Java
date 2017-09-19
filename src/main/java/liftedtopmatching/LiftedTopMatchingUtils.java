package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pattern.Graph;
import pattern.Node;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;

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
					? labelToParentsMap.get(child.getLabel())
					: new HashSet<>();
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
					? topMatchingArgs.getLabelToParentsMap().get(label)
					: new HashSet<>();
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

	public static double getInsertionProb(Delta delta, String sigma, int j) {
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
		return topMatchingArgs.getInsertionProbs().get(i - 1).get(jTag - 1);
	}

	public static HashSet<Integer> getIllegalLables(String sigma, Delta delta,
			HashMap<Integer, HashSet<String>> jToSetOfLabels) {

		// For each label, make sure that the following applies:
		// for each l' s.t l' in lambda(sigma) and delta(l) < delta(l'),
		// parents(l') != empty_set AND delta(l) < Max delta(u), where u in parents(l')
		//
		//
		// (if parents(l') = empty_set, then l' should be the one that takes sigma)

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

}
