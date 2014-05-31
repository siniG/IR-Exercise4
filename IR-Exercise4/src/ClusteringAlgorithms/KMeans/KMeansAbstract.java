package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.Cluster;
import ClusteringAlgorithms.ICentroid;
import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import entities.IMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 31/05/2014.
 */
public abstract class KMeansAbstract<T> implements IClusteringAlgorithm<T>
{
    protected IMatrix distanceMatrix;

    /**
     * calculates which document belongs to which cluster, according to the number of given clusters.
     * @param numberOfClusters
     * @param maxIterations
     * @return
     */
    public List<ICluster<T>> GetClusters(int numberOfClusters, int maxIterations)
    {
        // initialize clustering
        List<ICluster<T>> result = new ArrayList<ICluster<T>>(numberOfClusters);

        if (numberOfClusters < 2)
        {
            System.out.println("ERROR: minimum number of clusters is 2.");
        }
        else
        {
            boolean clusterChangesOccured = false;

            // initialize first centroids
            List<ICentroid> centroids = GetInitialCentroids(numberOfClusters);
            for (int k = 0; k < numberOfClusters ; k++)
            {
                ICentroid initialCentroidForCluster = centroids.get(k);
                ICluster<T> initialClusterWithCentroid = new Cluster<T>(initialCentroidForCluster);
                result.set(k, initialClusterWithCentroid);
            }

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

    protected void UpdateClusters(List<ICluster<T>> clusters, List<ICentroid> centroids)
    {

    }
}
