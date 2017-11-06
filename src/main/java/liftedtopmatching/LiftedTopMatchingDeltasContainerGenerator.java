package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import graph.Graph;
import graph.GraphUtils;
import topmatching.TopMatchingArgs;
import topmatching.TopProbArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.DeltasContainerGenerator;

public class LiftedTopMatchingDeltasContainerGenerator {

	public DeltasContainer getInitialDeltas(TopMatchingArgs topMatchingArgs) {
		DeltasContainer result = new DeltasContainer();

		Graph graph = topMatchingArgs.getG();

		ArrayList<HashMap<String, String>> allPossibleAssignments = GraphUtils.getAllPossibleAssigments(graph);

		DeltasContainerGenerator deltasGenerator = new DeltasContainerGenerator(topMatchingArgs);

		for (HashMap<String, String> gamma : allPossibleAssignments) {
			DeltasContainer r = deltasGenerator.getInitialDeltas(new TopProbArgs(gamma));
			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				result.addDelta(delta);
			}
		}
		return result;
	}
}
