package topmatching.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import general.main.GeneralArgs;
import pattern.Node;
import topmatching.TopMatchingArgs;
import topmatching.TopProbArgs;

public class EnhancedDeltasContainerGenerator implements IDeltasContainerGenerator {

	private static class Result {

		public boolean res;

		public Result() {
			this.res = true;
		}
	}

	private TopMatchingArgs topMatchingArgs;

	@Override
	public void init(TopMatchingArgs topMatchingArgs) {
		this.topMatchingArgs = topMatchingArgs;
	}

	@Override
	public DeltasContainer getInitialDeltas(TopProbArgs topProbArgs) {
		DeltasContainer dc = getNewDeltasContainer();
		dc.addDelta(new Delta());

		// A map from sigma to all the sigmas it depends on
		HashMap<String, HashSet<String>> childToParent = new HashMap<>();

		// A map from sigma to all the sigmas that depend on it
		HashMap<String, HashSet<String>> parentToChild = new HashMap<>();

		HashSet<Node> roots = new HashSet<>(this.topMatchingArgs.getG().getRoots());

		fillDependenciesMaps(topProbArgs, roots, childToParent, parentToChild);

		// Go over the maps. For each sigma that has no parents in
		// childToParent, we can put it in a new ArrayList and remove it from
		// the map
		HashMap<String, HashSet<String>> origChildToParent = new HashMap<>();
		for (String key : childToParent.keySet()) {
			HashSet<String> currentParents = new HashSet<>(childToParent.get(key));
			origChildToParent.put(key, currentParents);
		}

		while (childToParent.size() != 0) {
			ArrayList<String> currentRoots = new ArrayList<>();
			// Get those sigmas that do not depend on any other
			for (String sigma : childToParent.keySet()) {
				if (childToParent.get(sigma).isEmpty()) {
					currentRoots.add(sigma);
				}
			}

			// If no new roots, this means we have a cycle
			if (currentRoots.isEmpty()) {
				return getNewDeltasContainer();
			}

			for (String sigma : currentRoots) {
				DeltasContainer newResult = getNewDeltasContainer();

				// Go through all previously created deltas and push the current
				// label in them while keeping the constraints that is reflected
				// by the childTpParent map

				Iterator<Delta> iter = dc.iterator();
				while (iter.hasNext()) {
					Delta delta = iter.next();
					int maxIdx = 0;

					// If this label has parents, it'll come after the max of
					// them. o.w., it'll come in all possible indices
					if (origChildToParent.containsKey(sigma)) {
						for (String parentLabel : origChildToParent.get(sigma)) {
							// Integer parentIdx = delta.getLabelToIndex().get(parentLabel);
							Integer parentIdx = delta.getLabelPosition(parentLabel);
							maxIdx = Math.max(parentIdx != null ? parentIdx : 0, maxIdx);
						}
					}

					// Insert the label to all possible indices after maxIdx and
					// store in newResult (for each j in range)
					for (int j = maxIdx; j <= delta.getNumOfLabels(); j++) {
						Delta newDelta = new Delta(delta);
						// Create delta+j
						for (String key : delta.getKeySet()) {
							int keyCurrentIdx = delta.getLabelPosition(key);
							if (keyCurrentIdx > j) {
								newDelta.putKeyValue(key, keyCurrentIdx + 1);
							} else {
								newDelta.putKeyValue(key, keyCurrentIdx);
							}
						}
						// Set the label to the current position
						newDelta.putKeyValue(sigma, j + 1);
						newDelta.createStrForHash();
						if (newResult.getDelta(newDelta) == null) {
							newResult.addDelta(newDelta);
						}
					}
				}
				childToParent.remove(sigma);
				dc = newResult;

				// Omit the added labels from the list of dependencies of others
				HashSet<String> dependantLabels = parentToChild.get(sigma);
				if (dependantLabels != null) {
					// Remove this label from the labels that the dependantLabel
					// depends on
					for (String dependantLabel : dependantLabels) {
						childToParent.get(dependantLabel).remove(sigma);
					}
				}
			}
		}

		// Go over the deltas and replace the key-as-sigma to key-as-label
		DeltasContainer result = getNewDeltasContainer();

		Iterator<Delta> iter = dc.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			Delta newDelta = new Delta();
			for (String sigma : delta.getKeySet()) {
				HashSet<String> labelsOfSigma = topProbArgs.getSigmaToGammaValueMap().get(sigma);
				for (String label : labelsOfSigma) {
					newDelta.putKeyValue(label, delta.getLabelPosition(sigma));
				}
			}
			if (isDeltaConsistent(topProbArgs, newDelta)) {
				newDelta.createStrForHash();
				result.addDelta(newDelta);
			}
		}

		return result;
	}

	private static void fillDependenciesMaps(TopProbArgs topProbArgs, HashSet<Node> roots,
			HashMap<String, HashSet<String>> childToParent, HashMap<String, HashSet<String>> parentToChild) {

		for (String sigma : topProbArgs.getImgGamma()) {
			childToParent.put(sigma, new HashSet<>());
		}

		while (!roots.isEmpty()) {
			Node node = roots.iterator().next();
			roots.remove(node);
			HashMap<String, String> gamma = topProbArgs.getGamma();
			String parentLabel = node.getLabel();
			HashSet<String> parentChildren = parentToChild.get(gamma.get(parentLabel));
			if (parentChildren == null) {
				parentChildren = new HashSet<>();
				parentToChild.put(gamma.get(parentLabel), parentChildren);
			}
			for (Node child : node.getChildren()) {
				String childLabel = child.getLabel();
				parentChildren.add(gamma.get(childLabel));
				HashSet<String> childParents = childToParent.get(gamma.get(childLabel));
				childParents.add(gamma.get(parentLabel));
			}
			roots.addAll(node.getChildren());
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
	 * lambda(l), if tau(sigma) < delta(l), then: parents(l) neq {} and tau(sigma) <
	 * max_{l' in parents(l)} delta(l')
	 * 
	 * In particular, if parents(l) = {}, then gamma(l) is the highest ranked sigma
	 * s.t l in lambda(sigma)
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

		// Check top-matching's constraints
		HashSet<String> parentsOfL = this.topMatchingArgs.getLabelToParentsMap().containsKey(l.getLabel())
				? this.topMatchingArgs.getLabelToParentsMap().get(l.getLabel())
				: new HashSet<>();

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
							minIndexForItem = Math.min(minIndexForItem, delta.getLabelPosition(label));
						}
					}
					if (minIndexForItem < delta.getLabelPosition(l.getLabel())) {
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
				maxIndexOfParent = Math.max(maxIndexOfParent, delta.getLabelPosition(parent));
			}

			for (String otherSigmaInLabel : l.getItems()) {
				if (otherSigmaInLabel.equals(topProbArgs.getGamma().get(l.getLabel()))
						|| !topProbArgs.getImgGamma().contains(otherSigmaInLabel)) {
					continue;
				}
				String labelOfSigma = topProbArgs.getSigmaToGammaValueMap().get(otherSigmaInLabel).iterator().next();
				int indexOfOtherSigma = delta.getLabelPosition(labelOfSigma);
				if (indexOfOtherSigma > maxIndexOfParent && indexOfOtherSigma < delta.getLabelPosition(l.getLabel())) {
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
