package entities;

public class Coordinate {

	private double x, y;
	
	public Coordinate(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public Coordinate()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public double getXCoordinate()
	{
		return this.x;
	}
	
	public double getYCoordinate()
	{
		return this.y;
	}
	
	public void setYCoordinate(double y)
	{
		this.y = y;
	}
	
	public void setXCoordinate(double x)
	{
		this.x = x;
	}
}
