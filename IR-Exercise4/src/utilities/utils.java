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
}
