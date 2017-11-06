package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import general.Distribution;
import graph.Graph;

public class TopMatchingArgs {

	private Graph g;

	private ArrayList<Distribution> distributions;

	private HashMap<String, HashSet<String>> labelToParentsMap;

	private HashMap<String, HashSet<String>> lambda;

	private HashMap<Double, ArrayList<ArrayList<Double>>> phiToInsertionProbs;

	public TopMatchingArgs(Graph g, ArrayList<Distribution> distributions,
			HashMap<String, HashSet<String>> labelToParentsMap, HashMap<String, HashSet<String>> lambda,
			HashMap<Double, ArrayList<ArrayList<Double>>> phiToInsertionProbs) {
		this.g = g;
		this.distributions = distributions;
		this.labelToParentsMap = labelToParentsMap;
		this.lambda = lambda;
		this.phiToInsertionProbs = phiToInsertionProbs;
	}

	public Graph getG() {
		return g;
	}

	public void setG(Graph g) {
		this.g = g;
	}

	public ArrayList<Distribution> getDistributions() {
		return distributions;
	}

	public void setDistributions(ArrayList<Distribution> distributions) {
		this.distributions = distributions;
	}

	public HashMap<String, HashSet<String>> getLabelToParentsMap() {
		return labelToParentsMap;
	}

	public void setLabelToParentsMap(HashMap<String, HashSet<String>> labelToParentsMap) {
		this.labelToParentsMap = labelToParentsMap;
	}

	public HashMap<String, HashSet<String>> getLambda() {
		return lambda;
	}

	public void setLambda(HashMap<String, HashSet<String>> lambda) {
		this.lambda = lambda;
	}

	public HashMap<Double, ArrayList<ArrayList<Double>>> getPhiToInsertionProbs() {
		return phiToInsertionProbs;
	}

	public void setInsertionProbs(HashMap<Double, ArrayList<ArrayList<Double>>> phiToInsertionProbs) {
		this.phiToInsertionProbs = phiToInsertionProbs;
	}
}
