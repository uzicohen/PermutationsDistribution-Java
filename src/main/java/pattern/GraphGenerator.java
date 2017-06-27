package pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GraphGenerator {
	public static Graph GetGraph(int caseTest)
    {
		//HashSet<String> items, String label, List<Node> children, int level
        Graph graph = null;
        switch (caseTest)
        {
            case 1:
                Node z = new Node(new HashSet<>(Arrays.asList(new String[] {"s3", "s4" })), "z", new ArrayList<>());

                Node x = new Node(new HashSet<>(Arrays.asList(new String[] {"s1", "s2" })), "x", new ArrayList<>(Arrays.asList(new Node[] {z})));

                Node y = new Node(new HashSet<>( Arrays.asList(new String[]{"s1", "s3" })), "y", new ArrayList<>(Arrays.asList(new Node[] {z})));

                graph = new Graph(new ArrayList<>(Arrays.asList(new Node[] {x, y })));

                break;

//            case 2:
//                z = new Node { Children = new List<Node>(), Items = new HashSet<string> { "s3", "s4" }, Label = "z" };
//
//                Node w = new Node { Children = new List<Node>(), Items = new HashSet<string> { "s4", "s5" }, Label = "w" };
//
//                x = new Node { Children = new List<Node> { z }, Label = "x", Items = new HashSet<string> { "s1", "s2" } };
//
//                y = new Node { Children = new List<Node> { w, z }, Label = "y", Items = new HashSet<string> { "s1", "s3" } };
//
//                graph = new Graph(new List<Node> { x, y });
//
//                break;
//            case 3:
//                // yellow
//                y = new Node { Children = new List<Node>(), Items = new HashSet<string> { "s2", "s6" }, Label = "y" };
//
//                Node r = new Node { Children = new List<Node> { y }, Items = new HashSet<string> { "s2", "s5", "s7" }, Label = "r" };
//
//                Node p = new Node { Children = new List<Node> { r }, Label = "p", Items = new HashSet<string> { "s3", "s8" } };
//
//                Node g = new Node { Children = new List<Node> { r }, Label = "g", Items = new HashSet<string> { "s1", "s4" } };
//
//                graph = new Graph(new List<Node> { g, p });
//
//                break;
//
//            case 4:
//
//                z = new Node { Children = new List<Node>(), Label = "z", Items = new HashSet<string> { "s3" } };
//
//                x = new Node { Children = new List<Node> { z }, Items = new HashSet<string> { "s1" }, Label = "x" };
//
//                y = new Node { Children = new List<Node> { z }, Items = new HashSet<string> { "s2" }, Label = "y" };
//
//                graph = new Graph(new List<Node> { x, y });
//
//                break;
//
//            case 5:
//
//                z = new Node { Children = new List<Node>(), Label = "z", Items = new HashSet<string> { "s4" } };
//
//                w = new Node { Children = new List<Node>(), Label = "w", Items = new HashSet<string> { "s5", "s6" } };
//
//                x = new Node { Children = new List<Node> { z }, Items = new HashSet<string> { "s1", "s2" }, Label = "x" };
//
//                y = new Node { Children = new List<Node> { z, w }, Items = new HashSet<string> { "s3" }, Label = "y" };
//
//                graph = new Graph(new List<Node> { x, y });
//
//                break;
//
//            case 6:
//
//                z = new Node { Children = new List<Node>(), Label = "z", Items = new HashSet<string> { "s4", "s5" } };
//
//                x = new Node { Children = new List<Node> { z }, Label = "x", Items = new HashSet<string> { "s1", "s2" } };
//
//                y = new Node { Children = new List<Node> { z }, Label = "y", Items = new HashSet<string> { "s3" } };
//
//                graph = new Graph(new List<Node> { x, y });
//
//                break;
        }
        return graph;
    }
}
