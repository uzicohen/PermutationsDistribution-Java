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
import liftedtopmatching.LiftedTopMatchingAlgorithm;
import pattern.Graph;
import stats.Stats;
import topmatching.TopMatchingAlgorithm;

public class MultiThreadExperiment {

	private static final Logger logger = Logger.getLogger(MultiThreadExperiment.class.getName());

	private static final String LINUX_INPUT_PATH = "/home/uzicohen/Desktop/workspace/PermutationsDistribution/src/main/java/resources/experiments/input/input.csv";

	private static final String LINUX_OUTPUT_FOLDER_PATH = "/home/uzicohen/Desktop/workspace/PermutationsDistribution/src/main/java/resources/experiments/output/";

	private static final String WINDOWS_INPUT_PATH = "C:\\Users\\Uzi Cohen\\Documents\\eclipseWorkplace\\PermutationsDistribution\\src\\main\\java\\resources\\experiments\\input\\input.csv";

	private static final String WINDOWS_OUTPUT_FOLDER_PATH = "C:\\Users\\Uzi Cohen\\Documents\\eclipseWorkplace\\PermutationsDistribution\\src\\main\\java\\resources\\experiments\\output\\";

	private static final String INPUT_PATH;

	private static final String OUTPUT_FOLDER_PATH;

	private static final int[] queryIds = new int[] { 0, 1, 2, 3 };

	private static final int[] numOfThreads = new int[] { 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24 };

	private static final int[] bucketsLTM = new int[] { 1, 2, 3 };

	private static final int[] bucketsTM = new int[] { 1 };

	private static ArrayList<ExperimentData> experimentsDataLTM;

	private static ArrayList<ExperimentData> experimentsDataTM;

	private static HashMap<Integer, Integer> baseRunTimesLTM;

	private static HashMap<Integer, Integer> baseRunTimesTM;

	static {
		INPUT_PATH = System.getProperty("os.name").equals("Windows 10") ? WINDOWS_INPUT_PATH : LINUX_INPUT_PATH;

		OUTPUT_FOLDER_PATH = System.getProperty("os.name").equals("Windows 10") ? WINDOWS_OUTPUT_FOLDER_PATH
				: LINUX_OUTPUT_FOLDER_PATH;

		baseRunTimesLTM = new HashMap<>();
		baseRunTimesTM = new HashMap<>();

	}

	// Files

