package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import general.Distribution;
import general.GeneralUtils;
import general.IAlgorithm;
import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;

public class TopMatchingAlgorithm implements IAlgorithm {

	private TopMatchingArgs topMatchingArgs;

	public TopMatchingAlgorithm() {
	}

	@Override
	public double calculateProbability(Graph graph, Distribution distribution) {
		ArrayList<HashMap<String, String>> allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);

		// For each label, we keep in a dictionary it's parents
		HashMap<String, HashSet<String>> labelToParentsMap = new HashMap<>();

		// For each sigma, we keep in a dictionary it's possible labels
		HashMap<String, HashSet<String>> lambda = new HashMap<>();

		// Create the data structures in the level of top matching
		preProcessGraph(graph, labelToParentsMap, lambda);

		this.topMatchingArgs = new TopMatchingArgs(graph, distribution, labelToParentsMap, lambda,
				GeneralUtils.getInsertionProbabilities(distribution.getModel()));

		// TODO: We can easily parallelize this section
		double result = 0.0;
		for (HashMap<String, String> gamma : allPossibleAssignments) {
			TopProb topProb = new TopProb(gamma, topMatchingArgs);
			double probability = topProb.Calculate();
			result += probability;
		}
		return result;
	}

	private void preProcessGraphAux(Node node, HashMap<String, HashSet<String>> labelToParentsMap,
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

	private void preProcessGraph(Graph graph, HashMap<String, HashSet<String>> labelToParentsMap,
			HashMap<String, HashSet<String>> lambda) {
		for (Node root : graph.getRoots()) {
			preProcessGraphAux(root, labelToParentsMap, lambda);
		}
	}
}
