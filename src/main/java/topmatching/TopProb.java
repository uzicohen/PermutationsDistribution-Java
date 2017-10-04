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

	public double calculate() {

		PrintFlow.printGamma(this.topProbArgs.getGamma());

		// Initialize R0
		DeltasContainer r = new DeltasContainerGenerator(this.topMatchingArgs).getInitialDeltas(topProbArgs);

		PrintFlow.printDeltasContainer("Initial deltas", r, topProbArgs.getGamma());

		for (int i = 0; i < this.topMatchingArgs.getRim().getModel().getModal().size(); i++) {

			DeltasContainer newR = new DeltasContainer(topMatchingArgs);
			String sigma = this.topMatchingArgs.getRim().getModel().getModal().get(i);

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

					// Calculate the insertion probability
					double insertionProb = this.topProbUtils.getInsertionProb(deltaTag, sigma, j);

					deltaTag.setProbability(deltaTag.getProbability() * insertionProb);

					// Search in newR the constructed delta
					Delta exisitingDelta = newR.getDelta(deltaTag);

					if (exisitingDelta != null) {
						exisitingDelta.setProbability(exisitingDelta.getProbability() + deltaTag.getProbability());
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

		double probability = 0.0;
		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			probability += delta.getProbability();
		}

		PrintFlow.printProbability(probability);

		PrintFlow.printSeparator();

		return probability;
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
