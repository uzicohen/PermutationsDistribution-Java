package topmatching;

import java.util.HashSet;

import pattern.Node;

public class TopProbUtils {

	private static class Result {

		public boolean res;

		public Result() {
			this.res = true;
		}
	}

	private static TopMatchingArgs topMatchingArgs;

	private static TopProbArgs topProbArgs;

	public static void init(TopMatchingArgs topMatchingArgsInput, TopProbArgs topProbArgsInput) {
		topMatchingArgs = topMatchingArgsInput;
		topProbArgs = topProbArgsInput;
	}

	public static double getInsertionProb(Delta delta, String sigma, int j, StringBuilder sb) {
		// Get the i from "s{i}"
		int i = Integer.parseInt(sigma.split("s")[1]);

		HashSet<String> imgLargerThanI = getItemsLargerThanI(i);
		int numOfElementsSmallerThanJ = 0;

		for (String item : imgLargerThanI) {
			String label = topProbArgs.getSigmaToGammaValueMap().get(item).iterator().next();
			if (delta.getLabelToIndex().get(label) < j) {
				numOfElementsSmallerThanJ++;
			}
		}
		int jTag = j - numOfElementsSmallerThanJ;
		sb.append(String.format("Pi(%d,%d)", i, jTag));
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
			int labelDelta = delta.getLabelToIndex().get(label);
			int maxParentDelta = 0;
			HashSet<String> parents = topMatchingArgs.getLabelToParentsMap().containsKey(label)
					? topMatchingArgs.getLabelToParentsMap().get(label) : new HashSet<>();
			for (String parentLable : parents) {
				maxParentDelta = Math.max(maxParentDelta, delta.getLabelToIndex().get(parentLable));
			}
			maxParentDelta++;

			for (; maxParentDelta <= labelDelta; maxParentDelta++) {
				result.add(maxParentDelta);
			}
		}

		return result;
	}

	private static void isDeltaConsistentAux(Delta delta, Result result, Node l) {
		if (!result.res) {
			return;
		}

		// Check g's constraints
		for (Node lTag : l.getChildren()) {
			if (delta.getLabelToIndex().get(l.getLabel()) >= delta.getLabelToIndex().get(lTag.getLabel())) {
				result.res = false;
				return;
			}
		}

		// Check top-matching's constraints
		HashSet<String> parentsOfL = topMatchingArgs.getLabelToParentsMap().containsKey(l.getLabel())
				? topMatchingArgs.getLabelToParentsMap().get(l.getLabel()) : new HashSet<>();

		if (parentsOfL.isEmpty()) {
			// If the label has no parents, it's mapping has to be the best
			// possible

			for (String sigma : topMatchingArgs.getRim().getModel().getModal()) {
				if (!topMatchingArgs.getLambda().containsKey(sigma)
						|| sigma.equals(topProbArgs.getGamma().get(l.getLabel()))) {
					continue;
				}

				if (topMatchingArgs.getLambda().get(sigma).contains(l.getLabel())) {
					int minIndexForItem = topMatchingArgs.getRim().getModel().getModal().size();

					// Check if this sigma is mapped to a better position than
					// the position of l's item
					HashSet<String> labelsOfSigma = topMatchingArgs.getLambda().get(sigma);
					for (String label : labelsOfSigma) {
						if (topProbArgs.getGamma().get(label).equals(sigma)) {
							// This means sigma is in the mapping
							minIndexForItem = Math.min(minIndexForItem, delta.getLabelToIndex().get(label));
						}
					}
					if (minIndexForItem < delta.getLabelToIndex().get(l.getLabel())) {
						result.res = false;
						break;
					}
				}
			}
		} else

		{
			// If the label has parents, it's mapping has to be the best
			// possible under the constraint of the maximal index of the parents
			int maxIndexOfParent = 0;

			for (String parent : parentsOfL) {
				maxIndexOfParent = Math.max(maxIndexOfParent, delta.getLabelToIndex().get(parent));
			}

			for (String otherSigmaInLabel : l.getItems()) {
				if (otherSigmaInLabel.equals(topProbArgs.getGamma().get(l.getLabel()))
						|| !topProbArgs.getImgGamma().contains(otherSigmaInLabel)) {
					continue;
				}
				String labelOfSigma = topProbArgs.getSigmaToGammaValueMap().get(otherSigmaInLabel).iterator().next();
				int indexOfOtherSigma = delta.getLabelToIndex().get(labelOfSigma);
				if (indexOfOtherSigma > maxIndexOfParent
						&& indexOfOtherSigma < delta.getLabelToIndex().get(l.getLabel())) {
					result.res = false;
				}
			}
		}

		for (Node child : l.getChildren()) {
			isDeltaConsistentAux(delta, result, child);
		}
	}

	/**
	 * 
	 * 
	 * Check if delta is consistent with g and top-matching constarints
	 * 
	 * Consistent with g: for every (l,l') in E, delta(l) < delta(l')
	 * 
	 * Consistent with top-matching: for every l in V and sigma s.t sigma in
	 * lambda(l), if tau(sigma) < delta(l), then: parents(l) neq {} and
	 * tau(sigma) < max_{l' in parents(l)} delta(l')
	 * 
	 * In particular, if parents(l) = {}, then gamma(l) is the highest ranked
	 * sigma s.t l in lambda(sigma)
	 */
	public static boolean isDeltaConsistent(Delta delta) {
		Result result = new Result();

		for (Node root : topMatchingArgs.getG().getRoots()) {
			isDeltaConsistentAux(delta, result, root);
		}

		return result.res;
	}

}
