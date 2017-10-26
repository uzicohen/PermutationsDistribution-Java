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

	private static final String LINUX_INPUT_FOLDER_PATH = "/home/uzicohen/Desktop/workspace/PermutationsDistribution/src/main/java/resources/experiments/input/";

	private static final String LINUX_OUTPUT_FOLDER_PATH = "/home/uzicohen/Desktop/workspace/PermutationsDistribution/src/main/java/resources/experiments/output/";

	private static final String WINDOWS_INPUT_FOLDER_PATH = "C:\\Users\\Uzi Cohen\\Documents\\eclipseWorkplace\\PermutationsDistribution\\src\\main\\java\\resources\\experiments\\input\\";

	private static final String WINDOWS_OUTPUT_FOLDER_PATH = "C:\\Users\\Uzi Cohen\\Documents\\eclipseWorkplace\\PermutationsDistribution\\src\\main\\java\\resources\\experiments\\output\\";

	private static final String INPUT_FOLDER_PATH;

	private static final String OUTPUT_FOLDER_PATH;

	static {
		INPUT_FOLDER_PATH = System.getProperty("os.name").equals("Windows 10") ? WINDOWS_INPUT_FOLDER_PATH
				: LINUX_INPUT_FOLDER_PATH;
		OUTPUT_FOLDER_PATH = System.getProperty("os.name").equals("Windows 10") ? WINDOWS_OUTPUT_FOLDER_PATH
				: LINUX_OUTPUT_FOLDER_PATH;
	}

	private static int maximalNumOfThreads = 49;

	private static int numOfExperimentsPerPattern = 3;

	// For the actual graph, add 19
	private static int[] patternNums = new int[] { 0, 1, 2, 3 };

	private static class RowThread {
		private int numOfRow = 0;
		private int numOfThreads = 1;
		private boolean wasUsed = false;
	}

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

	private static void runExperiment(int patternNum, int numOfThreads, int rowNum, ExperimentData experimentData) {
		if (numOfThreads == 1) {
			GeneralArgs.runMultiThread = false;
			GeneralArgs.numOfThreads = 1;
		} else {
			GeneralArgs.runMultiThread = true;
			GeneralArgs.numOfThreads = numOfThreads;
		}
		Stats topMatchingStats = runInference(experimentData, rowNum, AlgorithmType.TOP_MATCHNING);
		Stats liftedTopMatchingStats = runInference(experimentData, rowNum, AlgorithmType.LIFTED_TOP_MATCHING);

		logger.info(String.format("Adding a new Stats row to the stats file for pattern %d", patternNum));
		addStatsRowToPatternFile(patternNum, rowNum, topMatchingStats, liftedTopMatchingStats);

	}

	private static void addStatsRowToPatternFile(int patternNum, int rowNum, Stats topMatchingStats,
			Stats liftedTopMatchingStats) {
		BufferedWriter writer = null;
		try {
			String filePath = String.format("%s/q%d.csv", OUTPUT_FOLDER_PATH, patternNum);
			writer = new BufferedWriter(new FileWriter(new File(filePath), true));
			float improvementRatio = ((float) topMatchingStats.getTotalTime())
					/ ((float) liftedTopMatchingStats.getTotalTime());
			String statsRow = String.format("%d,%d,%d,%d,%f\n", rowNum, GeneralArgs.numOfThreads,
					topMatchingStats.getTotalTime(), liftedTopMatchingStats.getTotalTime(), improvementRatio);
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

	private static RowThread createStatsFile(int patternNum) {
		RowThread result = new RowThread();
		String filePath = String.format("%s/q%d.csv", OUTPUT_FOLDER_PATH, patternNum);
		if (new File(filePath).exists()) {
			BufferedReader reader = null;
			boolean firstLine = true;
			try {
				reader = new BufferedReader(new FileReader(new File(filePath)));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (!line.isEmpty() && !firstLine) {
						result.numOfRow = Integer.parseInt(line.split(",")[0]);
						result.numOfThreads = Integer.parseInt(line.split(",")[1]);
					}
					firstLine = false;
				}
				reader.close();
				result.numOfThreads += 2;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(new File(filePath)));
				writer.write(
						"Row Number, Number Of Threads, Top Matching (MS), Lifted Top Matching (MS), Improvement Ratio\n");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
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
			RowThread rowThread = createStatsFile(patternNum);
			if (rowThread.numOfThreads > maximalNumOfThreads) {
				rowThread.numOfRow++;
				rowThread.numOfThreads = 1;
			}

			for (int rowNum = rowThread.numOfRow; rowNum < Math.min(numOfExperimentsPerPattern,
					experimentsData.size()); rowNum++) {
				ExperimentData experimentData = experimentsData.get(rowNum);
				if(rowThread.wasUsed){
					rowThread.numOfThreads = 1;
				}
				for (int numOfThreads = rowThread.numOfThreads; numOfThreads <= maximalNumOfThreads; numOfThreads += 2) {
					rowThread.wasUsed = true;
					
					logger.info(String.format(
							"Running experiment for pettern number %d, row number %d, number of therads %d", patternNum,
							rowNum, numOfThreads));
					runExperiment(patternNum, numOfThreads, rowNum, experimentData);
				}
			}
			logger.info(String.format("Done running experiments for pattern number %d", patternNum));
		}
		logger.info("Done invoking the experiment!!!");
	}

}
