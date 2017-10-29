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

	private int scenrioId;

	private int numOfThreads;

	private int bucket;

	private int queryId;

	private int cartesianProduct;

	private Graph graph;

	private ArrayList<Distribution> distributions;

	private boolean isDone;

	public ExperimentData(String experimentRow, int numOfThreads) {
		this.numOfThreads = numOfThreads;
		String[] components = experimentRow.split(",");
		this.scenrioId = Integer.parseInt(components[0]);
		this.bucket = Integer.parseInt(components[1]);
		this.queryId = Integer.parseInt(components[2]);
		this.cartesianProduct = Integer.parseInt(components[3]);
		initGraph(components);
		this.isDone = false;
	}

	@Override
	public String toString() {
		return "scenrioId=" + scenrioId + ", numOfThreads=" + numOfThreads + ", bucket=" + bucket + ", queryId="
				+ queryId + ", cartesianProduct=" + cartesianProduct + ", isDone=" + isDone + "\n";
	}

	private void initGraphAux(Node node, HashMap<String, HashSet<String>> labelToSetOfItems) {
		node.setItems(labelToSetOfItems.get(node.getLabel()));
		for (Node child : node.getChildren()) {
			initGraphAux(child, labelToSetOfItems);
		}
	}

	private void initGraph(String[] components) {
		this.graph = GraphGenerator.GetGraph(this.queryId + 19, new ArrayList<>(), 0);
		this.distributions = new ArrayList<>();
		HashMap<String, HashSet<String>> labelToSetOfItems = new HashMap<>();
		for (int i = 4; i < components.length - 2; i++) {
			labelToSetOfItems.put(String.format("Cand%d", i - 3),
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

	public int getScenrioId() {
		return scenrioId;
	}

	public void setScenrioId(int scenrioId) {
		this.scenrioId = scenrioId;
	}

	public int getBucket() {
		return bucket;
	}

	public int getNumOfThreads() {
		return numOfThreads;
	}

	public void setNumOfThreads(int numOfThreads) {
		this.numOfThreads = numOfThreads;
	}

	public int getQueryId() {
		return queryId;
	}

	public int getCartesianProduct() {
		return cartesianProduct;
	}

	public Graph getGraph() {
		return graph;
	}

	public ArrayList<Distribution> getDistributions() {
		return distributions;
	}

	public void setBucket(int bucket) {
		this.bucket = bucket;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public void setCartesianProduct(int cartesianProduct) {
		this.cartesianProduct = cartesianProduct;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void setDistributions(ArrayList<Distribution> distributions) {
		this.distributions = distributions;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
