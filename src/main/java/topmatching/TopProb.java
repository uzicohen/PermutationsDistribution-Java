package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import general.main.PrintFlow;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.DeltasContainerGenerator;

public class TopProb {

	private TopProbArgs topProbArgs;

	private TopMatchingArgs topMatchingArgs;

	private TopProbUtils topProbUtils;

	public TopProb(HashMap<String, String> gamma, TopMatchingArgs topMatchingArgs) {
		this.topProbArgs = new TopProbArgs(gamma);
		this.topMatchingArgs = topMatchingArgs;
		initTopProbArgs();
	}

	private void initTopProbArgs() {
		this.topProbUtils = new TopProbUtils();
		this.topProbUtils.init(this.topMatchingArgs, this.topProbArgs);
	}

	public HashMap<Double, Double> calculate() {

		PrintFlow.printGamma(this.topProbArgs.getGamma());

		// Initialize R0
		DeltasContainer r = new DeltasContainerGenerator(this.topMatchingArgs).getInitialDeltas(topProbArgs);

		PrintFlow.printDeltasContainer("Initial deltas", r, topProbArgs.getGamma());

		ArrayList<String> modal = this.topMatchingArgs.getDistributions().get(0).getModel().getModal();
		for (int i = 0; i < modal.size(); i++) {

			DeltasContainer newR = new DeltasContainer(topMatchingArgs);
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
					HashMap<Double, Double> phiToInsertionProb = this.topProbUtils.getInsertionProbs(deltaTag, sigma,
							j);
					for (double phi : phiToInsertionProb.keySet()) {
						deltaTag.setProbabilityOfPhi(phi,
								deltaTag.getProbabilityOfPhi(phi) * phiToInsertionProb.get(phi));
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
			r = newR;
		}

		PrintFlow.printDeltasContainer("Final deltas", r, topProbArgs.getGamma());

		HashMap<Double, Double> result = new HashMap<>();

		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			for (double phi : delta.getPhiToProbability().keySet()) {
				double prob = delta.getProbabilityOfPhi(phi);
				Double currentProb = result.get(phi);
				if (currentProb == null) {
					currentProb = 0.0;
				}
				result.put(phi, currentProb + prob);
			}
		}
		PrintFlow.printSeparator();

		return result;
	}

	private ArrayList<Integer> range(Delta delta, String sigma) {
		ArrayList<Integer> result = new ArrayList<>();
		if (this.topProbArgs.getImgGamma().contains(sigma)) {
			String label = this.topProbArgs.getSigmaToGammaValueMap().get(sigma).iterator().next();
			result.add(delta.getLabelPosition(label));
		} else {
			// Get the i from "s{i}"
			int i = Integer.parseInt(sigma.split("s")[1]);

			HashSet<String> itemsLargerThanI = this.topProbUtils.getItemsLargerThanI(i);
			int s = i + itemsLargerThanI.size();
			HashSet<Integer> illegalIndices = topMatchingArgs.getLambda().containsKey(sigma)
					? this.topProbUtils.getIllegalIndices(delta, sigma) : new HashSet<>();
			for (int j = 1; j <= s; j++) {
				if (!illegalIndices.contains(j)) {
					result.add(j);
				}
			}
		}
		return result;
	}

}
