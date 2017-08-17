package binarymatching;

import common.TestUtils;
import general.main.AlgorithmType;
import general.main.GeneralArgs;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run a complete test to compare bruteforce vs topmatching with enhanced deltes container
 */
public class BinaryMatchingTests extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public BinaryMatchingTests(String testName) {
		super(testName);
		GeneralArgs.enhancedInitialDeltas = true;
		GeneralArgs.enhancedDeltasContainer = true;
		GeneralArgs.omitRedundantItems = false;
		GeneralArgs.verbose = false;
		GeneralArgs.printFlow = false;
		GeneralArgs.currentAlgorithm = AlgorithmType.BINARY_MATCHING;
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(BinaryMatchingTests.class);
	}

	public void testBruteforceVsTopMatching1() {
		assertTrue(TestUtils.runBinaryMatchingTest(1, 4));
	}

	public void testBruteforceVsTopMatching2() {
		assertTrue(TestUtils.runBinaryMatchingTest(2, 5));
	}

	public void testBruteforceVsTopMatching3() {
		assertTrue(TestUtils.runBinaryMatchingTest(3, 8));
	}

	public void testBruteforceVsTopMatching4() {
		assertTrue(TestUtils.runBinaryMatchingTest(4, 3));
	}

	public void testBruteforceVsTopMatching5() {
		assertTrue(TestUtils.runBinaryMatchingTest(5, 6));
	}

	public void testBruteforceVsTopMatching6() {
		assertTrue(TestUtils.runBinaryMatchingTest(6, 5));
	}

	public void testBruteforceVsTopMatching7() {
		assertTrue(TestUtils.runBinaryMatchingTest(7, 5));
	}

	public void testBruteforceVsTopMatching8() {
		assertTrue(TestUtils.runBinaryMatchingTest(8, 4));
	}

	public void testBruteforceVsTopMatching9() {
		assertTrue(TestUtils.runBinaryMatchingTest(9, 6));
	}

	public void testBruteforceVsTopMatching10() {
		// assertTrue(TestUtils.runBinaryMatchingTest(10, 10));
	}

	public void testBruteforceVsTopMatching11() {
//		 assertTrue(TestUtils.runBinaryMatchingTest(11, 9));
	}

	public void testBruteforceVsTopMatching12() {
		// assertTrue(TestUtils.runBinaryMatchingTest(12, 11));
	}

	public void testBruteforceVsTopMatching13() {
		assertTrue(TestUtils.runBinaryMatchingTest(13, 6));
	}
	
	public void testBruteforceVsTopMatching14() {
		assertTrue(TestUtils.runBinaryMatchingTest(14, 7));
	}
	
	public void testBruteforceVsTopMatching15() {
		assertTrue(TestUtils.runBinaryMatchingTest(15, 5));
	}
}
