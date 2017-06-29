package topmatching;

import java.util.HashMap;
import java.util.HashSet;

public class TopProbArgs {

	public HashMap<String, String> gamma;

	public HashSet<String> imgGamma;

	public HashMap<String, HashSet<String>> sigmaToGammaValueMap;

	public TopProbArgs(HashMap<String, String> gamma) {
		this.gamma = gamma;
		this.imgGamma = new HashSet<>(gamma.values());
		this.sigmaToGammaValueMap = new HashMap<>();
		for (String label : gamma.keySet()) {
			String sigma = gamma.get(label);
			HashSet<String> currentValues = this.sigmaToGammaValueMap.containsKey(sigma)
					? this.sigmaToGammaValueMap.get(sigma) : new HashSet<>();
			currentValues.add(label);
			this.sigmaToGammaValueMap.put(sigma, currentValues);
		}
	}

	public HashMap<String, String> getGamma() {
		return gamma;
	}

	public void setGamma(HashMap<String, String> gamma) {
		this.gamma = gamma;
	}

	public HashSet<String> getImgGamma() {
		return imgGamma;
	}

	public void setImgGamma(HashSet<String> imgGamma) {
		this.imgGamma = imgGamma;
	}

	public HashMap<String, HashSet<String>> getSigmaToGammaValueMap() {
		return sigmaToGammaValueMap;
	}

	public void setSigmaToGammaValueMap(HashMap<String, HashSet<String>> sigmaToGammaValueMap) {
		this.sigmaToGammaValueMap = sigmaToGammaValueMap;
	}	
}
