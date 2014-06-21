package utilities;

import java.util.*;

import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IResultsWrapper;
import Program.IDocumentsLoader;
import entities.IDocVector;
import entities.KeyValuePair;
import entities.RandIndexEnum;

import org.apache.commons.lang3.ArrayUtils;

public class utils
{
    public static double cosineSimilarity(float[] vectorA, float[] vectorB)
    {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += (vectorA[i] * vectorA[i]);
            normB += (vectorB[i] * vectorB[i]);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    

    public static void PrintPurity(List<KeyValuePair<Integer, Double>> purityList)
    {
    	System.out.println("Purity List:============");
    	for(KeyValuePair<Integer, Double> kvp : purityList)
    	{
    		System.out.println("Cluster id: " + kvp.getKey() + " purity: " + kvp.getValue() );
    	}
    }

    public static List<KeyValuePair<Integer, Double>> CalculatePurity(List<ICluster<Integer>> clusters, int numberOfClusters, IDocumentsLoader documentsLoader)
    {
        List<KeyValuePair<Integer, Double>> result = new LinkedList<KeyValuePair<Integer, Double>>();

        int clusterId = 0;
        for(ICluster<Integer> cluster : clusters)
        {
            clusterId++;
            int[] maxClusterSize = new int[numberOfClusters];
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
    
    public static double CalculateRandIndex(IResultsWrapper wrapper1, IResultsWrapper wrapper2)
    {
    	double result = 0.0;
    	
    	List<Integer> memberList = java.util.Collections.list(wrapper1.getMemberList());
    	
    	Integer member1, member2, counter;
    	RandIndexEnum randIndexGroup;
    	Hashtable<Integer, Integer> groupsCounterMap = new Hashtable<Integer,Integer>();
    	
    	for(int i = 0; i < memberList.size(); i++)
    	{
    		member1 =  memberList.get(i);
    		
    		for(int j = i+1; j < memberList.size(); j++)
    		{
    			member2 = memberList.get(j);
    			randIndexGroup = getRandIndexGroup(wrapper1, wrapper2, member1, member2);
    			
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
    
    public static RandIndexEnum getRandIndexGroup(IResultsWrapper wrapper1, IResultsWrapper wrapper2, int docId1, int docId2)
    {
    	RandIndexEnum result;
    	boolean isSameClusterWrapper1 = wrapper1.isSameCluster(docId1, docId2);
    	boolean isSameClusterWrapper2 = wrapper2.isSameCluster(docId1, docId2);
    	
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
