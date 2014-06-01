package utilities;

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
	
	public static double[] floatArrayToDoubleArry(float[] array)
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
	
	
}
