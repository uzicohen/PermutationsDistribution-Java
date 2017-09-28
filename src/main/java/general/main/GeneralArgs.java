package general.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GeneralArgs {

	public static class ScenarioSettings {
		public String scenario;
		public int numOfItems;
		public int numOfLabels;
		public boolean runMultiThread;
		public int numOfThreads;

		public ScenarioSettings(int scenario, int numOfItems, int numOfLabels, boolean runMultiThread,
				int numOfThreads) {
			super();
			this.scenario = String.valueOf(scenario);
			this.numOfItems = numOfItems;
			this.numOfLabels = numOfLabels;
			this.runMultiThread = runMultiThread;
			this.numOfThreads = this.runMultiThread ? numOfThreads : 0;
		}

	}

	private static Properties properties;

	public static boolean verbose;

	public static boolean printDistribution;

	public static int numSamplesForPrint;

	public static int numAssignmentsForPrint;

	public static boolean printFlow;

	public static ArrayList<ScenarioSettings> senariosSettings;

	public static boolean runAll;

	public static boolean runBruteforce;

	public static boolean runSampled;

	public static boolean runTopMatching;

	public static boolean runBinaryMatching;

	public static boolean runLiftedTopMatching;

	public static double phi;

	public static int numSamples;

	public static AlgorithmType currentAlgorithm;

	public static boolean enhancedInitialDeltas;

	public static boolean enhancedDeltasContainer;

	public static boolean runMultiThread;

	public static int numOfThreads;

	static {
		properties = new Properties();

		try {
			properties.load(new FileInputStream(new File("src/main/java/resources/conf.properties")));

			verbose = Boolean.parseBoolean(properties.getProperty("verbose"));

			printDistribution = Boolean.parseBoolean(properties.getProperty("print_distribution"));

			numSamplesForPrint = Integer.parseInt(properties.getProperty("num_samples_for_print"));

			numAssignmentsForPrint = Integer.parseInt(properties.getProperty("num_assignments_for_print"));

			printFlow = Boolean.parseBoolean(properties.getProperty("print_flow"));

			senariosSettings = new ArrayList<>();

			String experimentScenariosStr = properties.getProperty("scenarios_settings").toString();

			for (String setting : experimentScenariosStr.split(",")) {
				int scenario = Integer.parseInt(setting.split(";")[0].split("\\(")[1]);
				int numOfItems = Integer.parseInt(setting.split(";")[1]);
				int numOfLabels = Integer.parseInt(setting.split(";")[2]);
				boolean isMultiThread = Boolean.parseBoolean(setting.split(";")[3]);
				int numOfThreads = Integer.parseInt(setting.split(";")[4].split("\\)")[0]);
				senariosSettings
						.add(new ScenarioSettings(scenario, numOfItems, numOfLabels, isMultiThread, numOfThreads));
			}

			runAll = Boolean.parseBoolean(properties.getProperty("run_all"));

			runBruteforce = Boolean.parseBoolean(properties.getProperty("run_bruteforce")) || runAll;

			runSampled = Boolean.parseBoolean(properties.getProperty("run_sampled")) || runAll;

			runTopMatching = Boolean.parseBoolean(properties.getProperty("run_topmatching")) || runAll;

			runBinaryMatching = Boolean.parseBoolean(properties.getProperty("run_binarymatching")) || runAll;

			runLiftedTopMatching = Boolean.parseBoolean(properties.getProperty("run_liftedtopmatching")) || runAll;

			phi = Double.parseDouble(properties.getProperty("phi"));

			numSamples = Integer.parseInt(properties.getProperty("num_samples"));

			enhancedDeltasContainer = Boolean.parseBoolean(properties.getProperty("enhanced_deltas_container"));

			enhancedInitialDeltas = Boolean.parseBoolean(properties.getProperty("enhanced_initial_deltas"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
