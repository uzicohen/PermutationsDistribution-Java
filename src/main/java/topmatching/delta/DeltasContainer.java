package topmatching.delta;

import java.util.Iterator;

import topmatching.TopMatchingArgs;

public abstract class DeltasContainer {

	protected TopMatchingArgs topMatchingArgs;

	public DeltasContainer(TopMatchingArgs topMatchingArgs) {
		this.topMatchingArgs = topMatchingArgs;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Delta> iter = iterator();

		if (!iter.hasNext()) {
			return "[]";
		}

		while (iter.hasNext()) {
			sb.append(iter.next());
			sb.append("\n          ");
		}
		sb.replace(sb.length() - 11, sb.length(), "");
		return sb.toString();
	}

	public abstract void addDelta(Delta delta);

	/**
	 * 
	 * 
	 * @param delta
	 * @return the object if the delta exists and null o.w.
	 */
	public abstract Delta getDelta(Delta delta);

	public abstract Iterator<Delta> iterator();

}
