package optimizations;

import bruteforce.BruteforceAlgorithm;
import bruteforce.ExplicitDistribution;
import general.Distribution;
import general.GeneralUtils;
import general.Mallows;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pattern.Graph;
import pattern.GraphGenerator;
import topmatching.SimpleDistribution;
import topmatching.TopMatchingAlgorithm;

/**
 * Run a complete test to compare bruteforce vs topmatching
 */
public class DeltasContainerOptimizationTests extends TestCase {

	private static double Epsilon = 10e-6;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public DeltasContainerOptimizationTests(String testName) {
		super(testName);
		GeneralArgs.enhancedInitialDeltas = true;
		GeneralArgs.enhancedDeltasContainer = true;
		GeneralArgs.omitRedundantItems = false;
		GeneralArgs.verbose = false;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(DeltasContainerOptimizationTests.class);
	}

	private boolean runTest(int graphId, int numItems) {
		Graph graph = GraphGenerator.GetGraph(graphId);

		Mallows model = new Mallows(GeneralUtils.getItems(numItems), 0.3);

		Distribution explicitDistribution = new ExplicitDistribution(model);

		double exactProb = new BruteforceAlgorithm().calculateProbability(graph, explicitDistribution);

		Distribution simpleDistribution = new SimpleDistribution(model);

		double topMatchingProb = new TopMatchingAlgorithm().calculateProbability(graph, simpleDistribution);

		return Math.abs(exactProb - topMatchingProb) < Epsilon;
	}

	/**
	 * 
	 * P = 0.990541370714296
	 * 
	 */
	public void testBruteforceVsTopMatching1() {
		assertTrue(runTest(1, 4));
	}

	/**
	 * 
	 * P = 0.989000393257776
	 * 
	 */
	public void testBruteforceVsTopMatching2() {
		assertTrue(runTest(2, 5));
	}

	/**
	 * 
	 * P = 0.732986262312703
	 * 
	 */
	public void testBruteforceVsTopMatching3() {
		assertTrue(runTest(3, 8));
	}

	/**
	 * 
	 * P = 0.719424460431655
	 * 
	 */
	public void testBruteforceVsTopMatching4() {
		assertTrue(runTest(4, 3));
	}

	/**
	 * 
	 * P = 0.761006046617952
	 * 
	 */
	public void testBruteforceVsTopMatching5() {
		assertTrue(runTest(5, 6));
	}

	/**
	 * 
	 * P = 0.934812879701345
	 * 
	 */
	public void testBruteforceVsTopMatching6() {
		assertTrue(runTest(6, 5));
	}

	/**
	 * 
	 * P = 0.9498882307361549
	 * 
	 * 
	 */
	public void testBruteforceVsTopMatching7() {
		assertTrue(runTest(7, 5));
	}

	/**
	 * 
	 * P = 1.0
	 * 
	 */
	public void testBruteforceVsTopMatching8() {
		assertTrue(runTest(8, 4));
	}

	/**
	 * 
	 * P = 0.6937277499487445
	 * 
	 */
	public void testBruteforceVsTopMatching9() {
		assertTrue(runTest(9, 6));
	}

	/**
	 * 
	 * P = 0.503103
	 * 
	 */
	public void testBruteforceVsTopMatching10() {
		// assertTrue(runTest(10, 10));
	}

	/**
	 * 
	 * P = 0.9741246606894051
	 * 
	 * 
	 */
	public void testBruteforceVsTopMatching11() {
		// assertTrue(runTest(11, 9));
	}

	/**
	 * 
	 * P = 0.567747
	 * 
	 * 
	 */
	public void testBruteforceVsTopMatching12() {
		// assertTrue(runTest(12, 11));
	}
}
