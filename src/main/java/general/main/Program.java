package general.main;
import bruteforce.BruteforceAlgorithm;
import bruteforce.ExplicitDistribution;
import general.Distribution;
import general.GeneralUtils;
import general.Mallows;
import pattern.Graph;
import pattern.GraphGenerator;

public class Program {

	private static int ExperimentNumber = 1;

	private static double Epsilon = 10e-6;

	private static void RunExperiment(int graphId, int numItems, boolean verbose) {
		System.out.printf("=================Experiment %d:=================\n", ExperimentNumber);

		Graph graph = GraphGenerator.GetGraph(graphId);

		System.out.printf("Graph:\n%s", graph);

		System.out.printf("Number of items: %d\n", numItems);

		Mallows model = new Mallows(GeneralUtils.GetItems(numItems), 0.3);

		Distribution explicitDistribution = new ExplicitDistribution(model);

		if (verbose) {
			System.out.println(explicitDistribution);
		}

		double exactProb = new BruteforceAlgorithm().CalculateProbability(graph, explicitDistribution);

		System.out.println(exactProb);
		
//		Distribution simpleDistribution = new SimpleDistribution(model);
//
//		double topMatchingProb = new TopMatchingAlgorithm().CalculateProbability(graph, simpleDistribution);
//
//		if (Math.abs(exactProb - topMatchingProb) < Epsilon) {
//			System.out.printf(
//					"\n########################\n#####Test passed!!!#####\n########################\nProbability: {0}\n",
//					topMatchingProb);
//		} else {
//			System.out.printf(
//					"\n????????????????????????????????????????????????????????\nTest failed!!!\nExact: {0}, TopMatching: {1}\n????????????????????????????????????????????????????????\n",
//					exactProb, topMatchingProb);
//		}
//
//		int numOfSamples = 10000;
//
//		Distribution sampledDistribution = new SampledDistribution(model, numOfSamples);
//
//		System.out.printf("Approx probability ({0} samples): {1}\n", numOfSamples,
//				new SampledAlgorithm().CalculateProbability(graph, sampledDistribution));
//
		System.out.printf("=================End of Experiment %d=================", ExperimentNumber++);
	}

	public static void main(String[] args) {
		// P = 0.990541370714296
		RunExperiment(1, 4, true);

		// TODO: Fix this case
		// P = 0.989000393257776
		// RunExperiment(2, 5, false);

		// P = 0.732986262312703
//		RunExperiment(3, 8, false);

		// P = 0.719424460431655
//		RunExperiment(4, 3, false);

		// TODO: Fix this case
		// P = 0.761006046617952
		// RunExperiment(5, 6);

		// P = 0.934812879701345
//		RunExperiment(6, 5, false);

	}

}