	private static ArrayList<String> readOutputFile(String filePath) {
		ArrayList<String> result = new ArrayList<>();
		if (new File(filePath).exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(new File(filePath)));
				String line = null;
				boolean firstLine = true;
				while ((line = reader.readLine()) != null) {
					if (!line.isEmpty() && !firstLine) {
						result.add(line);
					}
					firstLine = false;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static void addStatsRowToQueryFile(ExperimentData experimentData, Stats stats,
			AlgorithmType algorithmType) {
		HashMap<Integer, Integer> currentBaseRunTimes = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING
				? baseRunTimesLTM : baseRunTimesTM;
		if (experimentData.getNumOfThreads() == 1) {
			currentBaseRunTimes.put(experimentData.getScenrioId(), stats.getTotalTime());
		}

		String filePath = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING
				? String.format("%s/%s/q%d.csv", OUTPUT_FOLDER_PATH, "LTM", experimentData.getQueryId())
				: String.format("%s/%s/q%d.csv", OUTPUT_FOLDER_PATH, "TM", experimentData.getQueryId());

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(filePath), true));
			// scenario_id,num_of_threads,bucket,query_id,cartesian_product,probability,running_time,improvement_factor

			float improvementRatio = ((float) currentBaseRunTimes.get(experimentData.getScenrioId())
					/ (float) stats.getTotalTime());
			String statsRow = String.format("%d,%d,%d,%d,%d,%s,%d,%f\n", experimentData.getScenrioId(),
					experimentData.getNumOfThreads(), experimentData.getBucket(), experimentData.getQueryId(),
					experimentData.getCartesianProduct(), stats.getPhiToProbability(), stats.getTotalTime(),
					improvementRatio);
			writer.write(statsRow);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<ExperimentData> getExperimentData(AlgorithmType algorithmType) {
		int[] currentBuckets = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING ? bucketsLTM : bucketsTM;
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
				int bucket = Integer.parseInt(experimentRow.split(",")[1]);
				for (int bucketNum : currentBuckets) {
					if (bucketNum == bucket) {
						for (int i = 0; i < numOfThreads.length; i++) {
							int currentNumOfThreads = numOfThreads[i];
							result.add(new ExperimentData(experimentRow, currentNumOfThreads));
						}
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void runExperiment(ExperimentData experimentData, AlgorithmType algorithmType) {
		if (experimentData.getNumOfThreads() == 1) {
			GeneralArgs.runMultiThread = false;
			GeneralArgs.numOfThreads = 1;
		} else {
			GeneralArgs.runMultiThread = true;
			GeneralArgs.numOfThreads = experimentData.getNumOfThreads();
		}

		String algorithmName = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING ? "Lifted Top Matching"
				: "Top Matching";
		logger.info(String.format("Runnning experiment with %s algorithm for scenrio-id %d, number of threads %d",
				algorithmName, experimentData.getScenrioId(), experimentData.getNumOfThreads()));

		logger.info("Running inference");
		Stats stats = runInference(experimentData, algorithmType);

		logger.info(String.format("Adding a new Stats row to the stats file of %s algorithm, query-id %d",
				algorithmName, experimentData.getQueryId()));
		addStatsRowToQueryFile(experimentData, stats, algorithmType);

	}

	private static Stats runInference(ExperimentData experimentData, AlgorithmType algorithmType) {
		String algorithmName = algorithmType == AlgorithmType.TOP_MATCHING ? "Top Matching" : "Lifted Top Matching";

		Stats stats = new Stats(experimentData.getDistributions().get(0).getModel().getModal(), 5,
				experimentData.getGraph().toString());
		stats.setSectionDescription(String.format("Inference over scenrio-id %d, query-id %d",
				experimentData.getScenrioId(), experimentData.getQueryId()));

		stats.setAlgorithm(algorithmName);

		stats.setStartTimeDate(new Date());

		Graph graph = experimentData.getGraph();
		ArrayList<Distribution> distributions = experimentData.getDistributions();

		stats.setStartTimeDate(new Date());

		stats.setPhiToProbability(algorithmType == AlgorithmType.TOP_MATCHING
				? new TopMatchingAlgorithm(graph, distributions).calculateProbability()
				: new LiftedTopMatchingAlgorithm(graph, distributions).calculateProbability());

		stats.setEndTimeDate(new Date());

		System.out.println(stats);

		return stats;
	}

	private static void updateDoneExperiment(ArrayList<ExperimentData> experiments, int scenarioId, int numOfThreads) {
		for (ExperimentData experimentData : experiments) {
			if (experimentData.getScenrioId() == scenarioId && experimentData.getNumOfThreads() == numOfThreads) {
				experimentData.setDone(true);
				return;
			}
		}
	}

	private static void updateExperimentsData(AlgorithmType algorithmType) {
		ArrayList<ExperimentData> currentExperimentsData = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING
				? experimentsDataLTM : experimentsDataTM;

		HashMap<Integer, Integer> currentBaseRunTimes = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING
				? baseRunTimesLTM : baseRunTimesTM;

		String currentBaseOutputFolder = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING
				? String.format("%s/%s/", OUTPUT_FOLDER_PATH, "LTM")
				: String.format("%s/%s/", OUTPUT_FOLDER_PATH, "TM");

		for (int queryId : queryIds) {
			String filePath = String.format("%s/q%d.csv", currentBaseOutputFolder, queryId);
			ArrayList<String> lines = readOutputFile(filePath);
			// scenario_id,num_of_threads,bucket,query_id,cartesian_product,probability,running_time,improvement_factor

			for (String line : lines) {
				String[] components = line.split(",");
				int scenrioId = Integer.parseInt(components[0]);
				int numOfThreads = Integer.parseInt(components[1]);
				int runningTime = Integer.parseInt(components[6]);
				updateDoneExperiment(currentExperimentsData, scenrioId, numOfThreads);
				if (numOfThreads == 1) {
					currentBaseRunTimes.put(scenrioId, runningTime);
				}
			}
		}
	}

	private static void runExperiments(AlgorithmType algorithmType) {
		ArrayList<ExperimentData> experiments = algorithmType == AlgorithmType.LIFTED_TOP_MATCHING ? experimentsDataLTM
				: experimentsDataTM;

		int totalNum = experiments.size();
		int doneNum = 0;
		for (ExperimentData experimentData : experiments) {
			if (experimentData.isDone()) {
				doneNum++;
			}
		}

		for (int i = 0; i < experiments.size(); i++) {
			double perc = (double) doneNum / (double) totalNum;
			ExperimentData currentExperiment = experiments.get(i);
			if (!currentExperiment.isDone()) {
				logger.info(String.format("Running experiment %d out of %d (%f %%)", doneNum++, totalNum, perc));
				runExperiment(currentExperiment, algorithmType);
				currentExperiment.setDone(true);
			}
		}
	}

	public static void main(String[] args) {
		GeneralArgs.earlyPrunningOptimization = true;
		GeneralArgs.sharedModalOptimization = true;
		GeneralArgs.verbose = true;

		logger.info("Starting invoking the experiment");

		logger.info("Reading input file to grab experiments data");
		experimentsDataLTM = getExperimentData(AlgorithmType.LIFTED_TOP_MATCHING);
		experimentsDataTM = getExperimentData(AlgorithmType.TOP_MATCHING);

		logger.info("Reading exisiting output files to mark done experiments");
		updateExperimentsData(AlgorithmType.LIFTED_TOP_MATCHING);
		updateExperimentsData(AlgorithmType.TOP_MATCHING);

		logger.info("Running experiment for Lifted Top Matching");
		runExperiments(AlgorithmType.LIFTED_TOP_MATCHING);

		logger.info("Running experiment for Top Matching");
		runExperiments(AlgorithmType.TOP_MATCHING);

		logger.info("Done invoking the experiment!!!");
	}

}
