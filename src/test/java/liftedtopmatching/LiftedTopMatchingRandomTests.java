package liftedtopmatching;

import java.util.Random;

import common.RandomGraphGenerator;
import common.TestUtils;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pattern.Graph;

/**
 * Run a complete test to compare topmatching vs liftedtopmatching
 */
public class LiftedTopMatchingRandomTests extends TestCase {

	private static int numOfTests = 100;

	private static int maxNumOfItems = 12;

	private static int maxNumOfLabels = 8;

	private static Random rand = new Random(1113);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LiftedTopMatchingRandomTests(String testName) {
		super(testName);
		GeneralArgs.currentAlgorithm = AlgorithmType.LIFTED_TOP_MATCHING;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LiftedTopMatchingRandomTests.class);
	}

	public void testBruteforceVsLiftedTopMatching() {
		// GeneralArgs.numAssignmentsForPrint = 300;
		// GeneralArgs.verbose = true;
		for (int i = 0; i < numOfTests; i++) {
			int numOfItems = rand.nextInt(maxNumOfItems) + 1;
			int numOfLabels = rand.nextInt(maxNumOfLabels) + 1;
			Graph graph = RandomGraphGenerator.GetRandomGraph(numOfItems, numOfLabels, rand);
			assertTrue(TestUtils.runLiftedTopMatchingRandomTest(graph, numOfItems, numOfLabels, i + 1));
		}

		for (int i = 0; i < TestUtils.summaries.size(); i++) {
			System.out.println(String.format("i = %d, %s", i + 1, TestUtils.summaries.get(i)));
		}

	}
}
