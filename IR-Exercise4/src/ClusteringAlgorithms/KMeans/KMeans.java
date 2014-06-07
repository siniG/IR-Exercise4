package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.*;
import entities.IDocVector;
import utilities.utils;

import java.util.ArrayList;
import java.util.List;

public class KMeans<T> extends KMeansAbstract implements IClusteringAlgorithm
{
    public KMeans(int numberOfClusters, IDocVector documentsData, int maxNumberOfIterations)
    {
        super(numberOfClusters, documentsData, maxNumberOfIterations);
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
            int indexOfDocumentId = documentsVectorData.getNumberOfDocs() / numberOfClusters * i;
            int documentId = documentsVectorData.getDocIdAtIndex(indexOfDocumentId);
            float [] randomCentroidCoordinates = documentsVectorData.getTfIdfVector(documentId);
            ICentroid newCentroid = new Centroid(randomCentroidCoordinates);
            ICluster<Integer> newCluster = new Cluster<Integer>(newCentroid, documentsVectorData);

            // add some initial documents in the cluster.
            newCluster.AddMember(documentId);

            // update the clusters structure
            clusters.add(newCluster);
            result.add(newCentroid);
        }
        return result;
    }
}