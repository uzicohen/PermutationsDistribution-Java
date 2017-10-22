package liftedtopmatching;

import java.util.Random;

import common.RandomGraphGenerator;
import common.TestUtils;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pattern.Graph;

/**
 * Run a complete test to compare topmatching vs liftedtopmatching
 */
public class LiftedTopMatchingRandomTests extends TestCase {

	private static int numOfTests = 10;

	private static int maxNumOfItems = 15;

	private static int maxNumOfLabels = 4;

	private static Random rand = new Random(1113);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LiftedTopMatchingRandomTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		GeneralArgs.commonPrefixOptimization = false;
		return new TestSuite(LiftedTopMatchingRandomTests.class);
	}

	public void testBruteforceVsLiftedTopMatching() {
		for (int i = 0; i < numOfTests; i++) {
			int numOfItems = rand.nextInt(maxNumOfItems) + 1;
			int numOfLabels = rand.nextInt(maxNumOfLabels) + 1;
			Graph graph = RandomGraphGenerator.GetRandomGraph(numOfItems, numOfLabels, rand);
			assertTrue(TestUtils.runLiftedTopMatchingRandomTest(graph, numOfItems, numOfLabels, i + 1, true, 4));
		}

		for (int i = 0; i < TestUtils.summaries.size(); i++) {
			System.out.println(String.format("i = %d, %s", i + 1, TestUtils.summaries.get(i)));
		}

	}
}
