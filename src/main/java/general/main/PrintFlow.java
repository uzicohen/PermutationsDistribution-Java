package general.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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

	public static void printDeltasContainer(String name, DeltasContainer deltasContainer,
			HashMap<String, String> gamma) {
		if (GeneralArgs.printFlow) {
			Iterator<Delta> iter = deltasContainer.iterator();
			StringBuilder sb = getIndentation(2);
			sb.append(name);
			sb.append(": ");
			if (!iter.hasNext()) {
				sb.append("[]");
			}
			System.out.println(sb);
			while (iter.hasNext()) {
				Delta delta = iter.next();
				printDelta(true, name.equals("Initial deltas") ? "Init delta" : "Final delta", delta, gamma);
			}
		}
	}

	public static void printItem(String item, boolean inImg) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(4);
			sb.append("Current item: ");
			sb.append(item);
			if (inImg) {
				sb.append(" (in image)");
			}
			System.out.println(sb);
		}
	}

	// Gamma: {x=s1, y=s1, z=s6, t=s5, w=s3}
	// Delta: {t=4, w=3, x=1, y=1, z=2}
	// (s1,s6,s3,s5)
	private static String getDeltaForPrint(Delta delta, HashMap<String, String> gamma) {
		StringBuilder sb = new StringBuilder(" (");
		ArrayList<Integer> indices = new ArrayList<>(delta.getLabelToIndex().values());
		Collections.sort(indices);
		HashSet<String> seen = new HashSet<>();
		for (int i = 0; i < indices.size(); i++) {
			for (String key : delta.getLabelToIndex().keySet()) {
				if (delta.getLabelPosition(key) == indices.get(i) && !seen.contains(gamma.get(key))) {
					seen.add(gamma.get(key));
					sb.append(gamma.get(key));
					sb.append(",");
				}
			}
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append(") ");
		return sb.toString();
	}

	public static void printDelta(boolean withIndent, String name, Delta delta, HashMap<String, String> gamma) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(withIndent ? 6 : 0);
			sb.append(name + ": ");
			sb.append(getDeltaForPrint(delta, gamma));
			sb.append(",");
			sb.append(delta.getLabelToIndex());
			sb.append(",");
			sb.append(delta.getProbability());
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

	public static void printJAndNewDelta(int j, Delta delta, HashMap<String, String> gamma) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(10);
			sb.append("J: ");
			sb.append(j);
			sb.append(", ");
			System.out.print(sb);
			printDelta(false, "New Delta", delta, gamma);
		}
	}

	public static void printProbability(double probability) {
		if (GeneralArgs.printFlow) {
			StringBuilder sb = getIndentation(2);
			sb.append("Probability: ");
			sb.append(probability);
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
