package utilities;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.RealVector;

public class utils {

	public static double CalculateHypotenuseFromOrigin(double cosineAngle, double adjacentDistance)
	{
		
		double result;
		
		if(cosineAngle == 0.0 || Double.isNaN(cosineAngle)|| Double.isNaN(adjacentDistance))
		{
			result = Double.NaN;
		}
		else
		{
			result = adjacentDistance / cosineAngle;
		}
		
		return result;
	}
	
	public static double[] floatArrayToDoubleArray(float[] array)
	{
		if(array == null)
			return null;
		
		double[] dArray = new double[array.length];
		
		for(int i = 0; i<array.length; i++)
		{
			dArray[i] = array[i];
		}
		
		return dArray;
	}

    public static double cosineSimilarity(double[] vectorA, double[] vectorB)
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
}
