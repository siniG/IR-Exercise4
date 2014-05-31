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

        // check if value exists, only return a calculated value.
        // 0.0 means the documents are the same.
		Coordinate c = ((Double.isNaN(x)) ||
                        (Double.isNaN(y)) ||
                        (x == 0.0 && y == 0.0)) ? null : new Coordinate(x, y);
		
		return c;
		
	}
	
	public boolean set(int row, int column, Coordinate c)
	{
		// only calculate half matrix
		if(row > column)
		{
            int temp;
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
	}
}
