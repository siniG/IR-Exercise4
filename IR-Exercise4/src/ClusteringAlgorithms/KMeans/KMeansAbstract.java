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

            while ( centroidsUpdated &&
                    (maxNumberOfIterations > 0))
            {
                // assign all objects to their closest cluster centroid
                AssignObjectsToClusters();

                // recalculate the centroid of each cluster
                List<ICentroid> recalculatedCentroids = RecalculateCentroids();

                // check if the centroids have changed, and act accordingly
                centroidsUpdated = CentroidsUpdated(centroids, recalculatedCentroids);

                // move the newly calculated centroids to be the actual centroids
                centroids = recalculatedCentroids;

                // decrease number of remaining iterations
                maxNumberOfIterations--;
            }
        }
        return clusters;
    }

    protected abstract List<ICentroid> InitializeCentroids();

    private void AssignObjectsToClusters()
    {
        
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
