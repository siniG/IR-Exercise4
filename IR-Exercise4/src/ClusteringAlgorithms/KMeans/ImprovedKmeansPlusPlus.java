package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IResultsWrapper;
import ClusteringAlgorithms.ResultsWrapper;
import entities.IDocVector;

import java.util.List;

/**
 * Created by amit on 21/06/2014.
 */
public class ImprovedKmeansPlusPlus<T> extends KmeansPlusPlus<T>
{
    IResultsWrapper bestResults;

    public ImprovedKmeansPlusPlus(int numberOfClusters, IDocVector documentsData)
    {
        super(numberOfClusters, documentsData);
    }

    public List<ICluster<Integer>> GetClusters()
    {
        // set the first results as the first execution of the algorithm.
        bestResults = new ResultsWrapper(super.GetClusters());

        // we have at least two clusters, which means we will execute at least an additional try of kmeans++.
        // in case we have more than two clusters, we might even get better results.
        for (int i = 0; i < numberOfClusters; i++)
        {
            // execute the algorithm again to get new results.
            IResultsWrapper newResults = new ResultsWrapper(this.GetClusters());
            // check the results with the ones we already have
            if (newResults.areBetterThan(bestResults))
            {
                bestResults = newResults;
            }
        }

        return bestResults.GetResults();
    }
}
