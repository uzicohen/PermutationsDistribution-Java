package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.IAlgorithm;
import general.main.GeneralArgs;
import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;

public class TopMatchingAlgorithm implements IAlgorithm {

	private static final Logger logger = Logger.getLogger(TopMatchingAlgorithm.class.getName());

	private TopMatchingArgs topMatchingArgs;

	private double probability;

	private class AssignmentProbCalculator implements Runnable {
		private ArrayList<HashMap<String, String>> assignments;

		public AssignmentProbCalculator(ArrayList<HashMap<String, String>> assignments) {
			this.assignments = assignments;
		}

		@Override
		public void run() {
			calculateProbabilityForSubsetOfAssignments(assignments);
		}

	}

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

		int numOfAssignments = allPossibleAssignments.size();

		if (GeneralArgs.verbose) {
			logger.info(String.format("Calculating probability over %d assignments", numOfAssignments));
		}

		if (GeneralArgs.runMultiThread) {
			ExecutorService executor = Executors.newFixedThreadPool(GeneralArgs.numOfThreads);

			for (HashMap<String, String> assignment : allPossibleAssignments) {
				ArrayList<HashMap<String, String>> assignments = new ArrayList<>();
				assignments.add(assignment);
				executor.execute(new AssignmentProbCalculator(assignments));

			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

		} else {
			calculateProbabilityForSubsetOfAssignments(allPossibleAssignments);
		}
		return this.probability;
	}

	private void calculateProbabilityForSubsetOfAssignments(ArrayList<HashMap<String, String>> assignments) {
		double prob = 0.0;
		for (HashMap<String, String> gamma : assignments) {
			TopProb topProb = new TopProb(gamma, topMatchingArgs);
			prob += topProb.calculate();
		}
		updateProb(prob);
	}

	private synchronized void updateProb(double prob) {
		this.probability += prob;
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
					? labelToParentsMap.get(child.getLabel())
					: new HashSet<>();
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
