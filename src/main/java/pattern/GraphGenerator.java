package pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import pattern.Graph;
import pattern.Node;

public class GraphGenerator {

	public static Graph GetGraph(int scenrio) {

		Graph graph = null;
		switch (scenrio) {

		// (1,4), P = 0.990541370714296
		case 1:
			Node z = new Node(new HashSet<>(Arrays.asList(new String[] { "s3", "s4" })), "z", new ArrayList<>());

			Node x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			Node y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// (2,5), P = 0.989000393257776
		case 2:

			Node w = new Node(new HashSet<>(Arrays.asList(new String[] { "s4", "s5" })), "w", new ArrayList<>());

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s3", "s4" })), "z", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { w, z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// (3,8), P = 0.732986262312703
		case 3:
			// Yellow
			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s6" })), "y", new ArrayList<>());

			// Red
			Node r = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s5", "s7" })), "r",
					new ArrayList<>(Arrays.asList(new Node[] { y })));

			// Purple
			Node p = new Node(new HashSet<>(Arrays.asList(new String[] { "s3", "s8" })), "p",
					new ArrayList<>(Arrays.asList(new Node[] { r })));

			// Green
			Node g = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s4" })), "g",
					new ArrayList<>(Arrays.asList(new Node[] { r })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { g, p })));

			break;

		// (4,3), P = 0.719424460431655
		case 4:

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s3" })), "z", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s2" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// (5,6), P = 0.761006046617952
		case 5:

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s4" })), "z", new ArrayList<>());

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s5", "s6" })), "w", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z, w })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// (6,5), P = 0.934812879701345
		case 6:

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s4", "s5" })), "z", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// (7,5), P = 0.9498882307361549
		case 7:

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s5" })), "w", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s4" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s1" })), "z",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y, z })));

			break;

		// (8,4), P = 1.0
		case 8:

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3", "s4" })), "w", new ArrayList<>());

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3", "s4" })), "z", new ArrayList<>());

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3", "s4" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { w, z })));

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3", "s4" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { y, z, w })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x })));

			break;

		// (9,6), P = 0.6937277499487445
		case 9:

			Node t = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s6" })), "t", new ArrayList<>());

			Node m = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s5" })), "m", new ArrayList<>());

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s4", "s5" })), "w",
					new ArrayList<>(Arrays.asList(new Node[] { t, m })));

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s6" })), "z", new ArrayList<>());

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z, w })));

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// P = 0.503103
		case 10:

			// Level 2
			Node a = new Node(new HashSet<>(Arrays.asList(new String[] { "s4", "s5", "s10" })), "a", new ArrayList<>());

			Node b = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s7", "s8", "s9" })), "b",
					new ArrayList<>());

			// Level 1
			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s7", "s8" })), "w", new ArrayList<>());

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s3", "s4", "s9" })), "z",
					new ArrayList<>(Arrays.asList(new Node[] { a, b })));

			// Level 0
			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s5", "s6", "s9" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { w, a, z })));

			t = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s9", "s10" })), "t",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y, t })));

			break;

		// P = 0.9741246606894051
		case 11:

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s5" })), "w", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s4" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3", "s4", "s7", "s8" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s5", "s9" })), "z",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y, z })));

			break;

		// 2 in level 0, 3 in level 1 and 3 in level 2. 12 items

		// P = 0.567747
		case 12:

			// Level 2
			Node x8 = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s5", "s11" })), "x8",
					new ArrayList<>());

			Node x7 = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s4", "s11" })), "x7",
					new ArrayList<>());

			Node x6 = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s4" })), "x6", new ArrayList<>());

			// Level 1
			Node x5 = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3", "s4", "s7", "s10" })), "x5",
					new ArrayList<>(Arrays.asList(new Node[] { x6, x8 })));

			Node x4 = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3", "s4", "s7", "s9" })), "x4",
					new ArrayList<>(Arrays.asList(new Node[] { x6, x7 })));

			Node x3 = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3", "s4", "s7", "s8" })), "x3",
					new ArrayList<>(Arrays.asList(new Node[] { x4, x7, x8 })));

			// Level 0
			Node x2 = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s5", "s8" })), "x2",
					new ArrayList<>(Arrays.asList(new Node[] { x3, x5 })));

			Node x1 = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3", "s6", "s9", "s10" })), "x1",
					new ArrayList<>(Arrays.asList(new Node[] { x3, x5, x7 })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x1, x2 })));

			break;

		// (13,6), P = 0.862931
		case 13:

			t = new Node(new HashSet<>(Arrays.asList(new String[] { "s5", "s6" })), "t", new ArrayList<>());

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s3" })), "w",
					new ArrayList<>(Arrays.asList(new Node[] { t })));

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s6" })), "z", new ArrayList<>());

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s5" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z, w })));

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, y })));

			break;

		// (14,7), P = 0.862931
		case 14:
			// Yellow
			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s5", "s7" })), "y", new ArrayList<>());

			// Black
			b = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s4" })), "b", new ArrayList<>());

			// Purple
			p = new Node(new HashSet<>(Arrays.asList(new String[] { "s3" })), "p",
					new ArrayList<>(Arrays.asList(new Node[] { y })));

			// Green
			g = new Node(new HashSet<>(Arrays.asList(new String[] { "s6", "s7" })), "g",
					new ArrayList<>(Arrays.asList(new Node[] { p, b })));

			r = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "r",
					new ArrayList<>(Arrays.asList(new Node[] { b })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { r, g })));

			break;

		// (15,5)
		case 15:
			// Yellow
			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s5" })), "y", new ArrayList<>());

			// Purple
			p = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s4" })), "p", new ArrayList<>());

			// Red
			r = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "r",
					new ArrayList<>(Arrays.asList(new Node[] { y })));

			// Green
			g = new Node(new HashSet<>(Arrays.asList(new String[] { "s3" })), "g",
					new ArrayList<>(Arrays.asList(new Node[] { y, p })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { r, g })));

			break;

		// (16,15)
		case 16:
			// Yellow
			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s5" })), "y", new ArrayList<>());

			// Purple
			p = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3", "s4", "s15" })), "p",
					new ArrayList<>());

			// Red
			r = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2", "s3", "s8", "s9", "s11" })), "r",
					new ArrayList<>(Arrays.asList(new Node[] { y })));

			// Green
			g = new Node(
					new HashSet<>(Arrays.asList(new String[] { "s3", "s6", "s7", "s8", "s10", "s12", "s13", "s14" })),
					"g", new ArrayList<>(Arrays.asList(new Node[] { y, p })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { r, g })));

			break;

		// (17,4)
		case 17:
			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s3", "s4" })), "y", new ArrayList<>());

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { y })));

			w = new Node(new HashSet<>(Arrays.asList(new String[] { "s2", "s3", "s4" })), "w", new ArrayList<>());

			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "z",
					new ArrayList<>(Arrays.asList(new Node[] { w })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x, z })));

			break;

		// (18,4)
		case 18:
			z = new Node(new HashSet<>(Arrays.asList(new String[] { "s3", "s4" })), "z", new ArrayList<>());

			y = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s3" })), "y",
					new ArrayList<>(Arrays.asList(new Node[] { z })));

			x = new Node(new HashSet<>(Arrays.asList(new String[] { "s1", "s2" })), "x",
					new ArrayList<>(Arrays.asList(new Node[] { y, z })));

			graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] { x })));

			break;

		}
		return graph;

	}
}
