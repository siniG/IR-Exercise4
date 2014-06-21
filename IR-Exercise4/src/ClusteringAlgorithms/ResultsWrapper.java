package ClusteringAlgorithms;

import Program.IDocumentsLoader;
import entities.KeyValuePair;
import entities.RandIndexEnum;

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

import com.google.common.collect.Lists;

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
	
	private boolean isSameCluster(Hashtable<Integer, Integer> table, int docId1, int docId2)
	{
		boolean result = false;
		
		Integer doc1Cluster = table.get(docId1);
		Integer doc2Cluster = table.get(docId2);
		
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

    	double result = 0.0;
    	
    	List<Integer> memberList = Lists.newArrayList(this.documentsLoader.GetDocumentIterator());
    	
    	Integer member1, member2, counter;
    	RandIndexEnum randIndexGroup;
    	Hashtable<Integer, Integer> groupsCounterMap = new Hashtable<Integer,Integer>();
    	
    	for(int i = 0; i < memberList.size(); i++)
    	{
    		member1 =  memberList.get(i);
    		
    		for(int j = i+1; j < memberList.size(); j++)
    		{
    			member2 = memberList.get(j);
    			randIndexGroup = getRandIndexGroup(member1, member2);
    			
    			counter = groupsCounterMap.get(randIndexGroup.ordinal());
    			counter = (counter == null) ? 1 : counter + 1;
    			
    			groupsCounterMap.put(randIndexGroup.ordinal(), counter);
    		}
    	}
    	
    	double bothInSameClusters = (groupsCounterMap.get(RandIndexEnum.SameInBothClusters.ordinal()) == null) ? 0  : groupsCounterMap.get(RandIndexEnum.SameInBothClusters.ordinal());
    	double bothInDifferentClusters = (groupsCounterMap.get(RandIndexEnum.DifferentInBothClusters.ordinal()) == null) ? 0  : groupsCounterMap.get(RandIndexEnum.DifferentInBothClusters.ordinal());
    	double disSimilarClusters = (groupsCounterMap.get(RandIndexEnum.SameInOneButDiffInOther.ordinal()) == null) ? 0  : groupsCounterMap.get(RandIndexEnum.SameInOneButDiffInOther.ordinal());
    	
    	double temp = bothInSameClusters + bothInDifferentClusters;
    	result = temp / (temp + disSimilarClusters);
    	
    	return result;
    }
    
    private RandIndexEnum getRandIndexGroup(int docId1, int docId2)
    {
    	RandIndexEnum result;
    	boolean isSameClusterWrapper1 = isSameCluster(this.documentsLoader.getDocumentClusterIdsByDocumentId(), docId1, docId2);
    	boolean isSameClusterWrapper2 = isSameCluster(this.documentClusterIdByDocumentId, docId1, docId2);
    	
    	if(isSameClusterWrapper1 && isSameClusterWrapper2)
    	{
    		result = RandIndexEnum.SameInBothClusters;
    	}
    	else if(!isSameClusterWrapper1 && !isSameClusterWrapper2)
    	{
    		result = RandIndexEnum.DifferentInBothClusters;
    	}
    	else
    	{
    		result = RandIndexEnum.SameInOneButDiffInOther;
    	}
    	
    	return result;
    }

}
