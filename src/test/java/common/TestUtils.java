package common;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import binarytopmatching.BinaryMatchingAlgorithm;
import bruteforce.BruteforceAlgorithm;
import bruteforce.ExplicitDistribution;
import general.Distribution;
import general.GeneralUtils;
import general.Mallows;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import liftedtopmatching.LiftedTopMatchingAlgorithm;
import pattern.Graph;
import pattern.GraphGenerator;
import stats.Stats;
import topmatching.SimpleDistribution;
import topmatching.TopMatchingAlgorithm;

public class TestUtils {

	private static final Logger logger = Logger.getLogger(TestUtils.class.getName());

	public static ArrayList<String> summaries = new ArrayList<>();

	private static double Epsilon = 10e-6;

	private static final String TOP_MATCHNING = "Top Matching";

	private static final String BINARY_MATCHNING = "Binary Matching";
	
	private static final String LIFTED_TOP_MATCHNING = "Lifted Top Matching";

	public static boolean runTest(int graphId, int numItems) {
		Graph graph = GraphGenerator.GetGraph(graphId);

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		Distribution explicitDistribution = new ExplicitDistribution(model);

		double exactProb = new BruteforceAlgorithm().calculateProbability(graph, explicitDistribution);

		Distribution simpleDistribution = new SimpleDistribution(model);

		double topMatchingProb = new TopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		return Math.abs(exactProb - topMatchingProb) < Epsilon;
	}

	public static boolean runBinaryMatchingTest(int graphId, int numItems) {
		Graph graph = GraphGenerator.GetGraph(graphId);

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		Distribution explicitDistribution = new ExplicitDistribution(model);

		double exactProb = new BruteforceAlgorithm().calculateProbability(graph, explicitDistribution);

		Distribution simpleDistribution = new SimpleDistribution(model);

		double topBinaryProb = new BinaryMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		return Math.abs(exactProb - topBinaryProb) < Epsilon;
	}

	public static boolean runLiftedTopMatchingTest(int graphId, int numItems) {
		Graph graph = GraphGenerator.GetGraph(graphId);

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		Distribution explicitDistribution = new ExplicitDistribution(model);

		double exactProb = new BruteforceAlgorithm().calculateProbability(graph, explicitDistribution);

		Distribution simpleDistribution = new SimpleDistribution(model);

		double liftedTopMatchingProb = new LiftedTopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		return Math.abs(exactProb - liftedTopMatchingProb) < Epsilon;
	}

	public static boolean runBinaryMatchingRandomTest(Graph graph, int numItems, int numOfLabels, int scenario) {
		StringBuilder summary = new StringBuilder(
				String.format("(Labels,Items): (%d,%d), Top Matching: ", numOfLabels, numItems));

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		Stats stats = new Stats(String.format("Random-%d", scenario), numItems, numOfLabels, graph.toString());

		// TopMatching
		GeneralArgs.currentAlgorithm = AlgorithmType.TOP_MATCHNING;

		stats.setAlgorithm(TOP_MATCHNING);

		Distribution simpleDistribution = new SimpleDistribution(model);

		stats.setStartTimeDate(new Date());

		logger.info(String.format("Running top-matchnig algorithm for a random scenario %d with %d labels and %d items",
				scenario, numOfLabels, numItems));

		double topMatchingProb = new TopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		stats.setEndTimeDate(new Date());

		summary.append(String.format("%f (%s MS), ", topMatchingProb,
				(stats.getEndTimeDate().getTime() - stats.getStartTimeDate().getTime())));

		stats.setProbability(topMatchingProb);

		System.out.println(stats);

		// BinaryMatching
		stats = new Stats(String.format("Random-%d", scenario), numItems, numOfLabels, graph.toString());

		GeneralArgs.currentAlgorithm = AlgorithmType.BINARY_MATCHING;

		stats.setAlgorithm(BINARY_MATCHNING);

		stats.setStartTimeDate(new Date());

		logger.info(
				String.format("Running binary-matchnig algorithm for a random scenario %d with %d labels and %d items",
						scenario, numOfLabels, numItems));

		double topBinaryProb = new BinaryMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		stats.setEndTimeDate(new Date());

		summary.append(String.format("Binary Matching: %f (%s MS)", topBinaryProb,
				(stats.getEndTimeDate().getTime() - stats.getStartTimeDate().getTime())));

		if (topBinaryProb != 0.000000) {
			summaries.add(summary.toString());
		} else {
			summaries.add("NI");
		}

		stats.setProbability(topBinaryProb);

		System.out.println(stats);

		return Math.abs(topMatchingProb - topBinaryProb) < Epsilon;

	}
	
	public static boolean runLiftedTopMatchingRandomTest(Graph graph, int numItems, int numOfLabels, int scenario) {
		StringBuilder summary = new StringBuilder(
				String.format("(Labels,Items): (%d,%d), Top Matching: ", numOfLabels, numItems));
		
		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);
		
		Stats stats = new Stats(String.format("Random-%d", scenario), numItems, numOfLabels, graph.toString());
		
		// TopMatching
		GeneralArgs.currentAlgorithm = AlgorithmType.TOP_MATCHNING;
		
		stats.setAlgorithm(TOP_MATCHNING);
		
		Distribution simpleDistribution = new SimpleDistribution(model);
		
		stats.setStartTimeDate(new Date());
		
		logger.info(String.format("Running top-matchnig algorithm for a random scenario %d with %d labels and %d items",
				scenario, numOfLabels, numItems));
		
		double topMatchingProb = new TopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);
		
		stats.setEndTimeDate(new Date());
		
		summary.append(String.format("%f (%s MS), ", topMatchingProb,
				(stats.getEndTimeDate().getTime() - stats.getStartTimeDate().getTime())));
		
		stats.setProbability(topMatchingProb);
		
		System.out.println(stats);
		
		// LiftedTopMatching
		stats = new Stats(String.format("Random-%d", scenario), numItems, numOfLabels, graph.toString());
		
		GeneralArgs.currentAlgorithm = AlgorithmType.LIFTED_TOP_MATCHING;
		
		stats.setAlgorithm(LIFTED_TOP_MATCHNING);
		
		stats.setStartTimeDate(new Date());
		
		logger.info(
				String.format("Running lifted-top-matchnig algorithm for a random scenario %d with %d labels and %d items",
						scenario, numOfLabels, numItems));
		
		double liftedTopMatchingProb = new LiftedTopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);
		
		stats.setEndTimeDate(new Date());
		
		summary.append(String.format("Lifted Top Matching: %f (%s MS)", liftedTopMatchingProb,
				(stats.getEndTimeDate().getTime() - stats.getStartTimeDate().getTime())));
		
		if (liftedTopMatchingProb != 0.000000) {
			summaries.add(summary.toString());
		} else {
			summaries.add("NI");
		}
		
		stats.setProbability(liftedTopMatchingProb);
		
		System.out.println(stats);
		
		return Math.abs(topMatchingProb - liftedTopMatchingProb) < Epsilon;
		
	}

}
