package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import general.main.GeneralArgs;
import general.main.PrintFlow;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.EnhancedDeltasContainer;
import topmatching.delta.SimpleDeltasContainer;

public class TopProb {

	private TopProbArgs topProbArgs;

	private TopMatchingArgs topMatchingArgs;

	public TopProb(HashMap<String, String> gamma, TopMatchingArgs topMatchingArgs) {
		this.topProbArgs = new TopProbArgs(gamma);
		this.topMatchingArgs = topMatchingArgs;
		initTopProbArgs();
	}

	private void initTopProbArgs() {
		TopProbUtils.init(this.topMatchingArgs, this.topProbArgs);
	}

	public double Calculate() {
		
		PrintFlow.printGamma(this.topProbArgs.getGamma());
		
		// Initialize R0
		DeltasContainer r = topMatchingArgs.getDeltasContainerGenerator().getInitialDeltas(topProbArgs);
		
		PrintFlow.printDeltasContainer(r);

		for (int i = 0; i < this.topMatchingArgs.getRim().getModel().getModal().size(); i++) {			
			
			DeltasContainer newR = GeneralArgs.enhancedDeltasContainer ? new EnhancedDeltasContainer(topMatchingArgs)
					: new SimpleDeltasContainer(topMatchingArgs);
			String sigma = this.topMatchingArgs.getRim().getModel().getModal().get(i);

			PrintFlow.printItem(sigma);

			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				
				PrintFlow.printDelta(delta);
				
				ArrayList<Integer> range = range(delta, sigma);
				
				PrintFlow.printRange(range);
				
				for (int j : range) {
					Delta deltaTag = new Delta(delta);

					if (!this.topProbArgs.getImgGamma().contains(sigma)) {
						// Create delta+j
						for (String key : delta.getLabelToIndex().keySet()) {
							if (deltaTag.getLabelToIndex().get(key) >= j) {
								int currentIndex = deltaTag.getLabelToIndex().get(key);
								deltaTag.getLabelToIndex().put(key, currentIndex + 1);
							}
						}
					}
					// else - the old delta stays

					PrintFlow.printJAndNewDelta(j, deltaTag, topProbArgs.getImgGamma().contains(sigma));
					
					// Update the new delta's hash
					deltaTag.createStrForHash();
					
					// Calculate the insertion probability
					double insertionProb = TopProbUtils.getInsertionProb(deltaTag, sigma, j);

					deltaTag.setProbability(deltaTag.getProbability() * insertionProb);

					// Search in newR the constructed delta
					Delta exisitingDelta = newR.getDelta(deltaTag);

					if (exisitingDelta != null) {
						exisitingDelta.setProbability(exisitingDelta.getProbability() + deltaTag.getProbability());
					} else {
						// insert the new delta into the new R
						newR.addDelta(deltaTag);
					}
				}
			}
			r = newR;
		}
		
		PrintFlow.printDeltasContainer(r);

		double probability = 0.0;
		Iterator<Delta> iter = r.iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			probability += delta.getProbability();
		}
		
		PrintFlow.printSeparator();
		
		return probability;
	}

	private ArrayList<Integer> range(Delta delta, String sigma) {
		ArrayList<Integer> result = new ArrayList<>();
		if (this.topProbArgs.getImgGamma().contains(sigma)) {
			String label = this.topProbArgs.getSigmaToGammaValueMap().get(sigma).iterator().next();
			result.add(delta.getLabelToIndex().get(label));
		} else {
			// Get the i from "s{i}"
			int i = Integer.parseInt(sigma.split("s")[1]);

			HashSet<String> itemsLargerThanI = TopProbUtils.getItemsLargerThanI(i);
			int s = i + itemsLargerThanI.size();
			HashSet<Integer> illegalIndices = topMatchingArgs.getLambda().containsKey(sigma)
					? TopProbUtils.getIllegalIndices(delta, sigma) : new HashSet<>();
			for (int j = 1; j <= s; j++) {
				if (!illegalIndices.contains(j)) {
					result.add(j);
				}
			}
		}
		return result;
	}

}
