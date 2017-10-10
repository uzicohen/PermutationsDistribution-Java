package general;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralUtils {

	public static HashMap<Double, ArrayList<ArrayList<Double>>> getPhiToInsertionProbabilities(
			ArrayList<Distribution> distributions) {
		HashMap<Double, ArrayList<ArrayList<Double>>> result = new HashMap<>();
		for (int i = 0; i < distributions.size(); i++) {
			Distribution current = distributions.get(i);
			result.put(current.getModel().getPhi(), getInsertionProbabilities(current.getModel()));
		}
		return result;
	}

	private static ArrayList<ArrayList<Double>> getInsertionProbabilities(Mallows model) {
		ArrayList<ArrayList<Double>> result = new ArrayList<>();
		for (int i = 0; i < model.getModal().size(); i++) {
			ArrayList<Double> current = new ArrayList<>();
			result.add(current);
			double denum = 0.0;
			for (int k = 0; k <= i; k++) {
				denum += Math.pow(model.getPhi(), k);
			}

			for (int j = 0; j <= i; j++) {
				current.add(Math.pow(model.getPhi(), i - j) / denum);
			}
		}
		return result;
	}

	public static ArrayList<String> getItems(int n) {
		ArrayList<String> items = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			items.add("s" + (i + 1));
		}
		return items;
	}

	public static int kendallTau(ArrayList<String> sigma, ArrayList<String> tau) {
		HashMap<String, Integer> tauMap = new HashMap<>();
		for (int i = 0; i < tau.size(); i++) {
			tauMap.put(tau.get(i), i);
		}
		int result = 0;
		for (int i = 0; i < tau.size(); i++) {
			int sigmaPos = tauMap.get(sigma.get(i));
			for (int j = i + 1; j < tau.size(); j++) {
				result += tauMap.get(sigma.get(j)) < sigmaPos ? 1 : 0;
			}
		}
		return result;
	}

	private static void generatePermutationsAux(ArrayList<String> current, ArrayList<String> referenceList,
			ArrayList<ArrayList<String>> result) {
		if (referenceList.isEmpty()) {
			result.add(current);
			return;
		}
		for (int i = 0; i < referenceList.size(); i++) {
			ArrayList<String> newCurrent = new ArrayList<>(current);
			newCurrent.add(referenceList.get(i));
			ArrayList<String> newReference = new ArrayList<>(referenceList);
			newReference.remove(i);
			generatePermutationsAux(newCurrent, newReference, result);
		}
	}

	public static ArrayList<ArrayList<String>> generatePermutations(ArrayList<String> referenceList) {
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		generatePermutationsAux(new ArrayList<>(), referenceList, result);
		return result;
	}
}
