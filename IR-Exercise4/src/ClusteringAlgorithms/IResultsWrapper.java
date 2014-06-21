package ClusteringAlgorithms;

import java.util.Enumeration;

public interface IResultsWrapper {

	boolean isSameCluster(int docId1, int docId2);
	Enumeration<Integer> getMemberList();
}
