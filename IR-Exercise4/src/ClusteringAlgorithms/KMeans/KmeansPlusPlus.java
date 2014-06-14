package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.Centroid;
import ClusteringAlgorithms.ICentroid;
import entities.IDocVector;

import java.util.*;

/**
 * Created by amit on 07/06/2014.
 */
public class KmeansPlusPlus<T> extends KMeansAbstract<Integer>
{
    public KmeansPlusPlus(int numberOfClusters, IDocVector documentsData)
    {
        super(numberOfClusters, documentsData);
    }

    protected List<ICentroid> InitializeCentroids()
    {
        List<ICentroid> result = new ArrayList<ICentroid>();

        // create the first centroid manually;
        int docIdAtFirstIndex = documentsVectorData.getDocIdAtIndex(0);
        float[] firstDocumentVector = documentsVectorData.getTfIdfVector(docIdAtFirstIndex);
        ICentroid firstCentroid = new Centroid(firstDocumentVector);
        result.add(firstCentroid);

        // mark the used document ids
        List<Integer> usedDocumentsIds = new ArrayList<Integer>();
        usedDocumentsIds.add(docIdAtFirstIndex);

        final Map<Integer, Double> minimumSquaredDistance = new HashMap<Integer, Double>();

        // add the rest of the centroids.
        // (this is a while loop since not all iterations are guaranteed to add a new centroid)
        while (result.size() < numberOfClusters)
        {
            result.add(GetNextCentroid(result, usedDocumentsIds, minimumSquaredDistance));
        }

        return result;
    }

    private ICentroid GetNextCentroid(List<ICentroid> alreadyDefinedCentroids, List<Integer> usedDocumentIds, Map<Integer, Double> minimumSquaredDistance)
    {
        ICentroid result = null;

        // initialize the distance of the first centroid from all the rest of the documents
        for (int i = 0; i < documentsVectorData.getNumberOfDocs(); i++)
        {
            // skip the documents already in use by the chosen centroids
            if (!usedDocumentIds.contains(documentsVectorData.getDocIdAtIndex(i)))
            {
                // calculate the distance of the document.
                int currentIndexDocumentId = documentsVectorData.getDocIdAtIndex(i);
                float [] currentIndexDocumentVector = documentsVectorData.getTfIdfVector(currentIndexDocumentId);
                double distance = alreadyDefinedCentroids.get(0).GetCosineSimilarity(currentIndexDocumentVector);
                minimumSquaredDistance.put(documentsVectorData.getDocIdAtIndex(i), distance*distance);
            }
        }

        // sum of all squared distances
        double squaredDistancesSum = 0.0;
        for (int i = 0; i < documentsVectorData.getNumberOfDocs(); i++)
        {
            int currentCheckedDocumentId = documentsVectorData.getDocIdAtIndex(i);
            if (!usedDocumentIds.contains(currentCheckedDocumentId))
            {
                squaredDistancesSum += minimumSquaredDistance.get(currentCheckedDocumentId);
            }
        }

        // set a random distance sum
        Random random = new Random();
        final double newCenterMinimumDistance = random.nextDouble() * squaredDistancesSum;

        // set the next document id as an invalid id.
        int nextDocumentIdToUse = -1;

        // iterate through the summed distances until we get to a distance which is far enough.
        // then we get the document id it relates to.
        double sum = 0.0;
        for (int i = 0; i < documentsVectorData.getNumberOfDocs(); i++)
        {
            int currentIndexDocumentId = documentsVectorData.getDocIdAtIndex(i);
            // make sure we don't count the already used document ids.
            if (!usedDocumentIds.contains(currentIndexDocumentId))
            {
                sum += minimumSquaredDistance.get(currentIndexDocumentId);
                if (sum > newCenterMinimumDistance)
                {
                    nextDocumentIdToUse = currentIndexDocumentId;
                    break;
                }
            }
        }

        // check we actually got some document id.
        // if we didn't get any document id, take the last point not already in use
        if (nextDocumentIdToUse == -1)
        {
            for (int i = documentsVectorData.getNumberOfDocs() - 1; i >= 0; i--)
            {
                int currentIndexDocumentId = documentsVectorData.getDocIdAtIndex(i);
                if (!usedDocumentIds.contains(currentIndexDocumentId))
                {
                    nextDocumentIdToUse = currentIndexDocumentId;
                    break;
                }
            }
        }

        // we have a document id to use!
        if (nextDocumentIdToUse > -1)
        {
            // create the new centroid to be used!
            result = new Centroid(documentsVectorData.getTfIdfVector(nextDocumentIdToUse));

            // add the document to the used document ids so we don't use it again.
            usedDocumentIds.add(nextDocumentIdToUse);

            // update the new squared distances. only need to compute with new centroid coordinates.
            for (int i = 0; i < documentsVectorData.getNumberOfDocs(); i++)
            {
                int currentIndexDocumentId = documentsVectorData.getDocIdAtIndex(i);
                if (!usedDocumentIds.contains(currentIndexDocumentId))
                {
                    // check if the distance of the new centroid is closer to the document.
                    double distanceToCheck = result.GetCosineSimilarity(documentsVectorData.getTfIdfVector(currentIndexDocumentId));
                    double squaredDistanceToCheck = distanceToCheck * distanceToCheck;
                    if (squaredDistanceToCheck < minimumSquaredDistance.get(currentIndexDocumentId))
                    {
                        // the new distance is less than the previous calculated distance.
                        minimumSquaredDistance.put(currentIndexDocumentId, squaredDistanceToCheck);
                    }
                }
            }
        }
        else
        {
            System.out.println("ERROR: Cannot find a point to act as the next cluster centroid vector.");
        }

        return result;
    }
}
