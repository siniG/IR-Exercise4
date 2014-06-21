package ClusteringAlgorithms.KMeans;

import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IResultsWrapper;
import ClusteringAlgorithms.ResultsWrapper;
import Program.IDocumentsLoader;
import entities.IDocVector;

import java.util.List;

/**
 * Created by amit on 21/06/2014.
 */
public class ImprovedKmeansPlusPlus<T> extends KmeansPlusPlus<T>
{
    IResultsWrapper bestResults;
    IDocumentsLoader documentsLoader;

    public ImprovedKmeansPlusPlus(int numberOfClusters, IDocVector documentsData, IDocumentsLoader documentsLoader)
    {
        super(numberOfClusters, documentsData);
        this.documentsLoader = documentsLoader;
    }

    public List<ICluster<Integer>> GetClusters()
    {
        // set the first results as the first execution of the algorithm.
        bestResults = new ResultsWrapper(super.GetClusters(), documentsLoader);

        // we have at least two clusters, which means we will execute at least an additional try of kmeans++.
        // in case we have more than two clusters, we might even get better results.
        for (int i = 1; i < numberOfClusters; i++)
        {
            // execute the algorithm again to get new results.
            IResultsWrapper newResults = new ResultsWrapper(super.GetClusters(), documentsLoader);
            // check the results with the ones we already have
            if (newResults.areBetterThan(bestResults))
            {
                bestResults = newResults;
            }
        }

        return bestResults.GetResults();
    }
}
