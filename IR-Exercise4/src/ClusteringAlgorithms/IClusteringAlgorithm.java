package ClusteringAlgorithms;

import java.util.List;

/**
 * Created by amit on 31/05/2014.
 */
public interface IClusteringAlgorithm {
    List<Cluster> GetClusters(int numberOfClusters, int maxIterations);
}
