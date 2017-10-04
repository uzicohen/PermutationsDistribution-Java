package liftedtopmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import general.main.PrintFlow;
import pattern.Graph;
import pattern.PatternUtils;
import topmatching.TopMatchingArgs;
import topmatching.TopProbArgs;
import topmatching.delta.Delta;
import topmatching.delta.DeltasContainer;
import topmatching.delta.DeltasContainerGenerator;

public class LiftedTopMatchingDeltasContainerGenerator {

	public DeltasContainer getInitialDeltas(TopMatchingArgs topMatchingArgs) {
		DeltasContainer result = new DeltasContainer(topMatchingArgs);

		Graph graph = topMatchingArgs.getG();

		ArrayList<HashMap<String, String>> allPossibleAssignments = PatternUtils.getAllPossibleAssigments(graph);

		DeltasContainerGenerator deltasGenerator = new DeltasContainerGenerator(topMatchingArgs);

		for (HashMap<String, String> gamma : allPossibleAssignments) {

			PrintFlow.printGamma(gamma);

			DeltasContainer r = deltasGenerator.getInitialDeltas(new TopProbArgs(gamma));

			PrintFlow.printDeltasContainer("Initial deltas", r, gamma);

			Iterator<Delta> iter = r.iterator();
			while (iter.hasNext()) {
				Delta delta = iter.next();
				result.addDelta(delta);
			}
		}
		return result;
	}
}
