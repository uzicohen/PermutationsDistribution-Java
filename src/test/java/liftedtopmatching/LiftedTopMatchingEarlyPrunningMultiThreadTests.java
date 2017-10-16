package liftedtopmatching;

import common.TestUtils;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run a complete test to compare bruteforce vs liftedtopmatching container
 */
public class LiftedTopMatchingEarlyPrunningMultiThreadTests extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LiftedTopMatchingEarlyPrunningMultiThreadTests(String testName) {
		super(testName);
		GeneralArgs.verbose = false;
		GeneralArgs.printFlow = false;
		// in case early pruning optimization is turned off, it acts as the
		// regular algorithm
		GeneralArgs.earlyPrunningOptimization = false;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LiftedTopMatchingEarlyPrunningMultiThreadTests.class);
	}

	public void testScenrio1() {
		assertTrue(TestUtils.runTest(1, 4, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio2() {
		assertTrue(TestUtils.runTest(2, 5, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio3() {
		assertTrue(TestUtils.runTest(3, 8, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio4() {
		assertTrue(TestUtils.runTest(4, 3, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio5() {
		assertTrue(TestUtils.runTest(5, 6, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio6() {
		assertTrue(TestUtils.runTest(6, 5, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio7() {
		assertTrue(TestUtils.runTest(7, 5, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio8() {
		assertTrue(TestUtils.runTest(8, 4, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio9() {
		assertTrue(TestUtils.runTest(9, 6, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio10() {
//		assertTrue(TestUtils.runTest(10, 10, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio11() {
//		assertTrue(TestUtils.runTest(11, 9, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio12() {
//		assertTrue(TestUtils.runTest(12, 11, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio13() {
		assertTrue(TestUtils.runTest(13, 6, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio14() {
		assertTrue(TestUtils.runTest(14, 7, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio15() {
		assertTrue(TestUtils.runTest(15, 5, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio16() {
		assertTrue(TestUtils.runTest(16, 15, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio17() {
		assertTrue(TestUtils.runTest(17, 4, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}

	public void testScenrio18() {
		assertTrue(TestUtils.runTest(18, 4, true, 4, AlgorithmType.LIFTED_TOP_MATCHING));
	}
}
