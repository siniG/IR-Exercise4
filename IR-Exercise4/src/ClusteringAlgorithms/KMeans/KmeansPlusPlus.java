package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.ICentroid;
import entities.IDocVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 07/06/2014.
 */
public class KmeansPlusPlus extends KMeansAbstract<Integer>
{
    public KmeansPlusPlus(int numberOfClusters, IDocVector documentsData, int maxNumberOfIterations)
    {
        super(numberOfClusters, documentsData, maxNumberOfIterations);
    }

    protected List<ICentroid> InitializeCentroids()
    {
        List<ICentroid> result  = new ArrayList<ICentroid>();



        return result;
    }
}
