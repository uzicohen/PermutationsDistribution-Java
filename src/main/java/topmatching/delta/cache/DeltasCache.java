package topmatching.delta.cache;

import java.util.ArrayList;
import java.util.HashMap;
import topmatching.delta.DeltasContainer;

public class DeltasCache {

	public static class DeltasCacheInfo {
		public int numberOfItem = -1;
	}

	private HashMap<String, DeltasCacheItem> cache;

	public DeltasCache() {
		this.cache = new HashMap<>();
	}

	public synchronized void storeInCache(int graphId, int itemNumber, ArrayList<String> ranking, DeltasContainer dc) {
		String key = String.format("%d-%s", graphId, ranking.toString());
		DeltasCacheItem deltasCacheItem = this.cache.get(key);
		if (deltasCacheItem == null) {
			deltasCacheItem = new DeltasCacheItem(graphId, ranking);
		}
		deltasCacheItem.storeNewDeltasContainer(itemNumber, dc);
		this.cache.put(key, deltasCacheItem);
	}

	private synchronized int prefixLength(ArrayList<String> a, ArrayList<String> b) {
		int len = -1, i = 0, minLen = Math.min(a.size(), b.size());
		while (i <= minLen) {
			if (a.get(i).equals(b.get(i))) {
				len++;
			} else {
				break;
			}
			i++;
		}
		return len;
	}

	public synchronized DeltasContainer getFromCache(int graphId, ArrayList<String> ranking,
			DeltasCacheInfo deltasCacheInfo) {
		String maxKey = "";
		int max = -1;
		for (String key : this.cache.keySet()) {
			int prefixLen = prefixLength(this.cache.get(key).getRanking(), ranking);
			if (prefixLen > max) {
				max = prefixLen;
				maxKey = key;
			}
		}

		if (max == -1) {
			return null;
		} else {
			deltasCacheInfo.numberOfItem = max;
		}
		return this.cache.get(maxKey).getDeltasContainer(max);
	}

}
