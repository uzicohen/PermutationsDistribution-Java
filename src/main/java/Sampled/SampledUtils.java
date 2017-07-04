package Sampled;

import java.util.ArrayList;
import java.util.Random;

import general.GeneralUtils;
import general.Mallows;
import general.Permutation;

public class SampledUtils {

	private static Random RANDOM = new Random(1313);

	public static ArrayList<Permutation> samplePermutations(Mallows model, int n) {
		ArrayList<ArrayList<Double>> insertionProbs = GeneralUtils.getInsertionProbabilities(model);
		ArrayList<Permutation> result = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			// create insertion vector
			ArrayList<Integer> insertionVector = createInsertionVector(model, insertionProbs);

			ArrayList<String> itemList = new ArrayList<>();

			itemList.add(model.getModal().get(0));

			for (int j = 1; j < insertionVector.size(); j++) {
				String item = model.getModal().get(j);
				int pos = insertionVector.get(j);
				int k = 0;
				// LinkedListNode<string> currentNode = itemsList.First;
				while (pos > 1) {
					k++;
					pos--;
				}
				if (k >= itemList.size()) {
					itemList.add(itemList.size(), item);
				} else {
					itemList.add(k, item);

				}
			}

			// Add the permutation
			result.add(new Permutation(itemList, 1.0 / (double) n));
		}

		return result;
	}

	private static ArrayList<Integer> createInsertionVector(Mallows model, ArrayList<ArrayList<Double>> insertionProbs)
    {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);

        for (int i = 1; i < insertionProbs.size(); i++)
        {
            ArrayList<Double> probs = insertionProbs.get(i);
            double randNum = RANDOM.nextDouble();
            double sum = probs.get(0);
            for (int j = 1; j <= probs.size(); j++)
            {
                if (sum >= randNum)
                {
                    result.add(j);
                    break;
                }
                sum += probs.get(j);
            }
        }
        return result;
    }
}
