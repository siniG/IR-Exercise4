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
	
	
}
