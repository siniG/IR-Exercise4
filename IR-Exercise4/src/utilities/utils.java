package utilities;

import java.lang.Math;

public class utils {

	public static double GetOppositeByTangent(double radAngle, double xCoordinate)
	{
		double result = 0.0;
		double tan = Math.tan(radAngle);
		
		result = tan * xCoordinate;
		
		return result;
	}
	
	
}
