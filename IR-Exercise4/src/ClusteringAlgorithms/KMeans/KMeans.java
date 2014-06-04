package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.Centroid;
import ClusteringAlgorithms.ICentroid;
import ClusteringAlgorithms.IClusteringAlgorithm;
import entities.IMatrix;
import utilities.utils;

import java.util.ArrayList;
import java.util.List;

public class KMeans<T> extends KMeansAbstract implements IClusteringAlgorithm
{
    public KMeans(int numberOfClusters, IMatrix distanceMatrix)
    {
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
            ICentroid newCentroid = new Centroid(utils.floatArrayToDoubleArry(distanceMatrix.getRow(i + 1)));

            clusters.set(i, newCentroid);
            result.add(newCentroid);
        }
        return result;
    }
}