package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.Cluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import entities.IMatrix;

import java.util.ArrayList;
import java.util.List;

public class KMeans implements IClusteringAlgorithm
{
    private IMatrix matrix;

    public KMeans(IMatrix matrix)
    {
        this.matrix = matrix;
    }

    /**
     * calculates which document belongs to which cluster, according to the number of given clusters.
     * @param numberOfClusters
     * @param maxIterations
     * @return
     */
    public List<Cluster> GetClusters(int numberOfClusters, int maxIterations)
        {
        // initialize clustering
        ArrayList<Cluster> result = new ArrayList<Cluster>(numberOfClusters);

        if (numberOfClusters < 2)
        {
            System.out.println("ERROR: minimum number of clusters is 2.");
        }
        else
        {
            boolean clusterChangesOccured = false;

            // loop until done
            while ((maxIterations > 0) &&
                    (clusterChangesOccured))
            {
                // no cluster change is done by default.
                clusterChangesOccured = false;

                // compute mean of each cluster

                // update clustering based on new means
            }

        }
        return result;
    }
}