package binarytopmatching.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;
import topmatching.TopMatchingArgs;
import topmatching.TopProbArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.EnhancedDeltasContainer;
import topmatching.delta.EnhancedDeltasContainerGenerator;

public class LabelsDeltasContainerGenerator {

	private TopMatchingArgs topMatchingArgs;

	public DeltasContainer getInitialDeltas1(TopMatchingArgs topMatchingArgs) {
		EnhancedDeltasContainer result = new EnhancedDeltasContainer(topMatchingArgs);

		Graph graph = topMatchingArgs.getG();

		ArrayList<HashMap<String, String>> allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);

		EnhancedDeltasContainerGenerator deltasGenerator = new EnhancedDeltasContainerGenerator();
		deltasGenerator.init(topMatchingArgs);

		for (HashMap<String, String> gamma : allPossibleAssignments) {
			DeltasContainer r = deltasGenerator.getInitialDeltas(new TopProbArgs(gamma));
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				delta.fillLambda(gamma);
				Delta exisitingDelta = result.getDelta(delta);
				if (exisitingDelta != null) {
					exisitingDelta.fillLambda(gamma);
				}
				result.addDelta(exisitingDelta != null ? exisitingDelta : delta);
			}
		}
		return result;
	}

	public DeltasContainer getInitialDeltas(TopMatchingArgs topMatchingArgs) {
		this.topMatchingArgs = topMatchingArgs;

		DeltasContainer result = new EnhancedDeltasContainer(topMatchingArgs);
		result.addDelta(new Delta());

		// A map from label to all the label it depends on
		HashMap<String, HashSet<String>> childToParent = new HashMap<>();

		// A map from label to all the labels that depend on it
		HashMap<String, HashSet<String>> parentToChild = new HashMap<>();

		HashSet<Node> roots = new HashSet<>(this.topMatchingArgs.getG().getRoots());

		fillDependenciesMaps(roots, childToParent, parentToChild);

		// Make a copy of the childToParents map
		HashMap<String, HashSet<String>> origChildToParent = new HashMap<>();
		for (String key : childToParent.keySet()) {
			HashSet<String> currentParents = new HashSet<>(childToParent.get(key));
			origChildToParent.put(key, currentParents);
		}

		// Go over the maps. For each label that has no parents in
		// childToParent, we can put it in a new delta and remove it from
		// the map
		while (childToParent.size() != 0) {
			ArrayList<String> currentRoots = new ArrayList<>();
			// Get those labels that do not depend on any other
			for (String sigma : childToParent.keySet()) {
				if (childToParent.get(sigma).isEmpty()) {
					currentRoots.add(sigma);
				}
			}

			for (String label : currentRoots) {
				DeltasContainer newResult = new EnhancedDeltasContainer(topMatchingArgs);

				// Go through all previously created deltas and push the current
				// label in them while keeping the constraints that is reflected
				// by the childTpParent map

				Iterator<Delta> iter = result.iterator();
				while (iter.hasNext()) {
					Delta delta = iter.next();
					int maxIdx = 0;

					// If this label has parents, it'll come after the max of
					// them. o.w., it'll come in all possible indices
					for (String parentLabel : origChildToParent.get(label)) {
						Integer parentIdx = delta.getLabelPosition(parentLabel);
						maxIdx = Math.max(parentIdx != null ? parentIdx : 0, maxIdx);
					}

					// Insert the label to all possible indices after maxIdx and
					// store in newResult (for each j in range)
					for (int j = maxIdx; j <= delta.getNumOfLabels(); j++) {
						Delta newDelta = new Delta(delta);

						for (String key : delta.getKeySet()) {
							int keyCurrentIdx = delta.getLabelPosition(key);
							if (keyCurrentIdx > j) {
								newDelta.putKeyValue(key, keyCurrentIdx + 1);
							} else {
								newDelta.putKeyValue(key, keyCurrentIdx);
							}
						}

						// Set the label to the current position
						newDelta.putKeyValue(label, j + 1);

						newDelta.createStrForHash();

						if (newResult.getDelta(newDelta) == null) {
							newResult.addDelta(newDelta);
						}
					}
				}
				childToParent.remove(label);
				result = newResult;

				// Omit the added labels from the list of dependencies of others
				HashSet<String> dependantLabels = parentToChild.get(label);
				if (dependantLabels != null) {
					// Remove this label from the labels that the dependantLabel
					// depends on
					for (String dependantLabel : dependantLabels) {
						childToParent.get(dependantLabel).remove(label);
					}
				}

			}
		}
		return result;
	}

	private static void fillDependenciesMaps(HashSet<Node> roots, HashMap<String, HashSet<String>> childToParent,
			HashMap<String, HashSet<String>> parentToChild) {

		while (!roots.isEmpty()) {
			Node node = roots.iterator().next();
			roots.remove(node);
			String parentLabel = node.getLabel();
			// Put this label in both maps so we'll know we have all labels as keys anyway
			HashSet<String> parentParents = childToParent.get(parentLabel);
			if (parentParents == null) {
				parentParents = new HashSet<>();
				childToParent.put(parentLabel, parentParents);
			}

			HashSet<String> parentChildren = parentToChild.get(parentLabel);
			if (parentChildren == null) {
				parentChildren = new HashSet<>();
				parentToChild.put(parentLabel, parentChildren);
			}

			for (Node child : node.getChildren()) {
				String childLabel = child.getLabel();
				parentChildren.add(childLabel);
				HashSet<String> childParents = childToParent.get(childLabel);
				if (childParents == null) {
					childParents = new HashSet<>();
					childToParent.put(childLabel, childParents);
				}
				childParents.add(parentLabel);
			}
			roots.addAll(node.getChildren());
		}

	}
}
