package topmatching.delta;

import java.util.ArrayList;
import java.util.Iterator;

import topmatching.TopMatchingArgs;

public class SimpleDeltasContainer extends DeltasContainer {

	public static class SimpleDeltasContainerIterator implements Iterator<Delta> {

		private int index;

		private ArrayList<Delta> deltas;

		/**
		 * 
		 * @param n
		 *            the size of the container
		 */
		public SimpleDeltasContainerIterator(ArrayList<Delta> deltas) {
			this.index = 0;
			this.deltas = deltas;
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

	private ArrayList<Delta> deltas;

	public SimpleDeltasContainer(TopMatchingArgs topMatchingArgs) {
		super(topMatchingArgs);
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

	@Override
	public Iterator<Delta> iterator() {
		return new SimpleDeltasContainerIterator(this.deltas);
	}

}
