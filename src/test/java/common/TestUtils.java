package common;

import bruteforce.BruteforceAlgorithm;
import bruteforce.ExplicitDistribution;
import general.Distribution;
import general.GeneralUtils;
import general.Mallows;
import pattern.Graph;
import pattern.GraphGenerator;
import topmatching.SimpleDistribution;
import topmatching.TopMatchingAlgorithm;

public class TestUtils {
	
	private static double Epsilon = 10e-6;

	public static boolean runTest(int graphId, int numItems) {
		Graph graph = GraphGenerator.GetGraph(graphId);

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		Distribution explicitDistribution = new ExplicitDistribution(model);

		double exactProb = new BruteforceAlgorithm().calculateProbability(graph, explicitDistribution);

		Distribution simpleDistribution = new SimpleDistribution(model);

		double topMatchingProb = new TopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		return Math.abs(exactProb - topMatchingProb) < Epsilon;
	}

}
