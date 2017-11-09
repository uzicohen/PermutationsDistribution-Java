package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import general.Distribution;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import graph.Graph;
import liftedtopmatching.LiftedTopMatchingAlgorithm;
import stats.Stats;
import topmatching.TopMatchingAlgorithm;

public class EarlyPruningExperiment {

	private static final Logger logger = Logger.getLogger(EarlyPruningExperiment.class.getName());

	private static final String LINUX_INPUT_PATH = "/home/uzicohen/Desktop/workspace/RIMPPDInference/src/main/java/resources/experiments/input/input.csv";

	private static final String LINUX_OUTPUT_FOLDER_PATH = "/home/uzicohen/Desktop/workspace/RIMPPDInference/src/main/java/resources/experiments/output/";

	private static final String WINDOWS_INPUT_PATH = "C:\\Users\\Uzi Cohen\\Documents\\eclipseWorkplace\\RIMPPDInference\\src\\main\\java\\resources\\experiments\\input\\input.csv";

	private static final String WINDOWS_OUTPUT_FOLDER_PATH = "C:\\Users\\Uzi Cohen\\Documents\\eclipseWorkplace\\RIMPPDInference\\src\\main\\java\\resources\\experiments\\output\\";

	private static final String INPUT_PATH;

	private static final String OUTPUT_FOLDER_PATH;

	private static ArrayList<ExperimentData> experimentsData;

	static {
		INPUT_PATH = System.getProperty("os.name").equals("Windows 10") ? WINDOWS_INPUT_PATH : LINUX_INPUT_PATH;

		OUTPUT_FOLDER_PATH = System.getProperty("os.name").equals("Windows 10") ? WINDOWS_OUTPUT_FOLDER_PATH
				: LINUX_OUTPUT_FOLDER_PATH;

	}

	private static ArrayList<ExperimentData> getExperimentData() {
		ArrayList<ExperimentData> result = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
			boolean firstRow = true;
			String experimentRow = null;
			while ((experimentRow = reader.readLine()) != null) {
				if (firstRow) {
					firstRow = false;
					continue;
				}
				result.add(new ExperimentData(experimentRow, 1));
			}
			reader.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void addStatsRowToQueryFile(ExperimentData experimentData, Stats withoutEarlyPruningStats,
			Stats withEarlyPruningStats) {
		String filePath = String.format("%s/earlyPruning.csv", OUTPUT_FOLDER_PATH);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(filePath), true));
			// scenario-id, withoutEarlyPruningTime, withEarlyPruningTime,
			// improvement-ratio, w\o-1, ..., w\o-20, w-1, ..., w-20
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= 20; i++) {
				sb.append(withoutEarlyPruningStats.getPhiToProbability().get((double) i));
				sb.append(",");
			}
			for (int i = 1; i <= 20; i++) {
				sb.append(withEarlyPruningStats.getPhiToProbability().get((double) i));
				if (i < 20) {
					sb.append(",");
				}
			}
			float improvementRatio = ((float) withoutEarlyPruningStats.getTotalTime())
					/ (float) withEarlyPruningStats.getTotalTime();
			String statsRow = String.format("%d,%d,%d,%f,%s\n", experimentData.getScenrioId(),
					withoutEarlyPruningStats.getTotalTime(), withEarlyPruningStats.getTotalTime(), improvementRatio,
					sb.toString());
			writer.write(statsRow);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Stats runInference(ExperimentData experimentData, boolean earlyPruning) {
		GeneralArgs.earlyPrunningOptimization = earlyPruning;
		Stats stats = new Stats(experimentData.getDistributions().get(0).getModel().getModal(), 6,
				experimentData.getGraph().toString());
		stats.setSectionDescription(String.format("Inference over scenrio-id %d, earlyPruning = %s",
				experimentData.getScenrioId(), earlyPruning));

		stats.setAlgorithm("Lifted Top Matching");

		stats.setStartTimeDate(new Date());

		Graph graph = experimentData.getGraph();
		ArrayList<Distribution> distributions = experimentData.getDistributions();

		stats.setStartTimeDate(new Date());

		stats.setPhiToProbability(new LiftedTopMatchingAlgorithm(graph, distributions).calculateProbability());

		stats.setEndTimeDate(new Date());

		System.out.println(stats);

		return stats;
	}

	private static void runExperiment(ExperimentData experimentData) {
		GeneralArgs.runMultiThread = false;
		GeneralArgs.numOfThreads = 1;
		logger.info(String.format(
				"Runnning experiment with Lifted Top Matching algorithm for scenrio-id %d, number of threads %d",
				experimentData.getScenrioId(), experimentData.getNumOfThreads()));

		logger.info("Running inference");
		Stats withoutEarlyPruningStats = runInference(experimentData, false);
		Stats withEarlyPruningStats = runInference(experimentData, true);

		logger.info("Adding a new Stats row to the stats file");
		addStatsRowToQueryFile(experimentData, withoutEarlyPruningStats, withEarlyPruningStats);

	}

	private static void runExperiments() {
		for (int i = 0; i < experimentsData.size(); i++) {
			ExperimentData currentExperiment = experimentsData.get(i);
			runExperiment(currentExperiment);
		}
	}

	public static void main(String[] args) {
		GeneralArgs.earlyPrunningOptimization = true;
		GeneralArgs.sharedModalOptimization = true;
		GeneralArgs.verbose = true;

		logger.info("Starting invoking the experiment");

		logger.info("Reading input file to grab experiments data");
		experimentsData = getExperimentData();

		runExperiments();

		logger.info("Done invoking the experiment!!!");
	}

}
