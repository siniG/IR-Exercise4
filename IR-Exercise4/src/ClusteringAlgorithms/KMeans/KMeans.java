package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.*;
import entities.IMatrix;

import java.util.ArrayList;
import java.util.List;

import utilities.utils;

public class KMeans<T> extends KMeansAbstract implements IClusteringAlgorithm
{
    public KMeans(IMatrix distanceMatrix)
    {
        this.distanceMatrix = distanceMatrix;
    }

    /**
     * simple pick of the first documents as seed centroids. no calculations are done.
     * @param numberOfCentroids
     * @return
     */
    protected List<ICentroid> GetInitialCentroids(int numberOfCentroids)
    {
        List<ICentroid> result = new ArrayList<ICentroid>(numberOfCentroids);

        for (int i = 1; i < numberOfCentroids + 1; i++)
        {
            ICentroid newCentroid = new Centroid(utils.floatArrayToDoubleArry(distanceMatrix.getRow(i)));
            result.add(newCentroid);
        }

        return result;
    }
}