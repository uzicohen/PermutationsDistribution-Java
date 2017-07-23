package general.main;

import java.util.ArrayList;
import java.util.HashMap;

import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;

/**
 * 
 * This class' purpose is to print a complete flow of the algorithm for learning
 * purposes. Will be extended to print to file as needed
 * 
 * @author uzicohen
 *
 */
public class PrintFlow {

	private static StringBuilder getIndentation(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append(" ");
		}
		return sb;
	}

	public static void printGamma(HashMap<String, String> gamma) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(0);
			sb.append("Gamma: ");
			sb.append(gamma);
			System.out.println(sb);
		}
	}

	public static void printDeltasContainer(DeltasContainer deltasContainer) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(2);
			sb.append("Deltas: ");
			sb.append(deltasContainer);
			System.out.println(sb);
		}
	}

	public static void printItem(String item) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(4);
			sb.append("Current item: ");
			sb.append(item);
			System.out.println(sb);
		}
	}

	public static void printDelta(Delta delta) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(6);
			sb.append("Delta: ");
			sb.append(delta);
			System.out.println(sb);
		}
	}

	public static void printRange(ArrayList<Integer> range) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(8);
			sb.append("Range: ");
			sb.append(range);
			System.out.println(sb);
		}
	}

	public static void printJAndNewDelta(int j, Delta delta, boolean belongsToImg) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(10);
			sb.append("J: ");
			sb.append(j);
			sb.append(", New Delta: ");
			sb.append(delta);
			if (belongsToImg) {
				sb.append(" (In Img)");
			}
			System.out.println(sb);
		}
	}

	public static void printSeparator() {
		if (GeneralArgs.printFlow) {
			System.out.println("__________________________________________");
			System.out.println("__________________________________________");
			System.out.println("__________________________________________");
		}
	}

}
