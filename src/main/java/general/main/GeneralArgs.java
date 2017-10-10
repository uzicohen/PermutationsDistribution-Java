package general.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GeneralArgs {

	public static class GraphGeneratorParameters {
		public String graphGeneratorCase;
		public int numOfItems;
		public int numOfLabels;

		public GraphGeneratorParameters(int graphGeneratorCase, int numOfItems, int numOfLabels) {
			super();
			this.graphGeneratorCase = String.valueOf(graphGeneratorCase);
			this.numOfItems = numOfItems;
			this.numOfLabels = numOfLabels;
		}

	}

	private static Properties properties;

	public static boolean verbose;

	public static boolean printDistribution;

	public static int numSamplesForPrint;

	public static int numAssignmentsForPrint;

	public static boolean printFlow;

	public static ArrayList<GraphGeneratorParameters> graphGeneratorParameters;

	public static boolean runAll;

	public static boolean runBruteforce;

	public static boolean runSampled;

	public static boolean runTopMatching;

	public static boolean runLiftedTopMatching;

	public static double phi;

	public static int numSamples;

	public static AlgorithmType currentAlgorithm;

	public static boolean runMultiThread;

	public static int numOfThreads;

	public static boolean sharedModalOptimization;

	static {

		properties = new Properties();

		try {
			properties.load(new FileInputStream(new File("src/main/java/resources/conf.properties")));

			verbose = Boolean.parseBoolean(properties.getProperty("verbose"));

			printDistribution = Boolean.parseBoolean(properties.getProperty("print_distribution"));

			numSamplesForPrint = Integer.parseInt(properties.getProperty("num_samples_for_print"));

			numAssignmentsForPrint = Integer.parseInt(properties.getProperty("num_assignments_for_print"));

			printFlow = Boolean.parseBoolean(properties.getProperty("print_flow"));

			graphGeneratorParameters = new ArrayList<>();

			String graphGeneratorSingleCases = properties.getProperty("graph_generator_cases").toString();

			for (String graphGeneratorSingleCase : graphGeneratorSingleCases.split(",")) {
				int graphGeneratorCase = Integer.parseInt(graphGeneratorSingleCase.split(";")[0].split("\\(")[1]);
				int numOfItems = Integer.parseInt(graphGeneratorSingleCase.split(";")[1]);
				int numOfLabels = Integer.parseInt(graphGeneratorSingleCase.split(";")[2].split("\\)")[0]);
				graphGeneratorParameters.add(new GraphGeneratorParameters(graphGeneratorCase, numOfItems, numOfLabels));
			}

			runAll = Boolean.parseBoolean(properties.getProperty("run_all"));

			runBruteforce = Boolean.parseBoolean(properties.getProperty("run_bruteforce")) || runAll;

			runSampled = Boolean.parseBoolean(properties.getProperty("run_sampled")) || runAll;

			runTopMatching = Boolean.parseBoolean(properties.getProperty("run_topmatching")) || runAll;

			runLiftedTopMatching = Boolean.parseBoolean(properties.getProperty("run_liftedtopmatching")) || runAll;

			phi = Double.parseDouble(properties.getProperty("phi"));

			numSamples = Integer.parseInt(properties.getProperty("num_samples"));

			runMultiThread = Boolean.parseBoolean(properties.getProperty("run_multithread"));

			numOfThreads = Integer.parseInt(properties.getProperty("num_of_threads"));

			sharedModalOptimization = Boolean.parseBoolean(properties.getProperty("shared_modal_optimization"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
