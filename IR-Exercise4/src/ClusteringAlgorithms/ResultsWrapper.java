package ClusteringAlgorithms;

import Program.IDocumentsLoader;
import entities.KeyValuePair;
import org.apache.pdfbox.pdmodel.graphics.predictor.Average;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import entities.KeyValuePair;

public class ResultsWrapper implements IResultsWrapper {
	
	private Hashtable<Integer, Integer> documentClusterIdByDocumentId;
	private List<ICluster<Integer>> originalClusters;
    private IDocumentsLoader documentsLoader;

	public ResultsWrapper(List<ICluster<Integer>> clusters, IDocumentsLoader documentsLoader)
	{
		this.documentClusterIdByDocumentId = clusterListToHash(clusters);
        this.originalClusters = clusters;
        this.documentsLoader = documentsLoader;
	}

    public boolean areBetterThan(IResultsWrapper otherResultsWrapper)
    {
        boolean result = true;

        double thisAveragePurity = this.calculateAvgPurity();
        double thisRandIndex = this.calculateRandIndex();

        double otherAveragePurity = otherResultsWrapper.calculateAvgPurity();
        double otherRandIndex = otherResultsWrapper.calculateRandIndex();

        // this is the core of the comparison between the two result clusters.
        // this comparison can be changed if a boost is needed to one of the parameters.
        if ((otherAveragePurity + otherRandIndex) > (thisAveragePurity + thisRandIndex))
        {
            result = false;
        }

        return result;
    }

    public List<ICluster<Integer>> GetResults()
    {
        return originalClusters;
    }

    private Hashtable<Integer, Integer> clusterListToHash(List<ICluster<Integer>> clusters)
	{
		Hashtable<Integer, Integer> hashTable = new Hashtable<Integer, Integer>();
		
		for(int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex ++)
		{
			ICluster<Integer> cluster = clusters.get(clusterIndex);
			Enumeration<Integer> members = cluster.GetMemberIds();
			
			while(members.hasMoreElements())
			{
				hashTable.put(members.nextElement(), clusterIndex);
			}
		}
		
		return hashTable;
	}
	
	public boolean isSameCluster(int docId1, int docId2)
	{
		boolean result = false;
		
		Integer doc1Cluster = this.documentClusterIdByDocumentId.get(docId1);
		Integer doc2Cluster = this.documentClusterIdByDocumentId.get(docId2);
		
		if(doc1Cluster == null || doc2Cluster == null)
		{
			throw new IllegalArgumentException("Error: one of given doc Ids does not exist.ids=" + docId1 + ", " + docId2);
		}
		else if(doc1Cluster == doc2Cluster)
		{
			result = true;
		}
		
		
		return result;
	}
	
	public Enumeration<Integer> getMemberList()
	{
		return this.documentClusterIdByDocumentId.keys();
	}
	
    public List<KeyValuePair<Integer, Double>> calculatePurity()
    {
    	 List<KeyValuePair<Integer, Double>> result = new LinkedList<KeyValuePair<Integer, Double>>();

         int clusterId = 0;
         for(ICluster<Integer> cluster : this.originalClusters)
         {
             clusterId++;
             int[] maxClusterSize = new int[this.originalClusters.size()];
             Enumeration<Integer> members = cluster.GetMemberIds();

             while(members.hasMoreElements())
             {
                 Integer memberId = members.nextElement();
                 int trueDocCluster = documentsLoader.GetDocumentCluster(memberId);
                 maxClusterSize[trueDocCluster - 1]++;
             }

             double purity = (1.0/cluster.Size()) * Collections.max(Arrays.asList(ArrayUtils.toObject(maxClusterSize)));

             result.add(new KeyValuePair<Integer, Double>(clusterId, purity));
         }
         
         return result;
    }
    public double calculateAvgPurity()
    {
    	return 0.0;
    }
    public double calculateRandIndex()
    {
    	return 0.0;
    }

}
