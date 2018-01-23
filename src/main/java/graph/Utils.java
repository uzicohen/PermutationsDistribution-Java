package graph;

import java.util.ArrayList;
import java.util.Random;

public class Utils {
	public static Random rand = new Random(1331);

	public static ArrayList<Integer> getRandomList(int size) {

		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			result.add(i);
		}
		for (int i = 0; i < size; i++) {
			int nextIdx = rand.nextInt(size - i);
			int tmp = result.get(size - i - 1);
			result.set(size - i - 1, result.get(nextIdx));
			result.set(nextIdx, tmp);
		}
		return result;
	}

}
