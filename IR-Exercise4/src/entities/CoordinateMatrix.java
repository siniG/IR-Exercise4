package entities;

import java.io.IOException;

public class CoordinateMatrix implements IMatrix {

	private LargeDoubleMatrix matrix;
	private int width, height;
	public CoordinateMatrix(int rows, int columns) throws IOException
	{
		this.width = columns * 2;
		this.height = rows;
		matrix = new LargeDoubleMatrix("coordinate_matrix", columns * 2, rows);
		
		
	}
	
	public Coordinate get(int row, int column)
	{
		int temp;
		if(row > column)
		{
			temp = column;
			column = row;
			row = temp;
		}
		
		double x = this.matrix.get(column*2, row);
		double y = this.matrix.get(column*2 + 1, row);
		
		Coordinate c = (Double.isNaN(x) || Double.isNaN(y)) ? null : new Coordinate(x, y); 	
		
		return c;
		
	}
	
	public boolean set(int row, int column, Coordinate c)
	{
		int temp;
		if(row > column)
		{
			temp = column;
			column = row;
			row = temp;
		}
		
		if(c == null)
		{
			this.matrix.set(column * 2, row, Double.NaN);
			this.matrix.set(column * 2 + 1, row, Double.NaN);
		}
		else
		{
			this.matrix.set(column * 2, row, c.getXCoordinate());
			this.matrix.set(column * 2 + 1, row, c.getYCoordinate());
		}
		
		return true;
	}
	
	public void init()
	{
		matrix.resetMatrix();
		/*
		for(int i = 0; i < this.width; i++)
		{
			for(int j = 0; j < this.height; j++)
			{
				matrix.set(i, j, Double.NaN);
			}
		}*/
	}
}
