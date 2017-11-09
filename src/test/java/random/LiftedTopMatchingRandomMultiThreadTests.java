package random;

import java.util.Random;

import common.RandomGraphGenerator;
import common.TestUtils;
import general.main.GeneralArgs;
import graph.Graph;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run a complete test to compare topmatching vs liftedtopmatching
 */
public class LiftedTopMatchingRandomMultiThreadTests extends TestCase {

	private static int numOfTests = 10;

	private static int maxNumOfItems = 20;

	private static int maxNumOfLabels = 4;

	private static Random rand = new Random(1113);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LiftedTopMatchingRandomMultiThreadTests(String testName) {
		super(testName);
		GeneralArgs.runMultiThread = true;
		GeneralArgs.numOfThreads = 4;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LiftedTopMatchingRandomMultiThreadTests.class);
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
