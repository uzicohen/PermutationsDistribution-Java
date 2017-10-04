package stats;

import java.util.ArrayList;
import java.util.Date;

import general.main.AlgorithmType;
import general.main.GeneralArgs;
import general.main.GeneralArgs.ScenarioSettings;

/**
 * 
 * A class to hold statistics regarding experiment
 * 
 * @author uzicohen
 *
 */
public class Stats {

	private String experimentScenario;

	private int numOfItems;

	private int numOfLabels;

	private String graph;

	private String algorithm;

	private boolean runMultiThread;

	private int numOfThreads;

	private ArrayList<String> optimizations;

	private Date startTimeDate;

	private Date endTimeDate;

	private double probability;

	public Stats(ScenarioSettings scenarioSettings, String graph) {
		super();
		this.experimentScenario = scenarioSettings.scenario;
		this.numOfItems = scenarioSettings.numOfItems;
		this.numOfLabels = scenarioSettings.numOfLabels;
		this.runMultiThread = scenarioSettings.runMultiThread;
		this.numOfThreads = scenarioSettings.numOfThreads;
		this.graph = graph;
		this.optimizations = new ArrayList<>();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(String.format("Experiment scenraio: %s\n", experimentScenario));
		result.append(String.format("Number of items (m): %s\n", numOfItems));
		result.append(String.format("Number of labels (q): %s\n", numOfLabels));
		result.append(String.format("Graph: %s\n", graph));
		result.append(String.format("Algorithm: %s\n", algorithm));
		result.append(String.format("Run multithread: %s\n", runMultiThread));
		if (this.runMultiThread) {
			result.append(String.format("Numbr of threads: %d\n", numOfThreads));
		}
		if (GeneralArgs.currentAlgorithm == AlgorithmType.TOP_MATCHNING) {
			result.append(String.format("Optimizations: %s\n", optimizations.isEmpty() ? "None" : optimizations));
		}
		result.append(String.format("Start Time: %s\n", startTimeDate));
		result.append(String.format("End Time: %s\n", endTimeDate));
		result.append(String.format("Total Time: %s MS\n", (endTimeDate.getTime() - startTimeDate.getTime())));
		result.append(String.format("Probability: %f\n", probability));
		return result.toString();
	}

	public String getExperimentScenario() {
		return experimentScenario;
	}

	public void setExperimentScenario(String experimentScenario) {
		this.experimentScenario = experimentScenario;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public int getNumOfItems() {
		return numOfItems;
	}

	public void setNumOfItems(int numOfItems) {
		this.numOfItems = numOfItems;
	}

	public int getNumOfLabels() {
		return numOfLabels;
	}

	public void setNumOfLabels(int numOfLabels) {
		this.numOfLabels = numOfLabels;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public ArrayList<String> getOptimizations() {
		return optimizations;
	}

	public void setOptimizations(ArrayList<String> optimizations) {
		this.optimizations = optimizations;
	}

	public Date getStartTimeDate() {
		return startTimeDate;
	}

	public void setStartTimeDate(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}

	public Date getEndTimeDate() {
		return endTimeDate;
	}

	public void setEndTimeDate(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	};
}
