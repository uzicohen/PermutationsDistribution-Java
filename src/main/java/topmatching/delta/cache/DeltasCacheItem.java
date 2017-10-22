package topmatching.delta.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;

public class DeltasCacheItem {

	private int graphId;

	private ArrayList<String> ranking;

	private HashMap<Integer, DeltasContainer> itemToDeltasContainer;

	public DeltasCacheItem(int graphId, ArrayList<String> ranking) {
		super();
		this.graphId = graphId;
		this.ranking = ranking;
		this.itemToDeltasContainer = new HashMap<>();
	}

	@Override
	public boolean equals(Object obj) {
		DeltasCacheItem other = (DeltasCacheItem) obj;
		return this.graphId == other.graphId && this.ranking.toString().equals(other.ranking.toString());
	}

	@Override
	public int hashCode() {
		return String.format("%d-%s", this.graphId, this.ranking.toString()).hashCode();
	}

	public void storeNewDeltasContainer(int itemNumber, DeltasContainer dc) {
		DeltasContainer current = this.itemToDeltasContainer.get(itemNumber);
		if (current == null) {
			current = dc;
		} else {
			Iterator<Delta> iter = dc.iterator();
			while (iter.hasNext()) {
				current.addDelta(iter.next());
			}
		}
		this.itemToDeltasContainer.put(itemNumber, current);
	}

	public DeltasContainer getDeltasContainer(int itemNum) {
		return this.itemToDeltasContainer.get(itemNum);
	}

	public int getGraphId() {
		return graphId;
	}

	public ArrayList<String> getRanking() {
		return ranking;
	}

	public HashMap<Integer, DeltasContainer> getItemToDeltasContainer() {
		return itemToDeltasContainer;
	}

	public void setGraphId(int graphId) {
		this.graphId = graphId;
	}

	public void setRanking(ArrayList<String> ranking) {
		this.ranking = ranking;
	}

	public void setItemToDeltasContainer(HashMap<Integer, DeltasContainer> itemToDeltasContainer) {
		this.itemToDeltasContainer = itemToDeltasContainer;
	}

}
