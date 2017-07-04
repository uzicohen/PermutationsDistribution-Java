package topmatching;

import java.util.ArrayList;
import java.util.HashMap;

public class Delta {

	private HashMap<String, Integer> labelToIndex;

	private double probability;

	public Delta(ArrayList<String> order, HashMap<String, String> gamma) {

		this.labelToIndex = new HashMap<>();

		int j = 0;
		HashMap<String, Integer> sigmaToIndexMap = new HashMap<>();
		for (int i = 0; i < order.size(); i++) {
			String label = order.get(i);
			String sigma = gamma.get(label);
			if (!sigmaToIndexMap.containsKey(sigma)) {
				sigmaToIndexMap.put(sigma, ++j);
			}
			this.labelToIndex.put(label, sigmaToIndexMap.get(sigma));
		}
		this.probability = 1.0;
	}

	public Delta(Delta other) {
		this.labelToIndex = new HashMap<>(other.labelToIndex);
		this.probability = other.probability;
	}

	public Delta() {
	}

	@Override
	public String toString() {
		return String.format("Mapping: %s, Prob: %f", this.labelToIndex, this.probability);
	}

	@Override
	public boolean equals(Object obj) {
		Delta delta1 = (Delta) obj;
		if (delta1 == this) {
			return true;
		}
		if (delta1.labelToIndex.size() != this.labelToIndex.size()) {
			return false;
		}
		for (String key : delta1.labelToIndex.keySet()) {
			if (!this.labelToIndex.containsKey(key) || (delta1.labelToIndex.get(key) != this.labelToIndex.get(key))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	public HashMap<String, Integer> getLabelToIndex() {
		return labelToIndex;
	}

	public void setLabelToIndex(HashMap<String, Integer> labelToIndex) {
		this.labelToIndex = labelToIndex;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}
