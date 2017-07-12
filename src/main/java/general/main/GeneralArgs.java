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

	public static ArrayList<ScenarioToNumOfItemsPair> scenarioToNumOfItemsPairs;

	public static boolean runAll;

	public static boolean runBruteforce;

	public static boolean runSampled;

	public static boolean runTopMatching;

	public static double phi;

	public static int numSamples;

	public static boolean enhancedInitialDeltas;

	public static boolean enhancedDeltasContainer;

	public static boolean omitRedundantItems;

	static {
		properties = new Properties();

		try {
			properties.load(new FileInputStream(new File("src/main/java/resources/conf.properties")));

			verbose = Boolean.parseBoolean(properties.getProperty("verbose"));

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

			phi = Double.parseDouble(properties.getProperty("phi"));

			numSamples = Integer.parseInt(properties.getProperty("num_samples"));

			enhancedDeltasContainer = Boolean.parseBoolean(properties.getProperty("enhanced_initial_deltas"));

			enhancedInitialDeltas = Boolean.parseBoolean(properties.getProperty("enhanced_initial_deltas"));

			omitRedundantItems = Boolean.parseBoolean(properties.getProperty("omit_redundant_items"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
