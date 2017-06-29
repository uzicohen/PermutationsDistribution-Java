package topmatching;

import java.util.ArrayList;

public class DeltasContainer {
	/**
	 * 
	 * This is implemented as a class and not as a List<> so we can optimize it
	 * in the future
	 * 
	 */

	private ArrayList<Delta> deltas;

	public DeltasContainer() {
		this.deltas = new ArrayList<>();
	}

	public Delta getDelta(Delta inputDelta) {
		for (Delta delta : this.deltas) {
			if (inputDelta.equals(delta)) {
				return delta;
			}
		}
		return null;
	}

	public void addDelta(Delta delta) {
		this.deltas.add(delta);
	}

	public ArrayList<Delta> getDeltas() {
		return deltas;
	}

	public void setDeltas(ArrayList<Delta> deltas) {
		this.deltas = deltas;
	}

}
