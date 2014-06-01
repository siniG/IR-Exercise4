package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.Cluster;
import ClusteringAlgorithms.ICentroid;
import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import entities.IMatrix;

import java.util.ArrayList;
import java.util.List;

import utilities.utils;

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
    public List<ICluster<Integer>> GetClusters(int numberOfClusters, int maxIterations)
    {
        // initialize clustering
        List<ICluster<Integer>> result = new ArrayList<ICluster<Integer>>(numberOfClusters);

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
                ICluster<Integer> initialClusterWithCentroid = new Cluster<Integer>(initialCentroidForCluster);
                result.set(k, initialClusterWithCentroid);
            }

            // loop until done
            while ((maxIterations > 0) &&
                    (clusterChangesOccured))
            {
                // no cluster change is done by default.
                clusterChangesOccured = false;

                // find the closest centroid to each document vector.
                for (int documentId = 1; documentId <= distanceMatrix.getRowsNumber() + 1; documentId++)
                {
                    // set the default distance to the cluster.
                    double closestClusterDistance = Double.MAX_VALUE;

                    // choose the first cluster as the closest cluster at random.
                    ICluster<Integer> closestCluster = result.get(0);

                    // get the document coordinates for the calculations.
                    float[] currentDocumentVector = distanceMatrix.getRow(documentId);

                    // find which cluster is the closest one.
                    for (ICluster<Integer> currentCluster : result)
                    {
                        double distanceBetweenDocumentAndCluster = currentCluster.GetCentroid().GetDistance(utils.floatArrayToDoubleArry(currentDocumentVector));
                        if (distanceBetweenDocumentAndCluster < closestClusterDistance)
                        {
                            closestClusterDistance = distanceBetweenDocumentAndCluster;
                            closestCluster = currentCluster;
                        }
                    }
                    closestCluster.AddMember(documentId);

                }

                // update clustering based on new means
                UpdateClusters(result, centroids);

                // reduce number of iterations to stop this crazy loop.
                maxIterations--;
            }
        }
        return result;
    }

    protected abstract List<ICentroid> GetInitialCentroids(int numberOfCentroids);

    protected void UpdateClusters(List<ICluster<Integer>> clusters, List<ICentroid> centroids)
    {

    }
}
