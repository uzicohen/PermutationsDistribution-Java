package general;

import java.util.ArrayList;
import java.util.HashMap;

import pattern.Graph;

public interface IAlgorithm {
	/**
	 * 
	 * Calculate the probability of a given pattern with a list of distributions
	 * with the same reference ranking (model) and different phi
	 * 
	 * @param graph
	 *            The input graph (pattern)
	 * @param distributions
	 *            A list of distributions that share the same reference ranking,
	 *            and differ with the phi
	 * @return A mapping from phi to the output probability of the algorithm, as
	 *         returned by the running with this phi
	 */
	public HashMap<Double, Double> calculateProbability(Graph graph, ArrayList<Distribution> distributions);
}
