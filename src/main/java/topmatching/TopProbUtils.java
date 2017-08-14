package topmatching;

import java.util.HashSet;

import topmatching.delta.Delta;

public class TopProbUtils {

	private static TopMatchingArgs topMatchingArgs;

	private static TopProbArgs topProbArgs;

	public static void init(TopMatchingArgs topMatchingArgsInput, TopProbArgs topProbArgsInput) {
		topMatchingArgs = topMatchingArgsInput;
		topProbArgs = topProbArgsInput;
	}

	public static double getInsertionProb(Delta delta, String sigma, int j) {
		// Get the i from "s{i}"
		int i = Integer.parseInt(sigma.split("s")[1]);

		HashSet<String> imgLargerThanI = getItemsLargerThanI(i);
		int numOfElementsSmallerThanJ = 0;

		for (String item : imgLargerThanI) {
			String label = topProbArgs.getSigmaToGammaValueMap().get(item).iterator().next();
			if (delta.getLabelPosition(label) < j) {
				numOfElementsSmallerThanJ++;
			}
		}
		int jTag = j - numOfElementsSmallerThanJ;
		return topMatchingArgs.getInsertionProbs().get(i - 1).get(jTag - 1);
	}

	public static HashSet<String> getItemsLargerThanI(int i) {
		HashSet<String> result = new HashSet<>();
		for (String sigma : topProbArgs.getImgGamma()) {
			int j = Integer.parseInt(sigma.split("s")[1]);
			if (j > i) {
				result.add(sigma);
			}
		}
		return result;
	}

	public static HashSet<Integer> getIllegalIndices(Delta delta, String sigma) {
		HashSet<Integer> result = new HashSet<>();
		HashSet<String> currentSigmaLambda = topMatchingArgs.getLambda().get(sigma);

		for (String label : currentSigmaLambda) {
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

}
