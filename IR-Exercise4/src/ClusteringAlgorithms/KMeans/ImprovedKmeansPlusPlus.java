package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.ICluster;
import entities.IDocVector;

import java.util.List;

/**
 * Created by amit on 21/06/2014.
 */
public class ImprovedKmeansPlusPlus<T> extends KmeansPlusPlus<T>
{
    List<ICluster<Integer>> bestResults;
    
    public ImprovedKmeansPlusPlus(int numberOfClusters, IDocVector documentsData)
    {
        super(numberOfClusters, documentsData);
    }

    public List<ICluster<Integer>> GetClusters()
    {
        return super.GetClusters();
    }
}
