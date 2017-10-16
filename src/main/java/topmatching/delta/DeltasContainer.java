package topmatching.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DeltasContainer {

	public class DeltasContainerIterator implements Iterator<Delta> {

		private int index;

		private ArrayList<Delta> deltas;

		/**
		 * 
		 * @param n
		 *            the size of the container
		 */
		public DeltasContainerIterator(HashMap<String, Delta> deltasMap) {
			this.index = 0;
			this.deltas = new ArrayList<>();
			for (Delta delta : deltasMap.values()) {
				this.deltas.add(delta);
			}
		}

		@Override
		public boolean hasNext() {
			return index < this.deltas.size();
		}

		@Override
		public Delta next() {
			int currentIndex = this.index;
			this.index++;
			return this.deltas.get(currentIndex);
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Delta> iter = iterator();
		while (iter.hasNext()) {
			Delta delta = iter.next();
			sb.append(delta);
			sb.append("\n");
		}
		return sb.toString();
	}

	private HashMap<String, Delta> deltas;

	public DeltasContainer() {
		this.deltas = new HashMap<>();
	}

	public Delta getDelta(Delta inputDelta) {
		return this.deltas.get(inputDelta.getStrForHash());
	}

	public void addDelta(Delta delta) {
		this.deltas.put(delta.getStrForHash(), delta);
	}

	public Iterator<Delta> iterator() {
		return new DeltasContainerIterator(this.deltas);
	}

	public int getNumOfDeltas() {
		return this.deltas.size();
	}

}
