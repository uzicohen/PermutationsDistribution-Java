package liftedtopmatching;

import common.TestUtils;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run a complete test to compare bruteforce vs topmatching with enhanced deltes container
 */
public class LiftedTopMatchingTests extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LiftedTopMatchingTests(String testName) {
		super(testName);
		GeneralArgs.enhancedInitialDeltas = true;
		GeneralArgs.enhancedDeltasContainer = true;
		GeneralArgs.omitRedundantItems = false;
		GeneralArgs.verbose = false;
		GeneralArgs.printFlow = false;
		GeneralArgs.currentAlgorithm = AlgorithmType.LIFTED_TOP_MATCHING;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LiftedTopMatchingTests.class);
	}

	public void testBruteforceVsTopMatching1() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(1, 4));
	}

	public void testBruteforceVsTopMatching2() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(2, 5));
	}

	public void testBruteforceVsTopMatching3() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(3, 8));
	}

	public void testBruteforceVsTopMatching4() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(4, 3));
	}

	public void testBruteforceVsTopMatching5() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(5, 6));
	}

	public void testBruteforceVsTopMatching6() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(6, 5));
	}

	public void testBruteforceVsTopMatching7() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(7, 5));
	}

	public void testBruteforceVsTopMatching8() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(8, 4));
	}

	public void testBruteforceVsTopMatching9() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(9, 6));
	}

	public void testBruteforceVsTopMatching10() {
		// assertTrue(TestUtils.runLiftedTopMatchingTest(10, 10));
	}

	public void testBruteforceVsTopMatching11() {
//		 assertTrue(TestUtils.runLiftedTopMatchingTest(11, 9));
	}

	public void testBruteforceVsTopMatching12() {
		// assertTrue(TestUtils.runLiftedTopMatchingTest(12, 11));
	}

	public void testBruteforceVsTopMatching13() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(13, 6));
	}
	
	public void testBruteforceVsTopMatching14() {
//		assertTrue(TestUtils.runLiftedTopMatchingTest(14, 7));
	}
	
	public void testBruteforceVsTopMatching15() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(15, 5));
	}
	
	public void testBruteforceVsTopMatching17() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(17, 4));
	}
	
	public void testBruteforceVsTopMatching18() {
		assertTrue(TestUtils.runLiftedTopMatchingTest(18, 4));
	}
}
