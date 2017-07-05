package sampled;

import general.Distribution;
import general.Mallows;

public class SampledDistribution extends Distribution {

	public SampledDistribution(Mallows model, int n) {
		this.model = model;
		this.permutations = SampledUtils.samplePermutations(model, n);
	}
}
