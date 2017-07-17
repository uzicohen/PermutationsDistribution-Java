package topmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import general.Distribution;
import general.main.GeneralArgs;
import pattern.Graph;
import topmatching.delta.EnhancedDeltasContainer;
import topmatching.delta.EnhancedDeltasContainerGenerator;
import topmatching.delta.DeltasContainer;
import topmatching.delta.IDeltasContainerGenerator;
import topmatching.delta.SimpleDeltasContainer;
import topmatching.delta.SimpleDeltasContainerGenerator;

public class TopMatchingArgs {

	private Graph g;

	private Distribution rim;

	private HashMap<String, HashSet<String>> labelToParentsMap;

	private HashMap<String, HashSet<String>> lambda;

	private ArrayList<ArrayList<Double>> insertionProbs;

	private IDeltasContainerGenerator deltasContainerGenerator;

	public TopMatchingArgs(Graph g, Distribution rim, HashMap<String, HashSet<String>> labelToParentsMap,
			HashMap<String, HashSet<String>> lambda, ArrayList<ArrayList<Double>> insertionProbs) {
		this.g = g;
		this.rim = rim;
		this.labelToParentsMap = labelToParentsMap;
		this.lambda = lambda;
		this.insertionProbs = insertionProbs;
		this.deltasContainerGenerator = GeneralArgs.enhancedInitialDeltas ? new EnhancedDeltasContainerGenerator()
				: new SimpleDeltasContainerGenerator();
		this.deltasContainerGenerator.init(this);
	}

	public Graph getG() {
		return g;
	}

	public void setG(Graph g) {
		this.g = g;
	}

	public Distribution getRim() {
		return rim;
	}

	public void setRim(Distribution rim) {
		this.rim = rim;
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

	public ArrayList<ArrayList<Double>> getInsertionProbs() {
		return insertionProbs;
	}

	public void setInsertionProbs(ArrayList<ArrayList<Double>> insertionProbs) {
		this.insertionProbs = insertionProbs;
	}

	public IDeltasContainerGenerator getDeltasContainerGenerator() {
		return deltasContainerGenerator;
	}

	public void setDeltasContainerGenerator(IDeltasContainerGenerator deltasContainerGenerator) {
		this.deltasContainerGenerator = deltasContainerGenerator;
	}
}
