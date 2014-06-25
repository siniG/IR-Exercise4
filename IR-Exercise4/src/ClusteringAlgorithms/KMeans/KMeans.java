package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.*;
import entities.IDocVector;

import java.util.ArrayList;
import java.util.List;

public class KMeans<T> extends KMeansAbstract implements IClusteringAlgorithm
{
    public KMeans(int numberOfClusters, IDocVector documentsData)
    {
        super(numberOfClusters, documentsData);
    }

    /**
     * simple pick of the first documents as seed centroids. no calculations are done.
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

            // update the clusters structure
            result.add(newCentroid);
        }
        return result;
    }
}