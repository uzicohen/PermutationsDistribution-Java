package general.main;

public enum AlgorithmType {
	// Original algorithm is TOP_MATCHING and runs with O(m^2q) = O(m^q X m^q)
	// New improved algorithm is LIFTED_TOP_MATCHING and runs with O(m^q X 2^q)	
	BRUTE_FORCE, SAMPLED, TOP_MATCHING, LIFTED_TOP_MATCHING
}
