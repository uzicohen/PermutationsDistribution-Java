package common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import graph.Graph;
import graph.Node;

public class RandomGraphGenerator {

	public static Graph GetRandomGraph(int numOfItems, int numOfLabels, Random rand) {
		
		ArrayList<Node> labels = new ArrayList<>();

		for (int i = 0; i < numOfLabels; i++) {
			// label i:
			String label = "x" + (i + 1);

			// Create this label's items
			HashSet<String> items = new HashSet<>();
			for (int j = 0; j < numOfItems; j++) {
				String item = "s" + (j + 1);
				if (rand.nextDouble() > 0.5) {
					items.add(item);
				}
			}
			labels.add(new Node(items, label, null));
		}

		// Create this label's children
		for (int i = 0; i < numOfLabels; i++) {
			ArrayList<Node> children = new ArrayList<>();
			for (int k = i + 1; k < numOfLabels; k++) {
				if (rand.nextDouble() > 0.3) {
					children.add(labels.get(k));
				}
			}

			// Update the label's children
			labels.get(i).setChildren(children);
		}

		// Get all labels that have parents
		HashSet<String> seen = new HashSet<>();
		for (Node label : labels) {
			for (Node child : label.getChildren()) {
				seen.add(child.getLabel());
			}
		}

		// Get all labels that do not have parents and add them as roots
		ArrayList<Node> roots = new ArrayList<>();
		for (Node label : labels) {
			if (!seen.contains(label.getLabel())) {
				roots.add(label);
			}
		}

		return new Graph(roots, 0);

	}
}
