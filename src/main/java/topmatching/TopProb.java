package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
		// Initialize R0
		DeltasContainer r = TopMatchingUtils.getInitialDeltas(this.topProbArgs);

		List<Delta> verifiedDeltas = r.getDeltas().stream().filter(d -> TopProbUtils.isDeltaConsistent(d))
				.collect(Collectors.toList());

		r.setDeltas(new ArrayList<>(verifiedDeltas));

		for (int i = 0; i < this.topMatchingArgs.getRim().getModel().getModal().size(); i++) {
			DeltasContainer newR = new DeltasContainer();
			String sigma = this.topMatchingArgs.getRim().getModel().getModal().get(i);
			for (Delta delta : r.getDeltas()) {
				ArrayList<Integer> range = range(delta, sigma);
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

					StringBuilder sb = new StringBuilder();

					// Calculate the insertion probability
					double insertionProb = TopProbUtils.getInsertionProb(deltaTag, sigma, j, sb);
					
					// For debug purposes
					String ins = sb.toString();
					
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

		double probability = 0.0;
		for (Delta delta : r.getDeltas()) {
			probability += delta.getProbability();
		}

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
			HashSet<Integer> illegalIndices = TopProbUtils.getIllegalIndices(delta, sigma);
			for (int j = 1; j <= s; j++) {
				if (!illegalIndices.contains(j)) {
					result.add(j);
				}
			}
		}
		return result;
	}

}
