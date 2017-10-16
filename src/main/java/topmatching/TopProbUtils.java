package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import general.main.PrintFlow;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;

public class TopProbUtils {

	private static TopMatchingArgs topMatchingArgs;

	private TopProbArgs topProbArgs;

	public void init(TopMatchingArgs topMatchingArgsInput, TopProbArgs topProbArgsInput) {
		topMatchingArgs = topMatchingArgsInput;
		topProbArgs = topProbArgsInput;
	}

	public HashMap<Double, Double> getInsertionProbs(Delta delta, String sigma, int j) {
		// Get the i from "s{i}"
		int i = Integer.parseInt(sigma.split("s")[1]);

		HashSet<String> imgLargerThanI = getItemsLargerThanI(i);
		int numOfElementsSmallerThanJ = 0;

		for (String item : imgLargerThanI) {
			String label = topProbArgs.getSigmaToGammaValueMap().get(item).iterator().next();
			if (delta.getLabelPosition(label) < j) {
				numOfElementsSmallerThanJ++;
			}
		}
		int jTag = j - numOfElementsSmallerThanJ;

		HashMap<Double, Double> result = new HashMap<>();
		HashSet<Double> setOfPhi = new HashSet<>();
		topMatchingArgs.getDistributions().forEach(dist -> setOfPhi.add(dist.getModel().getPhi()));
		for (double phi : setOfPhi) {
			result.put(phi, topMatchingArgs.getPhiToInsertionProbs().get(phi).get(i - 1).get(jTag - 1));
		}
		return result;

	}

	public HashSet<String> getItemsLargerThanI(int i) {
		HashSet<String> result = new HashSet<>();
		for (String sigma : topProbArgs.getImgGamma()) {
			int j = Integer.parseInt(sigma.split("s")[1]);
			if (j > i) {
				result.add(sigma);
			}
		}
		return result;
	}

	public HashSet<Integer> getIllegalIndices(Delta delta, String sigma) {
		HashSet<Integer> result = new HashSet<>();
		HashSet<String> currentSigmaLambda = topMatchingArgs.getLambda().get(sigma);

		for (String label : currentSigmaLambda) {
			int labelDelta = delta.getLabelPosition(label);
			int maxParentDelta = 0;
			HashSet<String> parents = topMatchingArgs.getLabelToParentsMap().containsKey(label)
					? topMatchingArgs.getLabelToParentsMap().get(label) : new HashSet<>();
			for (String parentLable : parents) {
				maxParentDelta = Math.max(maxParentDelta, delta.getLabelPosition(parentLable));
			}
			maxParentDelta++;

			for (; maxParentDelta <= labelDelta; maxParentDelta++) {
				result.add(maxParentDelta);
			}
		}

		return result;
	}

	public DeltasContainer getNewR(ArrayList<String> modal, DeltasContainer r, int i) {
		DeltasContainer newR = new DeltasContainer();
		String sigma = modal.get(i);

		PrintFlow.printItem(sigma, topProbArgs.getImgGamma().contains(sigma));

		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();

			PrintFlow.printDelta(true, "Interim delta", delta, topProbArgs.getGamma());

			ArrayList<Integer> range = range(delta, sigma);

			PrintFlow.printRange(range);

			for (int j : range) {
				Delta deltaTag = new Delta(delta);

				if (!this.topProbArgs.getImgGamma().contains(sigma)) {
					// Create delta+j
					deltaTag.insertNewItem(j);
				}
				// else - the old delta stays

				// Update the probability
				HashMap<Double, Double> phiToInsertionProb = getInsertionProbs(deltaTag, sigma, j);
				for (double phi : phiToInsertionProb.keySet()) {
					deltaTag.setProbabilityOfPhi(phi, deltaTag.getProbabilityOfPhi(phi) * phiToInsertionProb.get(phi));
				}

				// Search in newR the constructed delta
				Delta exisitingDelta = newR.getDelta(deltaTag);

				if (exisitingDelta != null) {
					for (double phi : phiToInsertionProb.keySet()) {
						exisitingDelta.setProbabilityOfPhi(phi,
								exisitingDelta.getProbabilityOfPhi(phi) + deltaTag.getProbabilityOfPhi(phi));
					}
					PrintFlow.printJAndNewDelta(j, exisitingDelta, topProbArgs.getGamma());
				} else {
					// insert the new delta into the new R
					newR.addDelta(deltaTag);
					PrintFlow.printJAndNewDelta(j, deltaTag, topProbArgs.getGamma());
				}
			}
		}
		return newR;
	}

	private ArrayList<Integer> range(Delta delta, String sigma) {
		ArrayList<Integer> result = new ArrayList<>();
		if (this.topProbArgs.getImgGamma().contains(sigma)) {
			String label = this.topProbArgs.getSigmaToGammaValueMap().get(sigma).iterator().next();
			result.add(delta.getLabelPosition(label));
		} else {
			// Get the i from "s{i}"
			int i = Integer.parseInt(sigma.split("s")[1]);

			HashSet<String> itemsLargerThanI = getItemsLargerThanI(i);
			int s = i + itemsLargerThanI.size();
			HashSet<Integer> illegalIndices = topMatchingArgs.getLambda().containsKey(sigma)
					? getIllegalIndices(delta, sigma) : new HashSet<>();
			for (int j = 1; j <= s; j++) {
				if (!illegalIndices.contains(j)) {
					result.add(j);
				}
			}
		}
		return result;
	}

}
