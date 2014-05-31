package entities;

import java.io.IOException;

public class CoordinateMatrix implements IMatrix {

	private LargeDoubleMatrix matrix;
	private int width, height;
	public CoordinateMatrix(int rows, int columns) throws IOException
	{
		this.width = columns;
		this.height = rows;
		matrix = new LargeDoubleMatrix("coordinate_matrix", columns, rows);
		
		
	}
	
	public Double get(int row, int column)
	{
		int temp;
		if(row > column)
		{
			temp = column;
			column = row;
			row = temp;
		}
		
		Double distance = this.matrix.get(column, row);

        // check if value exists, only return a calculated value.
        // 0.0 means the documents are the same.
		distance =  ((distance == null) ||
					(Double.isNaN(distance)) ||
					(distance == 0.0)) ? null : distance;
		
		return distance;
		
	}
	
	public boolean set(int row, int column, Double distance)
	{
		// only calculate half matrix
		if(row > column)
		{
            int temp;
			temp = column;
			column = row;
			row = temp;
		}
		
		if(distance == null)
		{
			this.matrix.set(column, row, Double.NaN);
		}
		else
		{
			this.matrix.set(column, row, distance);
		}
		
		return true;
	}
	
	public void init()
	{
		matrix.resetMatrix();
	}
}
