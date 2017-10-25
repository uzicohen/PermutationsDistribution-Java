package experiments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import general.Distribution;
import general.Mallows;
import pattern.Graph;
import pattern.Node;
import topmatching.SimpleDistribution;

public class ExperimentData {

	private Graph graph;

	private ArrayList<Distribution> distributions;

	private int patternNum;

	public ExperimentData(Graph graph, String experimentRow, int patternNum) {
		this.graph = graph;
		this.distributions = new ArrayList<>();
		this.patternNum = patternNum;
		initGraph(experimentRow);
	}

	@Override
	public String toString() {
		return "[graph=" + graph + ", distributions=" + distributions + ", patternNum =" + patternNum + "]";
	}

	private void initGraphAux(Node node, HashMap<String, HashSet<String>> labelToSetOfItems) {
		node.setItems(labelToSetOfItems.get(node.getLabel()));
		for (Node child : node.getChildren()) {
			initGraphAux(child, labelToSetOfItems);
		}
	}

	private void initGraph(String experimentRow) {
		HashMap<String, HashSet<String>> labelToSetOfItems = new HashMap<>();
		String[] components = experimentRow.split(",");
		for (int i = 0; i < components.length - 2; i++) {
			labelToSetOfItems.put(String.format("Cand%d", i + 1),
					new HashSet<>(Arrays.asList(components[i].split("_"))));
		}
		for (Node root : this.graph.getRoots()) {
			initGraphAux(root, labelToSetOfItems);
		}
		String[] strPhis = components[components.length - 1].split("_");
		for (String strPhi : strPhis) {
			double phi = Double.parseDouble(strPhi);
			ArrayList<String> modal = new ArrayList<>(Arrays.asList(components[components.length - 2].split("_")));
			this.distributions.add(new SimpleDistribution(new Mallows(modal, phi)));
		}
	}

	public Graph getGraph() {
		return graph;
	}

	public ArrayList<Distribution> getDistributions() {
		return distributions;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void setDistributions(ArrayList<Distribution> distributions) {
		this.distributions = distributions;
	}

	public int getPatternNum() {
		return patternNum;
	}

	public void setPatternNum(int patternNum) {
		this.patternNum = patternNum;
	}
}
