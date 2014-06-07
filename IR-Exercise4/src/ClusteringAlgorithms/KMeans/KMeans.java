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
            int documentId = distanceMatrix.getRowsNumber() / numberOfClusters * i;
            double [] randomCentroidCoordinates = utils.floatArrayToDoubleArray(distanceMatrix.getRow(documentId));
            ICentroid newCentroid = new Centroid(randomCentroidCoordinates);
            ICluster<Integer> newCluster = new Cluster<Integer>(newCentroid, distanceMatrix);

            // add some initial documents in the cluster.
            newCluster.AddMember(documentId);

            // update the clusters structure
            clusters.add(newCluster);
            result.add(newCentroid);
        }
        return result;
    }
}