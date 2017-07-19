package topmatching.delta;

import java.util.ArrayList;
import java.util.HashMap;

public class Delta {

	private HashMap<String, Integer> labelToIndex;

	private double probability;

	private String strForHash;

	public Delta(ArrayList<String> order, HashMap<String, String> gamma) {

		// We build the labelToIndex to keep track of previously added sigmas
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
		createStrForHash();
	}

	public Delta(Delta other) {
		this.labelToIndex = new HashMap<>(other.labelToIndex);
		this.probability = other.probability;
		createStrForHash();
	}

	public Delta() {
		this.labelToIndex = new HashMap<>();
		this.probability = 1.0;
		createStrForHash();
	}

	@Override
	public String toString() {
		return String.format("Mapping: %s, Prob: %f", this.labelToIndex, this.probability);
	}

	@Override
	public boolean equals(Object obj) {
		Delta delta1 = (Delta) obj;
		return this.strForHash.equals(delta1.strForHash);
	}

	@Override
	public int hashCode() {
		return this.strForHash.hashCode();
	}

	public void createStrForHash() {
		if (this.labelToIndex.isEmpty()) {
			this.strForHash = "";
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String key : this.labelToIndex.keySet()) {
			sb.append(key);
			sb.append(this.labelToIndex.get(key));
		}
		this.strForHash = sb.toString();
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

	public void setStrForHash(String strForHash) {
		this.strForHash = strForHash;
	}

	public String getStrForHash() {
		return strForHash;
	}
}
