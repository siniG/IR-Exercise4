package ClusteringAlgorithms;

import Program.IDocumentsLoader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

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

	public ResultsWrapper(Hashtable<Integer, Integer> documentClusters)
	{
		this.documentClusterIdByDocumentId = documentClusters;
	}

    public boolean areBetterThan(IResultsWrapper otherResultsWrapper)
    {
        utils.CalculatePurity(originalClusters, originalClusters.size(), documentsLoader);

        throw new NotImplementedException();
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
	
    public double calculatePurity(IResultsWrapper otherResultsWrapper)
    {
    	return 0.0;
    }
    public double calculateAvgPurity(IResultsWrapper otherResultsWrapper)
    {
    	return 0.0;
    }
    public double calculateRandIndex(IResultsWrapper otherResultsWrapper)
    {
    	return 0.0;
    }

}
