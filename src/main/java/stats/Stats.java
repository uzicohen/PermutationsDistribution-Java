package stats;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import general.main.AlgorithmType;
import general.main.GeneralArgs;
import general.main.GeneralArgs.GraphGeneratorParameters;

/**
 * 
 * A class to hold statistics regarding experiment
 * 
 * @author uzicohen
 *
 */
public class Stats {

	private String sectionDescription;

	private int numOfItems;

	private int numOfLabels;

	private String graph;

	private String algorithm;

	private boolean runMultiThread;

	private int numOfThreads;

	private ArrayList<String> optimizations;

	private Date startTimeDate;

	private Date endTimeDate;

	private int totalTime;

	private HashMap<Double, Double> phiToProbability;

	private boolean inferenceRunning = true;

	// Uzi's way of calling the Stats c'tor
	public Stats(GraphGeneratorParameters grapgGeneratorParameters, String graph) {
		super();
		this.sectionDescription = "Inference over graph-generator case " + grapgGeneratorParameters.graphGeneratorCase;
		this.numOfItems = grapgGeneratorParameters.numOfItems;
		this.numOfLabels = grapgGeneratorParameters.numOfLabels;
		this.runMultiThread = GeneralArgs.runMultiThread;
		this.numOfThreads = GeneralArgs.numOfThreads;
		this.graph = graph;
		this.optimizations = new ArrayList<>();
	}

	// Non probability-calculation step
	// Haoyue's way of calling the Stats c'tor for non-inference running
	public Stats(String sectionDescription) {
		super();
		this.sectionDescription = sectionDescription;
		this.runMultiThread = GeneralArgs.runMultiThread;
		this.numOfThreads = GeneralArgs.numOfThreads;
		this.optimizations = new ArrayList<>();
		this.inferenceRunning = false;
	}

	// Probability-calculation step
	// Haoyue's way of calling the Stats c'tor for inference running
	public Stats(int numOfItems, int numOfLabels, String graph) {
		super();
		this.sectionDescription = "LabeldRIM probability calculation";
		this.numOfItems = numOfItems;
		this.numOfLabels = numOfLabels;
		this.runMultiThread = GeneralArgs.runMultiThread;
		this.numOfThreads = GeneralArgs.numOfThreads;
		this.graph = graph;
		this.optimizations = new ArrayList<>();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(String.format("Section description: %s\n", sectionDescription));
		if (this.inferenceRunning) {
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
			result.append(String.format("Probabilities: %s\n", phiToProbability));
		}
		result.append(String.format("Start Time: %s\n", startTimeDate));
		result.append(String.format("End Time: %s\n", endTimeDate));
		result.append(String.format("Total Time: %s MS\n", (this.totalTime)));
		return result.toString();
	}

	public String getExperimentScenario() {
		return sectionDescription;
	}

	public void setExperimentScenario(String experimentScenario) {
		this.sectionDescription = experimentScenario;
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
		this.totalTime = new Long(endTimeDate.getTime() - startTimeDate.getTime()).intValue();
	}

	public HashMap<Double, Double> getPhiToProbability() {
		return phiToProbability;
	}

	public void setPhiToProbability(HashMap<Double, Double> phiToProbability) {
		this.phiToProbability = phiToProbability;
	}

	public int getTotalTime() {
		return this.totalTime;
	}
}
