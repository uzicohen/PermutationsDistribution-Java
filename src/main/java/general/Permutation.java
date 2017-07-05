package general;

import java.util.ArrayList;
import java.util.Collections;

public class Permutation {
	
	private ArrayList<String> itemsOrder;

	private double probability;    
    
    public Permutation(ArrayList<String> itemsOrder, double probability) {
		super();
		this.itemsOrder = itemsOrder;
		this.probability = probability;
	}

	@Override
    public String toString()
    {
    	ArrayList<String> sigma = new ArrayList<>(this.itemsOrder);
        Collections.sort(sigma);

        return String.format("%s, Prob: %f, Kendall-Tau: %d\n", this.itemsOrder, this.probability, GeneralUtils.kendallTau(sigma, this.itemsOrder));
    }

	public ArrayList<String> getItemsOrder() {
		return itemsOrder;
	}

	public double getProbability() {
		return probability;
	}

	public void setItemsOrder(ArrayList<String> itemsOrder) {
		this.itemsOrder = itemsOrder;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}	
}
