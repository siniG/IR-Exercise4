package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.*;
import entities.IDocVector;
import utilities.utils;

import java.util.ArrayList;
import java.util.List;

public class KMeans<T> extends KMeansAbstract implements IClusteringAlgorithm
{
    public KMeans(int numberOfClusters, IDocVector distanceMatrix, int maxNumberOfIterations)
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
            int indexOfDocumentId = distanceMatrix.getNumberOfDocs() / numberOfClusters * i;
            int documentId = distanceMatrix.getDocIdAtIndex(indexOfDocumentId);
            double [] randomCentroidCoordinates = utils.floatArrayToDoubleArray(distanceMatrix.getTfIdfVector(documentId));
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