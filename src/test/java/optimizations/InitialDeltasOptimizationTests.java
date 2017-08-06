package optimizations;

import common.TestUtils;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run a complete test to compare bruteforce vs topmatching with enhanced
 * initial deltas generator
 */
public class InitialDeltasOptimizationTests extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public InitialDeltasOptimizationTests(String testName) {
		super(testName);
		GeneralArgs.enhancedInitialDeltas = true;
		GeneralArgs.enhancedDeltasContainer = false;
		GeneralArgs.omitRedundantItems = false;
		GeneralArgs.verbose = false;
		GeneralArgs.printFlow = false;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(InitialDeltasOptimizationTests.class);
	}

	public void testBruteforceVsTopMatching1() {
		assertTrue(TestUtils.runTest(1, 4));
	}

	public void testBruteforceVsTopMatching2() {
		assertTrue(TestUtils.runTest(2, 5));
	}

	public void testBruteforceVsTopMatching3() {
		assertTrue(TestUtils.runTest(3, 8));
	}

	public void testBruteforceVsTopMatching4() {
		assertTrue(TestUtils.runTest(4, 3));
	}

	public void testBruteforceVsTopMatching5() {
		assertTrue(TestUtils.runTest(5, 6));
	}

	public void testBruteforceVsTopMatching6() {
		assertTrue(TestUtils.runTest(6, 5));
	}

	public void testBruteforceVsTopMatching7() {
		assertTrue(TestUtils.runTest(7, 5));
	}

	public void testBruteforceVsTopMatching8() {
		assertTrue(TestUtils.runTest(8, 4));
	}

	public void testBruteforceVsTopMatching9() {
		assertTrue(TestUtils.runTest(9, 6));
	}

	public void testBruteforceVsTopMatching10() {
		// assertTrue(TestUtils.runTest(10, 10));
	}

	public void testBruteforceVsTopMatching11() {
		// assertTrue(TestUtils.runTest(11, 9));
	}

	public void testBruteforceVsTopMatching12() {
		// assertTrue(TestUtils.runTest(12, 11));
	}

	public void testBruteforceVsTopMatching13() {
		assertTrue(TestUtils.runTest(13, 6));
	}

	public void testBruteforceVsTopMatching14() {
		assertTrue(TestUtils.runTest(14, 7));
	}
}
