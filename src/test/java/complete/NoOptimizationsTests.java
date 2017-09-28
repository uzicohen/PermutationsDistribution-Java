package complete;

import common.TestUtils;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run a complete test to compare bruteforce vs topmatching w/o optimizations
 */
public class NoOptimizationsTests extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public NoOptimizationsTests(String testName) {
		super(testName);
		GeneralArgs.enhancedInitialDeltas = false;
		GeneralArgs.enhancedDeltasContainer = false;
		GeneralArgs.verbose = false;
		GeneralArgs.printFlow = false;
		GeneralArgs.currentAlgorithm = AlgorithmType.TOP_MATCHNING;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(NoOptimizationsTests.class);
	}

	public void testScenrio1() {
		assertTrue(TestUtils.runTest(1, 4, false, 0));
	}

	public void testScenrio2() {
		assertTrue(TestUtils.runTest(2, 5, false, 0));
	}

	public void testScenrio3() {
		assertTrue(TestUtils.runTest(3, 8, false, 0));
	}

	public void testScenrio4() {
		assertTrue(TestUtils.runTest(4, 3, false, 0));
	}

	public void testScenrio5() {
		assertTrue(TestUtils.runTest(5, 6, false, 0));
	}

	public void testScenrio6() {
		assertTrue(TestUtils.runTest(6, 5, false, 0));
	}

	public void testScenrio7() {
		assertTrue(TestUtils.runTest(7, 5, false, 0));
	}

	public void testScenrio8() {
		assertTrue(TestUtils.runTest(8, 4, false, 0));
	}

	public void testScenrio9() {
		assertTrue(TestUtils.runTest(9, 6, false, 0));
	}

	public void testScenrio10() {
		// assertTrue(TestUtils.runTest(10, 10, false, 0));
	}

	public void testScenrio11() {
		// assertTrue(TestUtils.runTest(11, 9, false, 0));
	}

	public void testScenrio12() {
		// assertTrue(TestUtils.runTest(12, 11, false, 0));
	}

	public void testScenrio13() {
		assertTrue(TestUtils.runTest(13, 6, false, 0));
	}

	public void testScenrio14() {
		assertTrue(TestUtils.runTest(14, 7, false, 0));
	}

	public void testScenrio15() {
		assertTrue(TestUtils.runTest(15, 5, false, 0));
	}
}
