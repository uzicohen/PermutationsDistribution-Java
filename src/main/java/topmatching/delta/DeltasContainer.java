package topmatching.delta;

import java.util.Iterator;

import topmatching.TopMatchingArgs;

public abstract class DeltasContainer {

	protected TopMatchingArgs topMatchingArgs;

	public DeltasContainer(TopMatchingArgs topMatchingArgs) {
		this.topMatchingArgs = topMatchingArgs;
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
