package common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import general.Distribution;
import general.GeneralUtils;
import general.Mallows;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import general.main.GeneralArgs.GraphGeneratorParameters;
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

	private static final String LIFTED_TOP_MATCHNING = "Lifted Top Matching";

	private static double[] exactProbs;

	static {
		// For phi = 0.3
		exactProbs = new double[] { 0.990541370714296, 0.989000393257776, 0.732986262312703, 0.719424460431655,
				0.761006046617952, 0.934812879701345, 0.9498882307361549, 1.0, 0.6937277499487445, 0.503103,
				0.9741246606894051, 0.567747, 0.862931, 0.028696, 0.731592, 0.900444, 0.994654, 0.765118 };
	}

	public static boolean runTest(int graphId, int numItems, boolean runMultiThread, int numOfThreads,
			AlgorithmType algorithmType) {
		GeneralArgs.runMultiThread = runMultiThread;
		GeneralArgs.numOfThreads = numOfThreads;

		ArrayList<Mallows> models = new ArrayList<>();
		models.add(new Mallows(0.3));
		Graph graph = GraphGenerator.GetGraph(graphId, models, numItems);

		double exactProb = exactProbs[graphId - 1];

		ArrayList<Distribution> distributions = new ArrayList<>();
		models.forEach(model -> distributions.add(new SimpleDistribution(model)));
		
		HashMap<Double, Double> result = null;
		switch (algorithmType) {
		case TOP_MATCHNING:
			result = new TopMatchingAlgorithm(graph, distributions).calculateProbability();
			break;
		case LIFTED_TOP_MATCHING:
			result = new LiftedTopMatchingAlgorithm(graph, distributions).calculateProbability();
			break;
		default:
		}
		boolean allEqual = true;
		for (double phi : result.keySet()) {
			allEqual &= Math.abs(exactProb - result.get(phi)) < Epsilon;
		}
		return allEqual;
	}

	public static boolean runLiftedTopMatchingRandomTest(Graph graph, int numItems, int numOfLabels, int scenario,
			boolean runMultiThread, int numOfThreads) {
		GeneralArgs.runMultiThread = runMultiThread;
		GeneralArgs.numOfThreads = numOfThreads;

		StringBuilder summary = new StringBuilder(
				String.format("(Labels,Items): (%d,%d), Top Matching: ", numOfLabels, numItems));

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		GraphGeneratorParameters scenarioSettings = new GraphGeneratorParameters(scenario, numItems, numOfLabels);

		Stats stats = new Stats(scenarioSettings, graph.toString(), model.getModal());

		// TopMatching
		stats.setAlgorithm(TOP_MATCHNING);

		Distribution simpleDistribution = new SimpleDistribution(model);
		ArrayList<Distribution> distributions = new ArrayList<>();
		distributions.add(simpleDistribution);

		stats.setStartTimeDate(new Date());

		logger.info(String.format("Running top-matchnig algorithm for a random scenario %d with %d labels and %d items",
				scenario, numOfLabels, numItems));

		HashMap<Double, Double> topMatchingProbs = new TopMatchingAlgorithm(graph, distributions)
				.calculateProbability();

		stats.setEndTimeDate(new Date());

		summary.append(String.format("%f (%s MS), ", topMatchingProbs,
				(stats.getEndTimeDate().getTime() - stats.getStartTimeDate().getTime())));

		stats.setPhiToProbability(topMatchingProbs);

		System.out.println(stats);

		// LiftedTopMatching
		stats = new Stats(scenarioSettings, graph.toString(), model.getModal());

		stats.setAlgorithm(LIFTED_TOP_MATCHNING);

		stats.setStartTimeDate(new Date());

		logger.info(String.format(
				"Running lifted-top-matchnig algorithm for a random scenario %d with %d labels and %d items", scenario,
				numOfLabels, numItems));

		HashMap<Double, Double> liftedTopMatchingProbs = new LiftedTopMatchingAlgorithm(graph, distributions)
				.calculateProbability();

		stats.setEndTimeDate(new Date());

		summary.append(String.format("Lifted Top Matching: %f (%s MS)", liftedTopMatchingProbs,
				(stats.getEndTimeDate().getTime() - stats.getStartTimeDate().getTime())));

		// if (Math.abs(liftedTopMatchingProbs - 0.000000) > 10e-6 &&
		// Math.abs(liftedTopMatchingProb - 1.000000) > 10e-6) {
		// summaries.add(summary.toString());
		// } else {
		// summaries.add("NI");
		// }

		stats.setPhiToProbability(liftedTopMatchingProbs);

		System.out.println(stats);

		boolean allEqual = true;
		for (double phi : topMatchingProbs.keySet()) {
			allEqual &= Math.abs(topMatchingProbs.get(phi) - liftedTopMatchingProbs.get(phi)) < Epsilon;
		}
		return allEqual;
	}

}
