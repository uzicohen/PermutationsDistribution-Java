package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.Algorithm;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;

public class TopMatchingAlgorithm extends Algorithm {

	private static final Logger logger = Logger.getLogger(TopMatchingAlgorithm.class.getName());

	private TopMatchingArgs topMatchingArgs;

	private HashMap<Double, Double> phiToProbability;

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

	public TopMatchingAlgorithm(Graph graph, ArrayList<Distribution> distributions) {
		super(graph, distributions);
		this.phiToProbability = new HashMap<>();
	}

	@Override
	public HashMap<Double, Double> calculateProbability() {
		GeneralArgs.currentAlgorithm = AlgorithmType.TOP_MATCHNING;

		if (GeneralArgs.verbose) {
			logger.info("Generating all possible assignments");
		}

		ArrayList<HashMap<String, String>> allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);

		// For each label, we keep in a dictionary it's parents
		HashMap<String, HashSet<String>> labelToParentsMap = new HashMap<>();

		// For each sigma, we keep in a dictionary it's possible labels
		HashMap<String, HashSet<String>> lambda = new HashMap<>();

		// Create the data structures in the level of top matching
		preProcessGraph(graph, labelToParentsMap, lambda);

		this.topMatchingArgs = new TopMatchingArgs(graph, distributions, labelToParentsMap, lambda,
				GeneralUtils.getPhiToInsertionProbabilities(distributions));

		int numOfAssignments = allPossibleAssignments.size();

		if (GeneralArgs.verbose) {
			logger.info(String.format("Calculating probability over %d assignments", numOfAssignments));
			if (GeneralArgs.runMultiThread) {
				logger.info(String.format("Multithread is true. Running with %d threads", GeneralArgs.numOfThreads));
			} else {
				logger.info("Multithread is false");
			}
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

		logger.info("Done calculating the probability");

		return this.phiToProbability;
	}

	@Override
	public HashMap<Double, Double> calculateProbability(int itemNumToStoreInCache) {
		return calculateProbability();
	}

	private void calculateProbabilityForSubsetOfAssignments(ArrayList<HashMap<String, String>> assignments) {
		int i = 1;
		for (HashMap<String, String> gamma : assignments) {
			TopProb topProb = new TopProb(gamma, topMatchingArgs);
			HashMap<Double, Double> currentProbs = topProb.calculate();
			for (Double phi : currentProbs.keySet()) {
				updateProb(phi, currentProbs.get(phi));
			}
			if (GeneralArgs.verbose && !GeneralArgs.runMultiThread) {
				logger.info(String.format("Done with %d assignments", i++));
			}
		}
	}

	private synchronized void updateProb(double phi, double prob) {
		double newProb = 0.0;
		if (this.phiToProbability.containsKey(phi)) {
			newProb = this.phiToProbability.get(phi);
		}
		this.phiToProbability.put(phi, newProb + prob);
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
