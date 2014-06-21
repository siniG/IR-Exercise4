package ClusteringAlgorithms;

import java.util.Enumeration;
import java.util.List;

public interface IResultsWrapper {

	boolean isSameCluster(int docId1, int docId2);
	Enumeration<Integer> getMemberList();
    boolean areBetterThan(IResultsWrapper otherResultsWrapper);
    List<ICluster<Integer>> GetResults();
    double calculatePurity(IResultsWrapper otherResultsWrapper);
    double calculateAvgPurity(IResultsWrapper otherResultsWrapper);
    double calculateRandIndex(IResultsWrapper otherResultsWrapper);
}
