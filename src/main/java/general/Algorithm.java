package general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pattern.Graph;
import pattern.Node;

public abstract class Algorithm {

	protected Graph originalGraph;

	protected ArrayList<Distribution> originalDistributions;

	protected Graph graph;

	protected ArrayList<Distribution> distributions;

	protected HashMap<String, String> origItemToNewItem;

	/*
	 * @param graph The input graph (pattern)
	 * 
	 * @param distributions A list of distributions that share the same
	 * reference ranking, and differ with the phi
	 * 
	 */
	public Algorithm(Graph graph, ArrayList<Distribution> distributions) {
		super();
		this.originalGraph = graph;
		this.originalDistributions = distributions;
		this.origItemToNewItem = new HashMap<>();
		convertItemsToAscendingForm();
	}

	/**
	 * 
	 * Calculate the probability of a given pattern with a list of distributions
	 * with the same reference ranking (model) and different phi
	 * 
	 * @return A mapping from phi to the output probability of the algorithm, as
	 *         returned by the running with this phi
	 */
	public abstract HashMap<Double, Double> calculateProbability();

	/**
	 * 
	 * Calculate the probability of a given pattern with a list of distributions
	 * with the same reference ranking (model) and different phi
	 * 
	 * @param itemNumToStoreInCache
	 *            The item's number for which the algorithm will store the
	 *            extracted deltas in the cache for future calculations
	 * 
	 * @return A mapping from phi to the output probability of the algorithm, as
	 *         returned by the running with this phi
	 */
	public abstract HashMap<Double, Double> calculateProbability(int itemNumToStoreInCache);

	private void convertItemsToAscendingForm() {
		fillOrigItemToNewItem();
		convertDistributions();
		convertGraph();
	}

	private void fillOrigItemToNewItem() {
		ArrayList<String> modal = this.originalDistributions.get(0).model.getModal();
		for (int i = 0; i < modal.size(); i++) {
			this.origItemToNewItem.put(modal.get(i), String.format("s%d", i + 1));
		}
	}

	private void convertDistributions() {
		ArrayList<String> origModal = this.originalDistributions.get(0).model.getModal();
		ArrayList<String> modal = new ArrayList<>();
		origModal.forEach(origItem -> modal.add(this.origItemToNewItem.get(origItem)));
		this.distributions = new ArrayList<>();
		for (int i = 0; i < this.originalDistributions.size(); i++) {
			Mallows mallows = new Mallows(new ArrayList<>(modal),
					this.originalDistributions.get(i).getModel().getPhi());
			Distribution distribution = new Distribution();
			distribution.setModel(mallows);
			this.distributions.add(distribution);
		}
	}

	private void convertGraph() {
		HashMap<String, Node> labelToNode = new HashMap<>();
		createNewNodesMap(labelToNode);
		for (Node origNode : this.originalGraph.getRoots()) {
			createNewGraph(origNode, labelToNode);
		}
		ArrayList<Node> newRoots = new ArrayList<>();
		this.originalGraph.getRoots().forEach(root -> newRoots.add(labelToNode.get(root.getLabel())));
		this.graph = new Graph(newRoots, this.originalGraph.getId());
	}

	private void createNewNodesMap(HashMap<String, Node> labelToNode) {
		for (Node origNode : this.originalGraph.getRoots()) {
			createNewNodesMapAux(origNode, labelToNode);
		}
	}

	private void createNewNodesMapAux(Node origNode, HashMap<String, Node> labelToNode) {
		// Copy the node with the new items
		String label = origNode.getLabel();
		HashSet<String> newItems = new HashSet<>();
		origNode.getItems().forEach(origItem -> newItems.add(this.origItemToNewItem.get(origItem)));
		Node newNode = new Node(newItems, label, null);
		labelToNode.put(label, newNode);
		for (Node child : origNode.getChildren()) {
			if (!labelToNode.containsKey(child.getLabel())) {
				createNewNodesMapAux(child, labelToNode);
			}
		}
	}

	private void createNewGraph(Node origNode, HashMap<String, Node> labelToNode) {
		ArrayList<Node> origChildren = origNode.getChildren();
		Node newNode = labelToNode.get(origNode.getLabel());
		ArrayList<Node> newChildren = new ArrayList<>();
		origChildren.forEach(origChild -> newChildren.add(labelToNode.get(origChild.getLabel())));
		newNode.setChildren(newChildren);
		for (Node child : origNode.getChildren()) {
			createNewGraph(child, labelToNode);
		}
	}

}
