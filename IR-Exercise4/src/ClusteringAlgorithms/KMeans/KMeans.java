package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.*;
import entities.IMatrix;
import utilities.utils;

import java.util.ArrayList;
import java.util.List;

public class KMeans<T> extends KMeansAbstract implements IClusteringAlgorithm
{
    public KMeans(int numberOfClusters, IMatrix distanceMatrix, int maxNumberOfIterations)
    {
        super(maxNumberOfIterations);
        this.distanceMatrix = distanceMatrix;
        this.numberOfClusters = numberOfClusters;
    }

    /**
     * simple pick of the first documents as seed centroids. no calculations are done.
     * @return
     */
    protected List<ICentroid> InitializeCentroids()
    {
        List<ICentroid> result  = new ArrayList<ICentroid>();

        for (int i = 0; i < numberOfClusters; i++)
        {
            ICentroid newCentroid = new Centroid(utils.floatArrayToDoubleArray(distanceMatrix.getTfIdfVector(i)));
            ICluster<Integer> newCluster = new Cluster<Integer>(newCentroid, distanceMatrix);
            clusters.add(newCluster);
            result.add(newCentroid);
        }
        return result;
    }
}