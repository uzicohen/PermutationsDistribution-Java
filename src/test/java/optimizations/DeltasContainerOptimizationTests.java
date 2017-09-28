//package optimizations;
//
//import common.TestUtils;
//import general.main.AlgorithmType;
//import general.main.GeneralArgs;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
///**
// * Run a complete test to compare bruteforce vs topmatching with enhanced deltes
// * container
// */
//public class DeltasContainerOptimizationTests extends TestCase {
//
//	/**
//	 * Create the test case
//	 *
//	 * @param testName
//	 *            name of the test case
//	 */
//	public DeltasContainerOptimizationTests(String testName) {
//		super(testName);
//		GeneralArgs.enhancedInitialDeltas = true;
//		GeneralArgs.enhancedDeltasContainer = true;
//		GeneralArgs.verbose = false;
//		GeneralArgs.printFlow = false;
//		GeneralArgs.currentAlgorithm = AlgorithmType.TOP_MATCHNING;
//	}
//
//	/**
//	 * @return the suite of tests being tested
//	 */
//	public static Test suite() {
//		return new TestSuite(DeltasContainerOptimizationTests.class);
//	}
//
//	public void testScenrio1() {
//		assertTrue(TestUtils.runTest(1, 4));
//	}
//
//	public void testScenrio2() {
//		assertTrue(TestUtils.runTest(2, 5));
//	}
//
//	public void testScenrio3() {
//		assertTrue(TestUtils.runTest(3, 8));
//	}
//
//	public void testScenrio4() {
//		assertTrue(TestUtils.runTest(4, 3));
//	}
//
//	public void testScenrio5() {
//		assertTrue(TestUtils.runTest(5, 6));
//	}
//
//	public void testScenrio6() {
//		assertTrue(TestUtils.runTest(6, 5));
//	}
//
//	public void testScenrio7() {
//		assertTrue(TestUtils.runTest(7, 5));
//	}
//
//	public void testScenrio8() {
//		assertTrue(TestUtils.runTest(8, 4));
//	}
//
//	public void testScenrio9() {
//		assertTrue(TestUtils.runTest(9, 6));
//	}
//
//	public void testScenrio10() {
//		assertTrue(TestUtils.runTest(10, 10));
//	}
//
//	public void testScenrio11() {
//		assertTrue(TestUtils.runTest(11, 9));
//	}
//
//	public void testScenrio12() {
//		assertTrue(TestUtils.runTest(12, 11));
//	}
//
//	public void testScenrio13() {
//		assertTrue(TestUtils.runTest(13, 6));
//	}
//
//	public void testScenrio14() {
//		assertTrue(TestUtils.runTest(14, 7));
//	}
//
//	public void testScenrio15() {
//		assertTrue(TestUtils.runTest(15, 5));
//	}
//
//	public void testScenrio16() {
//		assertTrue(TestUtils.runTest(16, 15));
//	}
//
//	public void testScenrio17() {
//		assertTrue(TestUtils.runTest(17, 4));
//	}
//
//	public void testScenrio18() {
//		assertTrue(TestUtils.runTest(18, 4));
//	}
//}
