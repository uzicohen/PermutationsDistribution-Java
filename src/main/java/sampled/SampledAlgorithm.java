package sampled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import general.Distribution;
import general.IAlgorithm;
import general.Permutation;
import general.main.GeneralArgs;
import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;

public class SampledAlgorithm implements IAlgorithm {

	private static final Logger logger = Logger.getLogger(SampledAlgorithm.class.getName());

	private static class Result {
		public Result() {
			res = true;
		}

		public boolean res;
	}

	private void checkOneAssignmentAux(ArrayList<Node> nodes, HashMap<String, Integer> itemToPosition,
			HashMap<String, String> assignment, Result result) {
		if (nodes.isEmpty()) {
			return;
		}

		for (Node node : nodes) {
			int parentPosition = itemToPosition.get(assignment.get(node.getLabel()));
			;
			for (Node child : node.getChildren()) {
				int childPosition = itemToPosition.get(assignment.get(child.getLabel()));
				if (parentPosition >= childPosition) {
					result.res = false;
					return;
				}
			}
			checkOneAssignmentAux(node.getChildren(), itemToPosition, assignment, result);
		}
	}

	private boolean checkOneAssignment(Graph graph, Permutation permutation, HashMap<String, String> assignment) {
		HashMap<String, Integer> itemToPosition = new HashMap<>();
		for (int i = 0; i < permutation.getItemsOrder().size(); i++) {
			itemToPosition.put(permutation.getItemsOrder().get(i), i + 1);
		}
		Result result = new Result();
		checkOneAssignmentAux(graph.getRoots(), itemToPosition, assignment, result);
		return result.res;
	}

	private boolean isPermutationSatisfyGraph(Graph graph, Permutation permutation) {
		ArrayList<HashMap<String, String>> allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);
		for (HashMap<String, String> assignment : allPossibleAssignments) {
			if (this.checkOneAssignment(graph, permutation, assignment)) {
				return true;
			}
		}
		return false;
	}

	public double calculateProbability(Graph graph, Distribution distribution) {

		int numOfSample = distribution.getPermutations().size();
		int counter = 0;
		logger.info(String.format("Calculating probability over %d samples", numOfSample));

		double result = 0.0;
		for (Permutation permutation : distribution.getPermutations()) {
			
			result += isPermutationSatisfyGraph(graph, permutation) ? permutation.getProbability() : 0.0;

			counter++;
			if (GeneralArgs.verbose) {
				if (counter % GeneralArgs.numSamplesForPrint == 0) {
					double perc = 100.0 * ((double) counter) / ((double) numOfSample);
					logger.info(String.format("Done with %d out of %d samples (%f perc)", counter, numOfSample, perc));
				}
			}
		}
		return result;
	}
}
