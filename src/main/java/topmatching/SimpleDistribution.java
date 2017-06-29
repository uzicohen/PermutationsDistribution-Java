package topmatching;

import general.Distribution;
import general.Mallows;

public class SimpleDistribution extends Distribution {
	public SimpleDistribution(Mallows model)
    {
        this.model = model;
        // No need for permutations with this algorithm
        this.permutations = null;
    }
}
