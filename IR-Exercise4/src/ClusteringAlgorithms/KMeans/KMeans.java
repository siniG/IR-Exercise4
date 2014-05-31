package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.*;
import entities.IMatrix;

import java.util.ArrayList;
import java.util.List;

public class KMeans implements IClusteringAlgorithm
{
    private IMatrix distanceMatrix;

    public KMeans(IMatrix distanceMatrix)
    {
        this.distanceMatrix = distanceMatrix;
    }

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
        else if (distanceMatrix.getRowsNumber() != distanceMatrix.getColumnsNumber())
        {
            System.out.println("ERROR: matrix is not a perfect square. rows and columns number are different.");
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

    private List<ICentroid> GetInitialCentroids(int numberOfCentroids)
    {
        List<ICentroid> result = new ArrayList<ICentroid>(numberOfCentroids);

        return result;
    }

    private void UpdateClusters(List<ICluster> clusters, List<ICentroid> centroids)
    {

    }
}