package pattern;

import java.util.ArrayList;
import java.util.HashSet;


public class Node {
	private HashSet<String> items;

	private String label;

	private ArrayList<Node> children;

	private int level;

	public Node(HashSet<String> items, String label, ArrayList<Node> children) {
		super();
		this.items = items;
		this.label = label;
		this.children = children;
		this.level = 0;
	}

	public HashSet<String> getItems() {
		return items;
	}

	public String getLabel() {
		return label;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public int getLevel() {
		return level;
	}

	public void setItems(HashSet<String> items) {
		this.items = items;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public void setLevel(int level) {
		this.level = level;
	}	
	
}
