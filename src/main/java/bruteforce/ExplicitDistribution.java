package bruteforce;

import java.util.ArrayList;

import general.Distribution;
import general.GeneralUtils;
import general.Mallows;
import general.Permutation;

public class ExplicitDistribution extends Distribution {
	public ExplicitDistribution(Mallows model)
    {
        this.model = model;
        this.permutations = new ArrayList<>();
        ArrayList<ArrayList<String>> rawPermutations = GeneralUtils.generatePermutations(model.getModal());
        rawPermutations.forEach(rawPermutation ->
        {
            int kendallTau = GeneralUtils.kendallTau(this.model.getModal(), rawPermutation);
            double probability = Math.pow(this.model.getPhi(), kendallTau);
            this.permutations.add(new Permutation(rawPermutation, probability / this.model.getZ()));
        });
    }
}
