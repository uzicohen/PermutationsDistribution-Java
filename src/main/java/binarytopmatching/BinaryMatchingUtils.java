package binarytopmatching;

import java.util.HashMap;
import java.util.HashSet;

import pattern.Graph;
import pattern.Node;
import topmatching.TopMatchingArgs;
import topmatching.delta.Delta;

public class BinaryMatchingUtils {

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

}
