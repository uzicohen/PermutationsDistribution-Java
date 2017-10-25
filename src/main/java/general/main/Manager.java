package general.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import bruteforce.BruteforceAlgorithm;
import bruteforce.ExplicitDistribution;
import experiments.GraphGenerator;
import general.Distribution;
import general.Mallows;
import general.main.GeneralArgs.GraphGeneratorParameters;
import liftedtopmatching.LiftedTopMatchingAlgorithm;
import pattern.Graph;
import sampled.SampledAlgorithm;
import sampled.SampledDistribution;
import stats.Stats;
import topmatching.SimpleDistribution;
import topmatching.TopMatchingAlgorithm;

/**
 * 
 * A class to manage the entire running flow. Reads the configuration file and
 * determine what to run and how.
 * 
 * @author uzicohen
 *
 */
public class Manager {

	private static final String BRUTE_FORCE = "Brute force";

	private static final String SAMPLED = "Sampled";

	private static final String TOP_MATCHNING = "Top Matching";

	private static final String LIFTED_TOP_MATCHNING = "Lifted Top Matching";

	private static final Logger logger = Logger.getLogger(Manager.class.getName());

	public static void run() {

		logger.info("Resolving running arguments");

		ArrayList<Double> phiArray = GeneralArgs.phiArray;

		ArrayList<GraphGeneratorParameters> scenariosSettings = GeneralArgs.graphGeneratorParameters;
		for (GraphGeneratorParameters scenarioSettings : scenariosSettings) {

			logger.info(String.format(
					"Creating objects for graph-generator case %s, number of items: %d, number of labels: %d",
					scenarioSettings.graphGeneratorCase, scenarioSettings.numOfItems, scenarioSettings.numOfLabels));

			ArrayList<Mallows> models = new ArrayList<>();
			for (double phi : phiArray) {
				models.add(new Mallows(phi));
			}

			Graph graph = GraphGenerator.GetGraph(Integer.parseInt(scenarioSettings.graphGeneratorCase), models,
					scenarioSettings.numOfItems);

			Stats stats = new Stats(scenarioSettings, graph.toString(), models.get(0).getModal());

			// Run brute force
			if (GeneralArgs.runBruteforce) {
				stats.setAlgorithm(BRUTE_FORCE);

				ArrayList<Distribution> distributions = new ArrayList<>();
				models.forEach(model -> distributions.add(new ExplicitDistribution(model)));

				if (GeneralArgs.printDistribution) {
					System.out.println(distributions);
				}

				stats.setStartTimeDate(new Date());

				logger.info(String.format("Running brute-force algorithm for graph-generator case %s",
						scenarioSettings.graphGeneratorCase));

				HashMap<Double, Double> exactProbs = new BruteforceAlgorithm(graph, distributions)
						.calculateProbability();

				stats.setEndTimeDate(new Date());

				stats.setPhiToProbability(exactProbs);

				System.out.println(stats);

			}

			if (GeneralArgs.runSampled) {
				stats.setAlgorithm(SAMPLED);

				stats.setStartTimeDate(new Date());

				logger.info(String.format("Running sampled algorithm for graph-generator case %s",
						scenarioSettings.graphGeneratorCase));

				ArrayList<Distribution> distributions = new ArrayList<>();
				models.forEach(model -> distributions.add(new SampledDistribution(model, GeneralArgs.numSamples)));
				HashMap<Double, Double> approxProbs = new SampledAlgorithm(graph, distributions).calculateProbability();

				stats.setEndTimeDate(new Date());

				stats.setPhiToProbability(approxProbs);

				System.out.println(stats);
			}

			if (GeneralArgs.runTopMatching) {
				stats.setAlgorithm(TOP_MATCHNING);

				ArrayList<Distribution> distributions = new ArrayList<>();
				models.forEach(model -> distributions.add(new SimpleDistribution(model)));

				stats.setStartTimeDate(new Date());

				logger.info(String.format("Running top-matchnig algorithm for graph-generator case %s",
						scenarioSettings.graphGeneratorCase));

				HashMap<Double, Double> topMatchingProbs = new TopMatchingAlgorithm(graph, distributions)
						.calculateProbability();

				stats.setEndTimeDate(new Date());

				stats.setPhiToProbability(topMatchingProbs);

				System.out.println(stats);

			}

			if (GeneralArgs.runLiftedTopMatching) {
				stats.setAlgorithm(LIFTED_TOP_MATCHNING);

				ArrayList<Distribution> distributions = new ArrayList<>();
				models.forEach(model -> distributions.add(new SimpleDistribution(model)));
				stats.setStartTimeDate(new Date());

				logger.info(String.format("Running lifted-top-matchnig algorithm for graph-generator case %s",
						scenarioSettings.graphGeneratorCase));

				HashMap<Double, Double> liftedTopMatchingProbs = new LiftedTopMatchingAlgorithm(graph, distributions)
						.calculateProbability();

				stats.setEndTimeDate(new Date());

				stats.setPhiToProbability(liftedTopMatchingProbs);

				System.out.println(stats);

			}
		}

	}

}
