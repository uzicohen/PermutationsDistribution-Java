package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Delta {

	private HashMap<String, Integer> labelToIndex;

	private double probability;

	public Delta(ArrayList<String> order, HashMap<String, String> gamma) {
		
		this.labelToIndex = new HashMap<>();

		// j and seen are used to avoid duplicates (["x"] = "s1", ["y"] = "s3",
		// ["z"] = "s4", ["w"] = "s4" and possibleOrder = xyzw will become
		// s1,s3,s4 and not s1,s3,s4,s4)
		int j = 0;
		HashSet<String> seen = new HashSet<>();

		for (int i = 0; i < order.size(); i++) {
			if (!seen.contains(gamma.get(order.get(i)))) {
				j++;
				seen.add(gamma.get(order.get(i)));
			}
			this.labelToIndex.put(order.get(i), j);
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
