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
u */
public abstract class KMeansAbstract<T> implements IClusteringAlgorithm<T>
{
    protected IMatrix distanceMatrix;
    protected List<ICluster<Integer>> clusters;
    protected int numberOfClusters;
    protected int maxNumberOfIterations;

    protected KMeansAbstract(int maxNumberOfIterations)
    {
        this.maxNumberOfIterations = maxNumberOfIterations;
    }

    /**
     * calculates which document belongs to which cluster, according to the number of given clusters.
     * @param numberOfClusters
     * @return
     */
    public List<ICluster<Integer>> GetClusters(int numberOfClusters)
    {
        // initialize clustering
        if (numberOfClusters < 2)
        {
            System.out.println("ERROR: minimum number of clusters is 2.");
        }
        else
        {
            clusters = new ArrayList<ICluster<Integer>>(numberOfClusters);

            // initialize centroids
            List<ICentroid> centroids = InitializeCentroids();

            boolean centroidsUpdated = true;
            int iterationNumber = 1;

            while ( centroidsUpdated &&
                    (iterationNumber <= maxNumberOfIterations))
            {
                System.out.println("INFO: iterations left = " + (maxNumberOfIterations - iterationNumber));
                // assign all objects to their closest cluster centroid
                AssignObjectsToClusters(centroids, iterationNumber);

                // recalculate the centroid of each cluster
                List<ICentroid> recalculatedCentroids = RecalculateCentroids();

                // check if the centroids have changed, and act accordingly
                centroidsUpdated = CentroidsUpdated(centroids, recalculatedCentroids);
                System.out.println("INFO: centroids updated = " + centroidsUpdated);

                // move the newly calculated centroids to be the actual centroids
                centroids = recalculatedCentroids;

                // update number of remaining iterations
                iterationNumber++;
            }
        }
        return clusters;
    }

    protected abstract List<ICentroid> InitializeCentroids();

    private void AssignObjectsToClusters(List<ICentroid> centroids, int iteration)
    {
        // clear the clusters.
        for (int i = 0; i < numberOfClusters; i++)
        {
            clusters.get(i).Clear();
        }

        // re-populate the clusters. assign each document to the closest centroid available.
        for (int documentId = 0; documentId < distanceMatrix.getNumberOfDocs(); documentId++)
        {
            // find closest centroid
            int closestCentroid = GetClosestCentroid(documentId, centroids);

            // assign object to the cluster of the centroid
            clusters.get(closestCentroid).AddMember(documentId);
            //System.out.println("DEBUG: iteration " + iteration + ", assigned document id " + documentId + " to cluster " + closestCentroid);
        }

        System.out.println("INFO: finished assignment iteration " + iteration + ".");
        for (int i = 0; i < numberOfClusters; i++)
        {
            System.out.println("DEBUG: cluster id " + i + " contains " + clusters.get(i).Size() + " members.");
        }
    }

    private int GetClosestCentroid(int documentId, List<ICentroid> centroids)
    {
        int result = 0;

        float[] documentVector = distanceMatrix.getTfIdfVector(documentId);

        double closestCentroidDistance = centroids.get(result).GetDistance(documentVector);

        for (int i = 1; i < numberOfClusters; i++)
        {
            double tempDistance = centroids.get(i).GetDistance(documentVector);
            if (tempDistance < closestCentroidDistance)
            {
                closestCentroidDistance = tempDistance;
                result = i;
            }
        }

        return result;
    }

    private List<ICentroid> RecalculateCentroids()
    {
        List<ICentroid> result = new ArrayList<ICentroid>();

        for (int i = 0; i < clusters.size(); i++)
        {
            ICluster currentCluster = clusters.get(i);
            ICentroid currentCentroid = currentCluster.GetCentroid();
            result.add(currentCentroid);
        }
        return result;
    }

    private boolean CentroidsUpdated(List<ICentroid> centroidsBeforeCalculation, List<ICentroid> centroidsAfterCalculation)
    {
        boolean result = false;

        // check the number of centroids in both lists is the same
        if (centroidsBeforeCalculation.size() == centroidsAfterCalculation.size())
        {
            // iterate all the first list centroids
            for (int i = 0; i < centroidsBeforeCalculation.size(); i++)
            {
                ICentroid centroidBeforeCalculation = centroidsBeforeCalculation.get(i);

                // iterate all second list centroids
                for (int j = 0; j < centroidsAfterCalculation.size(); j++)
                {
                    ICentroid centroidAfterCalculation = centroidsAfterCalculation.get(i);

                    if (!centroidBeforeCalculation.equals(centroidAfterCalculation))
                    {
                        result = true;
                        break;
                    }
                }

                // no need to continue if an updated centroid was found.
                if (result)
                {
                    break;
                }
            }
        }

        return result;
    }
}
