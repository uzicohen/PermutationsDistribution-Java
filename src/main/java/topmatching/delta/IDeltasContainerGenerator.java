package topmatching.delta;

import topmatching.TopMatchingArgs;
import topmatching.TopProbArgs;

public interface IDeltasContainerGenerator {

	public void init(TopMatchingArgs topMatchingArgs);
	
	public DeltasContainer getInitialDeltas(TopProbArgs topProbArgs);
	
}
