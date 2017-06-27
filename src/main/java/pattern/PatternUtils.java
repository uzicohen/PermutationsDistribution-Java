package pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PatternUtils  {
    private static void GetPossibleValuesPerLabelAsDictionaryAux(HashMap<String, ArrayList<String>> result, ArrayList<Node> nodes)
    {
        for (Node node : nodes)
        {
            if (!result.containsKey(node.getLabel()))
            {
                result.put(node.getLabel(), new ArrayList<>(node.getItems()));
                GetPossibleValuesPerLabelAsDictionaryAux(result, node.getChildren());
            }
        }
    }

    public static HashMap<String, ArrayList<String>> GetPossibleValuesPerLabelAsDictionary(Graph graph)
    {
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        GetPossibleValuesPerLabelAsDictionaryAux(result, graph.getRoots());
        return result;
    }

    private static void GetAllPossibleAssigmentsAux(
        ArrayList<HashMap<String, String>> result,
        HashMap<String, String> current,
        HashMap<String, ArrayList<String>> possibleValuesPerLabelAsDictionary)
    {
        if (possibleValuesPerLabelAsDictionary.isEmpty())
        {
            result.add(current);
            return;
        }

        String key = possibleValuesPerLabelAsDictionary.keySet().iterator().next();
        ArrayList<String> possibleValuesForKey = possibleValuesPerLabelAsDictionary.get(key);
        for (String value : possibleValuesForKey)
        {
            HashMap<String, String> newCurrent = new HashMap<>(current);
            newCurrent.put(key, value);
            HashMap<String, ArrayList<String>> newPossibleValuesDictionary = new HashMap<>(possibleValuesPerLabelAsDictionary);
            newPossibleValuesDictionary.remove(key);
            GetAllPossibleAssigmentsAux(result, newCurrent, newPossibleValuesDictionary);
        }
    }

    public static ArrayList<HashMap<String, String>> GetAllPossibleAssigments(Graph graph)
    {
        // TODO: Use the graph structure to remove all assignments that involve two same items that are assigned to labels from two different levels in the graph
    	ArrayList<HashMap<String, String>> result = new ArrayList<>();
        GetAllPossibleAssigmentsAux(result, new HashMap<>(), GetPossibleValuesPerLabelAsDictionary(graph));
        return result;
    }

    private static void GetLabelsPerLevelAux(Node node, HashSet<String> seen, ArrayList<ArrayList<String>> result)
    {
        if (seen.contains(node.getLabel()))
        {
            return;
        }
        seen.add(node.getLabel());
        if (result.size() <= node.getLevel())
        {
            result.add(new ArrayList<>());
        }
        result.get(node.getLevel()).add(node.getLabel());
        for (Node child : node.getChildren())
        {
            GetLabelsPerLevelAux(child, seen, result);
        }
    }

    public static ArrayList<ArrayList<String>> GetLabelsPerLevel(Graph graph)
    {
    	ArrayList<ArrayList<String>> result = new ArrayList<>();
        HashSet<String> seen = new HashSet<>();
        for (Node root : graph.getRoots())
        {
            GetLabelsPerLevelAux(root, seen, result);
        }
        return result;
    }
}
