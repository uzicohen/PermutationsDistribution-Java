package general.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GeneralArgs {

	public static class ScenarioToNumOfItemsPair {
		public int scenario;
		public int numOfItems;

		public ScenarioToNumOfItemsPair(int scenario, int numOfItems) {
			super();
			this.scenario = scenario;
			this.numOfItems = numOfItems;
		}

	}

	private static Properties properties;

	public static boolean verbose;
	
	public static boolean printDistribution;

	public static int numSamplesForPrint;
	
	public static int numAssignmentsForPrint;

	public static boolean printFlow;

	public static ArrayList<ScenarioToNumOfItemsPair> scenarioToNumOfItemsPairs;

	public static boolean runAll;

	public static boolean runBruteforce;

	public static boolean runSampled;

	public static boolean runTopMatching;
	
	public static boolean runBinaryMatching;

	public static double phi;

	public static int numSamples;
	
	public static AlgorithmType currentAlgorithm;

	public static boolean enhancedInitialDeltas;

	public static boolean enhancedDeltasContainer;

	public static boolean omitRedundantItems;
	
	public static boolean deltasReuse;

	static {
		properties = new Properties();

		try {
			properties.load(new FileInputStream(new File("src/main/java/resources/conf.properties")));

			verbose = Boolean.parseBoolean(properties.getProperty("verbose"));
			
			printDistribution = Boolean.parseBoolean(properties.getProperty("print_distribution"));

			numSamplesForPrint = Integer.parseInt(properties.getProperty("num_samples_for_print"));
			
			numAssignmentsForPrint = Integer.parseInt(properties.getProperty("num_assignments_for_print"));

			printFlow = Boolean.parseBoolean(properties.getProperty("print_flow"));
			
			scenarioToNumOfItemsPairs = new ArrayList<>();

			String experimentScenariosStr = properties.getProperty("scenario_tuples");

			for (String tuple : experimentScenariosStr.split(",")) {
				int scenario = Integer.parseInt(tuple.split(";")[0].split("\\(")[1]);
				int numOfItems = Integer.parseInt(tuple.split(";")[1].split("\\)")[0]);
				scenarioToNumOfItemsPairs.add(new ScenarioToNumOfItemsPair(scenario, numOfItems));
			}

			runAll = Boolean.parseBoolean(properties.getProperty("run_all"));

			runBruteforce = Boolean.parseBoolean(properties.getProperty("run_bruteforce")) || runAll;

			runSampled = Boolean.parseBoolean(properties.getProperty("run_sampled")) || runAll;

			runTopMatching = Boolean.parseBoolean(properties.getProperty("run_topmatching")) || runAll;
			
			runBinaryMatching = Boolean.parseBoolean(properties.getProperty("run_binarymatching")) || runAll;

			phi = Double.parseDouble(properties.getProperty("phi"));

			numSamples = Integer.parseInt(properties.getProperty("num_samples"));

			enhancedDeltasContainer = Boolean.parseBoolean(properties.getProperty("enhanced_deltas_container"));

			enhancedInitialDeltas = Boolean.parseBoolean(properties.getProperty("enhanced_initial_deltas"));

			omitRedundantItems = Boolean.parseBoolean(properties.getProperty("omit_redundant_items"));
			
			deltasReuse = Boolean.parseBoolean(properties.getProperty("deltas_reuse"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
