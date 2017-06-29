package bruteforce;

import java.util.ArrayList;
import java.util.HashMap;

import general.Distribution;
import general.IAlgorithm;
import general.Permutation;
import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;

public class BruteforceAlgorithm implements IAlgorithm {

	private static class Result {
		public Result() {
			res = true;
		}

		public boolean res;
	}

	private void CheckOneAssignmentAux(ArrayList<Node> nodes, HashMap<String, Integer> itemToPosition,
			HashMap<String, String> assignment, Result result) {
		if (nodes.isEmpty()) {
			return;
		}

		for (Node node : nodes) {
			int parentPosition = itemToPosition.get(assignment.get(node.getLabel()));
			for (Node child : node.getChildren()) {
				int childPosition = itemToPosition.get(assignment.get(child.getLabel()));
				if (parentPosition >= childPosition) {
					result.res = false;
					return;
				}
			}
			CheckOneAssignmentAux(node.getChildren(), itemToPosition, assignment, result);
		}
	}

	private boolean CheckOneAssignment(Graph graph, Permutation permutation, HashMap<String, String> assignment) {
		HashMap<String, Integer> itemToPosition = new HashMap<>();
		for (int i = 0; i < permutation.getItemsOrder().size(); i++) {
			itemToPosition.put(permutation.getItemsOrder().get(i), i + 1);
		}
		Result result = new Result();
		CheckOneAssignmentAux(graph.getRoots(), itemToPosition, assignment, result);
		return result.res;
	}

	private boolean IsPermutationSatisfyGraph(Graph graph, Permutation permutation) {
		// TODO: This happens many times
		ArrayList<HashMap<String, String>> allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);
		for (HashMap<String, String> assignment : allPossibleAssignments) {
			if (this.CheckOneAssignment(graph, permutation, assignment)) {
				return true;
			}
		}
		return false;
	}

	public double calculateProbability(Graph graph, Distribution distribution) {
		double result = 0.0;
		for (Permutation permutation : distribution.getPermutations()) {
			result += IsPermutationSatisfyGraph(graph, permutation) ? permutation.getProbability() : 0.0;
		}
		return result;
	}
}
