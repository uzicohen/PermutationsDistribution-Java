package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

		// Initialize R0
		DeltasContainer r = new DeltasContainerGenerator(this.topMatchingArgs).getInitialDeltas(topProbArgs);

		ArrayList<String> modal = this.topMatchingArgs.getDistributions().get(0).getModel().getModal();
		for (int i = 0; i < modal.size(); i++) {
			r = this.topProbUtils.getNewR(modal, r, i);
		}

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
		
		return result;
	}
}
