package topmatching;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;

import javax.print.DocFlavor.STRING;

import general.Distribution;
import general.GeneralUtils;
import general.IAlgorithm;
import pattern.Graph;
import pattern.Node;
import pattern.PatternUtils;

public class TopMatchingAlgorithm implements IAlgorithm{
	
	private TopMatchingArgs topMatchingArgs;

    public TopMatchingAlgorithm()
    {
        //this.TopMatchingArgs = new TopMatchingArgs();
    }

    public double calculateProbability(Graph graph, Distribution distribution)
    {
    	ArrayList<HashMap<String, String>>  allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);

        // For each label, we keep in a dictionary it's parents
        HashMap<String, HashSet<String>> labelToParentsMap = new HashMap<>();

        // For each sigma, we keep in a dictionary it's possible labels
        HashMap<String, HashSet<String>> lambda = new HashMap<>();

        // Create the data structures in the level of top matching
        preProcessGraph(graph, labelToParentsMap, lambda);

        this.topMatchingArgs = new TopMatchingArgs(graph, distribution, labelToParentsMap, lambda, GeneralUtils.getInsertionProbabilities(distribution.getModel()));

        TopMatchingUtils.init(this.topMatchingArgs);


        // TODO: We can easily parallelize this section
        double result = 0.0;
        for (HashMap<String,String> gamma : allPossibleAssignments)
        {
//            HashMap<String, String> tmpGamma = new HashMap<>();
//            tmpGamma.put("x", "s1");
//            tmpGamma.put("y", "s1");
//            tmpGamma.put("z", "s4");
//            tmpGamma.put("w", "s5");


            TopProb topProb = new TopProb(gamma, topMatchingArgs);
            double probability = topProb.Calculate();
            result += probability;
//            System.out.println(gamma.toString() + probability);
//            Console.WriteLine($"Gamma: {string.Join(",", gamma)}, Probability: {probability}");
        }
        return result;
    }

    private void preProcessGraphAux(Node node, HashMap<String, HashSet<String>> labelToParentsMap, HashMap<String, HashSet<String>> lambda)
    {
        String label = node.getLabel();
        for (String sigma : node.getItems())
        {
            HashSet<String> currentSigmaLabels = lambda.containsKey(sigma) ? lambda.get(sigma) : new HashSet<>();
            currentSigmaLabels.add(label);
            lambda.put(sigma, currentSigmaLabels);
        }

        for (Node child : node.getChildren())
        {
            HashSet<String> currentChildParents = labelToParentsMap.containsKey(child.getLabel()) ? labelToParentsMap.get(child.getLabel()) : new HashSet<>();
            currentChildParents.add(label);
            labelToParentsMap.put(child.getLabel(), currentChildParents);

            preProcessGraphAux(child, labelToParentsMap, lambda);
        }
    }


    private void preProcessGraph(Graph graph, HashMap<String, HashSet<String>> labelToParentsMap, HashMap<String, HashSet<String>> lambda)
    {
        for (Node root : graph.getRoots())
        {
            preProcessGraphAux(root, labelToParentsMap, lambda);
        }
    }
}
