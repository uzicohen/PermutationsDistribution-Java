package topmatching.delta;

import java.util.ArrayList;
import java.util.HashSet;

import general.GeneralUtils;
import general.main.GeneralArgs;
import pattern.Node;
import pattern.PatternUtils;
import topmatching.TopMatchingArgs;
import topmatching.TopProbArgs;

public class SimpleDeltasContainerGenerator implements IDeltasContainerGenerator {

	private static class Result {

		public boolean res;

		public Result() {
			this.res = true;
		}
	}

	// This variable will serve all assignments, so we need to create it only
	// once per a given graph
	private static ArrayList<ArrayList<String>> allPossibleLabelsOrders;

	private TopMatchingArgs topMatchingArgs;

	@Override
	public void init(TopMatchingArgs topMatchingArgs) {
		this.topMatchingArgs = topMatchingArgs;
		if (allPossibleLabelsOrders == null) {
			ArrayList<ArrayList<ArrayList<String>>> labelsPerLevel = getInitialLabelsOrders();
			allPossibleLabelsOrders = new ArrayList<>();
			createPossibleOrders(new ArrayList<String>(), allPossibleLabelsOrders, labelsPerLevel);
		}
	}

	@Override
	public DeltasContainer getInitialDeltas(TopProbArgs topProbArgs) {
		DeltasContainer result = getNewDeltasContainer();
		ArrayList<String> allLabels = new ArrayList<>(topProbArgs.getGamma().keySet());
		ArrayList<ArrayList<String>> possibleOrders = GeneralUtils.generatePermutations(allLabels);
		for (ArrayList<String> possibleOrder : possibleOrders) {
			Delta newDelta = new Delta(possibleOrder, topProbArgs.getGamma());
			if (result.getDelta(newDelta) == null && isDeltaConsistent(topProbArgs, newDelta)) {
				result.addDelta(newDelta);
			}
		}
		return result;
	}

	/**
	 * 
	 * Get all the possible permutations within level where the order between
	 * levels is dictated by the graph
	 * 
	 */
	private ArrayList<ArrayList<ArrayList<String>>> getInitialLabelsOrders() {
		ArrayList<ArrayList<ArrayList<String>>> result = new ArrayList<>();
		ArrayList<ArrayList<String>> labelsPerLevel = PatternUtils.getLabelsPerLevel(this.topMatchingArgs.getG());
		for (int i = 0; i < labelsPerLevel.size(); i++) {
			result.add(GeneralUtils.generatePermutations(labelsPerLevel.get(i)));
		}
		return result;
	}

	private void createPossibleOrders(ArrayList<String> current, ArrayList<ArrayList<String>> result,
			ArrayList<ArrayList<ArrayList<String>>> labelsPerLevel) {
		if (labelsPerLevel.isEmpty()) {
			result.add(current);
			return;
		}
		ArrayList<ArrayList<String>> currentElementsToAdd = labelsPerLevel.get(0);
		for (int i = 0; i < currentElementsToAdd.size(); i++) {
			ArrayList<String> newCurrent = new ArrayList<>(current);
			newCurrent.addAll(currentElementsToAdd.get(i));
			ArrayList<ArrayList<ArrayList<String>>> subList = new ArrayList<>(
					labelsPerLevel.subList(1, labelsPerLevel.size()));
			createPossibleOrders(newCurrent, result, subList);
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
	private boolean isDeltaConsistent(TopProbArgs topProbArgs, Delta delta) {
		Result result = new Result();

		for (Node root : this.topMatchingArgs.getG().getRoots()) {
			isDeltaConsistentAux(topProbArgs, delta, result, root);
		}

		return result.res;
	}

	private void isDeltaConsistentAux(TopProbArgs topProbArgs, Delta delta, Result result, Node l) {
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
		HashSet<String> parentsOfL = this.topMatchingArgs.getLabelToParentsMap().containsKey(l.getLabel())
				? this.topMatchingArgs.getLabelToParentsMap().get(l.getLabel()) : new HashSet<>();

		if (parentsOfL.isEmpty()) {
			// If the label has no parents, it's mapping has to be the best
			// possible

			for (String sigma : this.topMatchingArgs.getRim().getModel().getModal()) {
				if (!this.topMatchingArgs.getLambda().containsKey(sigma)
						|| sigma.equals(topProbArgs.getGamma().get(l.getLabel()))) {
					continue;
				}

				if (this.topMatchingArgs.getLambda().get(sigma).contains(l.getLabel())) {
					int minIndexForItem = this.topMatchingArgs.getRim().getModel().getModal().size();

					// Check if this sigma is mapped to a better position than
					// the position of l's item
					HashSet<String> labelsOfSigma = this.topMatchingArgs.getLambda().get(sigma);
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
		} else {
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
			isDeltaConsistentAux(topProbArgs, delta, result, child);
		}
	}

	private DeltasContainer getNewDeltasContainer() {
		return GeneralArgs.enhancedDeltasContainer ? new EnhancedDeltasContainer(topMatchingArgs)
				: new SimpleDeltasContainer(topMatchingArgs);
	}

}
