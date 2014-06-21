package ClusteringAlgorithms;

import java.util.Enumeration;
import java.util.List;

import entities.KeyValuePair;

public interface IResultsWrapper {

	boolean isSameCluster(int docId1, int docId2);
	Enumeration<Integer> getMemberList();
    boolean areBetterThan(IResultsWrapper otherResultsWrapper);
    List<ICluster<Integer>> GetResults();
    List<KeyValuePair<Integer, Double>> calculatePurity();
    double calculateAvgPurity();
    double calculateRandIndex();
}
