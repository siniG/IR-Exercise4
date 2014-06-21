package ClusteringAlgorithms;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ResultsWrapper implements IResultsWrapper {
	
	private Hashtable<Integer, Integer> documentClusterIdByDocumentId;
	private List<ICluster<Integer>> originalClusters;

	public ResultsWrapper(List<ICluster<Integer>> clusters)
	{
		this.documentClusterIdByDocumentId = clusterListToHash(clusters);
        this.originalClusters = clusters;
	}

	public ResultsWrapper(Hashtable<Integer, Integer> documentClusters)
	{
		this.documentClusterIdByDocumentId = documentClusters;
	}

    public boolean areBetterThan(IResultsWrapper otherResultsWrapper)
    {
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
}
