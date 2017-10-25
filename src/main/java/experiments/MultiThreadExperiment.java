package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import general.Distribution;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import liftedtopmatching.LiftedTopMatchingAlgorithm;
import pattern.Graph;
import stats.Stats;
import topmatching.TopMatchingAlgorithm;

public class MultiThreadExperiment {

	private static final Logger logger = Logger.getLogger(MultiThreadExperiment.class.getName());

	private static final String INPUT_FOLDER_PATH = "C:\\Dev\\RimExperiments\\Input\\";

	private static final String OUTPUT_FOLDER_PATH = "C:\\Dev\\RimExperiments\\Output\\";

	private static int maximalNumOfThreads = 50;

	private static int numOfExperimentsPerPattern = 1;

	// For the actual graph, add 19
	private static int[] patternNums = new int[] { 0, 1, 2, 3 };

	private static ArrayList<ExperimentData> getExperimentData(int patternNum) {
		ArrayList<ExperimentData> result = new ArrayList<>();
		BufferedReader reader = null;
		try {
			String filePath = String.format("%s/q%d.csv", INPUT_FOLDER_PATH, patternNum);
			reader = new BufferedReader(new FileReader(new File(filePath)));
			String experimentRow = null;
			while ((experimentRow = reader.readLine()) != null) {
				Graph graph = GraphGenerator.GetGraph(patternNum + 19, new ArrayList<>(), 0);
				result.add(new ExperimentData(graph, experimentRow, patternNum));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void runExperiment(int patternNum, int numOfThreads, ArrayList<ExperimentData> experimentsData) {
		if (numOfThreads == 1) {
			GeneralArgs.runMultiThread = false;
			GeneralArgs.numOfThreads = 1;
		} else {
			GeneralArgs.runMultiThread = true;
			GeneralArgs.numOfThreads = numOfThreads;
		}

		for (int rowNum = 0; rowNum < Math.min(numOfExperimentsPerPattern, experimentsData.size()); rowNum++) {
			logger.info(String.format("Running experiment %d for pattern number %d with %d threads", rowNum, patternNum,
					numOfThreads));

			ExperimentData experimentData = experimentsData.get(rowNum);
			Stats topMatchingStats = runInference(experimentData, rowNum, AlgorithmType.TOP_MATCHNING);
			Stats liftedTopMatchingStats = runInference(experimentData, rowNum, AlgorithmType.LIFTED_TOP_MATCHING);

			logger.info(String.format("Adding a new Stats row to the stats file for pattern %d", patternNum));
			addStatsRowToPatternFile(patternNum, rowNum, topMatchingStats, liftedTopMatchingStats);
		}
	}

	private static void addStatsRowToPatternFile(int patternNum, int rowNum, Stats topMatchingStats,
			Stats liftedTopMatchingStats) {
		BufferedWriter writer = null;
		try {
			String filePath = String.format("%s/q%d.csv", OUTPUT_FOLDER_PATH, patternNum);
			writer = new BufferedWriter(new FileWriter(new File(filePath), true));
			float improvementRatio = ((float) topMatchingStats.getTotalTime())
					/ ((float) liftedTopMatchingStats.getTotalTime());
			String statsRow = String.format("%d,%d,%d,%d,%d,%f\n", patternNum, rowNum, GeneralArgs.numOfThreads,
					topMatchingStats.getTotalTime(), liftedTopMatchingStats.getTotalTime(),
					improvementRatio);
			writer.write(statsRow);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Stats runInference(ExperimentData experimentData, int rowNum, AlgorithmType algorithmType) {
		String algorithmName = algorithmType == AlgorithmType.TOP_MATCHNING ? "Top Matching" : "Lifted Top Matching";

		Stats stats = new Stats(experimentData.getDistributions().get(0).getModel().getModal(), 5,
				experimentData.getGraph().toString());
		stats.setSectionDescription(
				String.format("Inference over pattern %d, row %d", experimentData.getPatternNum(), rowNum));

		stats.setAlgorithm(algorithmName);

		stats.setStartTimeDate(new Date());

		Graph graph = experimentData.getGraph();
		ArrayList<Distribution> distributions = experimentData.getDistributions();

		stats.setStartTimeDate(new Date());

		stats.setPhiToProbability(algorithmType == AlgorithmType.TOP_MATCHNING
				? new TopMatchingAlgorithm(graph, distributions).calculateProbability()
				: new LiftedTopMatchingAlgorithm(graph, distributions).calculateProbability());

		stats.setEndTimeDate(new Date());

		System.out.println(stats);

		return stats;
	}

	private static void createStatsFile(int patternNum) {
		BufferedWriter writer = null;
		try {
			String filePath = String.format("%s/q%d.csv", OUTPUT_FOLDER_PATH, patternNum);
			writer = new BufferedWriter(new FileWriter(new File(filePath)));
			writer.write(
					"Pattern Number, Row Number, Number Of Threads, Top Matching (MS), Lifted Top Matching (MS), Improvement Ratio\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		logger.info("Starting invoking the experiment");

		GeneralArgs.earlyPrunningOptimization = true;
		GeneralArgs.sharedModalOptimization = true;
		GeneralArgs.verbose = true;

		for (int patternNum : patternNums) {
			logger.info(String.format("Resolving experiments data for pattern number %d", patternNum));
			ArrayList<ExperimentData> experimentsData = getExperimentData(patternNum);

			logger.info(String.format("Creating the stats file for pattern number %d", patternNum));
			createStatsFile(patternNum);

			for (int numOfThreads = 1; numOfThreads <= maximalNumOfThreads; numOfThreads += 2) {
				logger.info(String.format("Running experiments for %d threads", numOfThreads));
				runExperiment(patternNum, numOfThreads, experimentsData);
			}

			logger.info(String.format("Done running experiments for pattern number %d", patternNum));
		}
		logger.info("Done invoking the experiment!!!");
	}

}
