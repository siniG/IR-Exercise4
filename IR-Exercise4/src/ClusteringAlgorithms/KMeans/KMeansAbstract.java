package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.Cluster;
import ClusteringAlgorithms.ICentroid;
import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import entities.IDocVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by amit on 31/05/2014.
u */
public abstract class KMeansAbstract<T> implements IClusteringAlgorithm<T>
{
    private final int maxNumberOfIterations = 100;

    protected IDocVector documentsVectorData;
    protected List<ICluster<Integer>> clusters;
    protected int numberOfClusters;

    protected KMeansAbstract(int numberOfClusters, IDocVector documentsData)
    {
        this.documentsVectorData = documentsData;
        this.numberOfClusters = numberOfClusters;

        // initialize clustering
        if (numberOfClusters < 2)
        {
            System.out.println("ERROR: minimum number of clusters is 2.");
        }
        else
        {
            clusters = new ArrayList<ICluster<Integer>>(numberOfClusters);
            for (int i = 0; i < numberOfClusters; i++)
            {
                clusters.add(new Cluster<Integer>(documentsData));
            }
        }
    }

    /**
     * calculates which document belongs to which cluster, according to the number of given clusters.
     * @param numberOfClusters
     * @return
     */
    public List<ICluster<Integer>> GetClusters(int numberOfClusters)
    {
        // initialize centroids
        List<ICentroid> centroids = InitializeCentroids();

        AssignCentroidsToClusters(centroids);

        boolean centroidsUpdated = true;
        int iterationNumber = 1;

        while ( centroidsUpdated &&
                (iterationNumber <= maxNumberOfIterations))
        {
            System.out.println("INFO: executing iteration " + iterationNumber);

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
            if (++iterationNumber >= maxNumberOfIterations)
            {
                System.out.println("WARN: maximum number of iterations reached.");
            }
        }

        return clusters;
    }

    private void AssignCentroidsToClusters(List<ICentroid> centroids)
    {
        for (int i = 0; i < numberOfClusters; i++)
        {
            centroids.get(i).SetCluster(clusters.get(i));
        }
    }

    protected abstract List<ICentroid> InitializeCentroids();

    private void AssignObjectsToClusters(final List<ICentroid> centroids, int iteration)
    {
        // clear the clusters.
        for (int i = 0; i < numberOfClusters; i++)
        {
            clusters.get(i).Clear();
        }

        Enumeration<Integer> documentIdsEnumerator = documentsVectorData.getDocIdEnumerator();

        Collection<Integer> elems = new ArrayList<Integer>();
        while (documentIdsEnumerator.hasMoreElements())
        {
            elems.add(documentIdsEnumerator.nextElement());
        }

        /********************************/
        // multi threading baby! yeah!
        ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try
        {
            for (final Integer documentId : elems)
            {
                exec.submit(new Runnable()
                {
                    public void run()
                    {
                        // find closest centroid
                        ICentroid closestCentroid = GetClosestCentroid(documentId, centroids);

                        // assign object to the cluster of the centroid
                        closestCentroid.GetCluster().AddMember(documentId);
                    }
                });
            }
        }
        finally
        {
            exec.shutdown();
        }

        // wait for all threads to finish calculations and assignments
        try
        {
            exec.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        }
        catch (Exception ex)
        {
            System.out.println("ERROR: problem while waiting for iteration " + iteration + " to complete assignment.");
        }

        System.out.println("INFO: finished assignment iteration " + iteration + ".");
        for (int i = 0; i < numberOfClusters; i++)
        {
            System.out.println("DEBUG: cluster id " + i + " contains " + clusters.get(i).Size() + " members.");
        }
    }

    private ICentroid GetClosestCentroid(int documentId, List<ICentroid> centroids)
    {
        //randomly select the first centroid as the closest
        ICentroid result = centroids.get(0);

        float[] documentVector = documentsVectorData.getTfIdfVector(documentId);

        double closestCentroidDistance = 1 - result.GetCosineSimilarity(documentVector);

        for (int i = 1; i < numberOfClusters; i++)
        {
            double tempDistance = 1 - centroids.get(i).GetCosineSimilarity(documentVector);
            if (tempDistance < closestCentroidDistance)
            {
                closestCentroidDistance = tempDistance;
                result = centroids.get(i);
            }
        }

        return result;
    }

    private synchronized List<ICentroid> RecalculateCentroids()
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

    private synchronized boolean CentroidsUpdated(List<ICentroid> centroidsBeforeCalculation, List<ICentroid> centroidsAfterCalculation)
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
