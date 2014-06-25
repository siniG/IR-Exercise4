package ClusteringAlgorithms;

import java.util.List;

import entities.KeyValuePair;

public interface IResultsWrapper {

    boolean areBetterThan(IResultsWrapper otherResultsWrapper);
    List<ICluster<Integer>> GetResults();
    List<KeyValuePair<Integer, Double>> calculatePurity();
    double calculateAvgPurity();
    double calculateRandIndex();
}
