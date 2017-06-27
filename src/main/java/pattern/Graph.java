package pattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Graph {

	public ArrayList<Node> roots;

	public Graph(ArrayList<Node> roots) {
		this.roots = roots;
		for (Node root : this.roots) {
			InitGraph(root, 0);
		}
	}

	private void InitGraph(Node node, int level) {
		node.setLevel(level);
		for (Node child : node.getChildren()) {
			InitGraph(child, level + 1);
		}
	}

	private void ToStringAux(StringBuilder sb, List<Node> nodes, HashSet<String> seen) {
		if (nodes.size() == 0) {
			return;
		}

		for (Node node : nodes) {
			if (!seen.contains(node.getLabel())) {
				sb.append(String.format("Level: %d, Label: %s, Items: %s\n", node.getLevel(), node.getLabel(), node.getItems()));
			}
		}

		for (Node node : nodes) {
			if (!seen.contains(node.getLabel())) {
				seen.add(node.getLabel());
				ToStringAux(sb, node.getChildren(), seen);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		ToStringAux(sb, this.roots, new HashSet<String>());
		return sb.toString();
	}

	public ArrayList<Node> getRoots() {
		return roots;
	}

	public void setRoots(ArrayList<Node> roots) {
		this.roots = roots;
	}	

}
