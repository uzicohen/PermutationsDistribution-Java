package topmatching.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import topmatching.TopMatchingArgs;

public class EnhancedDeltasContainer extends DeltasContainer {

	public class EnhancedDeltasContainerIterator implements Iterator<Delta> {

		private int index;

		private ArrayList<Delta> deltas;

		/**
		 * 
		 * @param n
		 *            the size of the container
		 */
		public EnhancedDeltasContainerIterator(HashMap<String, Delta> deltasMap) {
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

	private HashMap<String, Delta> deltas;

	public EnhancedDeltasContainer(TopMatchingArgs topMatchingArgs) {
		super(topMatchingArgs);
		this.deltas = new HashMap<>();
	}

	public Delta getDelta(Delta inputDelta) {
		return this.deltas.get(inputDelta.getStrForHash());
	}

	public void addDelta(Delta delta) {
		this.deltas.put(delta.getStrForHash(), delta);
	}

	@Override
	public Iterator<Delta> iterator() {
		return new EnhancedDeltasContainerIterator(this.deltas);
	}
	
	@Override
	public int getNumOfDeltas() {
		return this.deltas.size();
	}

}
