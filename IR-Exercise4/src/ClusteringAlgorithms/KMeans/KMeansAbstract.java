package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.ICentroid;
import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import entities.IMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 31/05/2014.
 */
public abstract class KMeansAbstract implements IClusteringAlgorithm
{
    protected IMatrix distanceMatrix;

    /**
     * calculates which document belongs to which cluster, according to the number of given clusters.
     * @param numberOfClusters
     * @param maxIterations
     * @return
     */
    public List<ICluster> GetClusters(int numberOfClusters, int maxIterations)
    {
        // initialize clustering
        List<ICluster> result = new ArrayList<ICluster>(numberOfClusters);

        if (numberOfClusters < 2)
        {
            System.out.println("ERROR: minimum number of clusters is 2.");
        }
        else
        {
            boolean clusterChangesOccured = false;

            // initialize first centroids
            List<ICentroid> centroids = GetInitialCentroids(numberOfClusters);

            // loop until done
            while ((maxIterations > 0) &&
                    (clusterChangesOccured))
            {
                // no cluster change is done by default.
                clusterChangesOccured = false;

                // compute mean of each cluster

                // update clustering based on new means
                UpdateClusters(result, centroids);
            }

        }
        return result;
    }

    protected abstract List<ICentroid> GetInitialCentroids(int numberOfCentroids);

    protected abstract void UpdateClusters(List<ICluster> clusters, List<ICentroid> centroids);
}
