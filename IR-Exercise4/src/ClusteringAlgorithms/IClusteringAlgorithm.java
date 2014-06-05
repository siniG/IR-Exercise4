package ClusteringAlgorithms;

import java.util.List;

/**
 * Created by amit on 31/05/2014.
 */
public interface IClusteringAlgorithm<T>
{
    List<ICluster<Integer>> GetClusters(int numberOfClusters);
}
