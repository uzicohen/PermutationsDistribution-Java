package general;

import java.util.ArrayList;

public class Distribution {
	
	protected Mallows model;

	protected ArrayList<Permutation> permutations;

    @Override
    public String toString()
    {
        return this.permutations.toString();
    }

	public Mallows getModel() {
		return model;
	}

	public ArrayList<Permutation> getPermutations() {
		return permutations;
	}

	public void setModel(Mallows model) {
		this.model = model;
	}

	public void setPermutations(ArrayList<Permutation> permutations) {
		this.permutations = permutations;
	}    
    
}
