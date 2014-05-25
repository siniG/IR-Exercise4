package utilities;

import java.lang.Math;

import entities.Coordinate;

public class utils {

	public static Coordinate GetOppositeByTangent(double radAngle, double xCoordinate)
	{
		
		double result = 0.0;
		double tan = Math.tan(radAngle);
		
		result = tan * xCoordinate;
		Coordinate c = new Coordinate(xCoordinate, result);
		
		return c;
	}
	
	
}
