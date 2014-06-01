package entities;

import java.io.IOException;

public class VectorMatrix implements IMatrix {

	private LargeDoubleMatrix matrix;
	private int width, height;
	public VectorMatrix(int rows, int columns) throws IOException
	{
		this.width = columns;
		this.height = rows;
		matrix = new LargeDoubleMatrix("coordinate_matrix", columns, rows);
		
		
	}
	
	public float[] getRow(int row)
	{
		if(row > this.height)
			return null;
		
		float[] vector = new float[this.width];
		
		Float value;
		
		for(int i = 0; i < this.width; i++)
		{
			value = ((value = this.get(row, i)) == null) ? 0 : value;
			
			vector[i] = value; 
		}
		
		
		return vector;
	}
	
	public Float get(int row, int column)
	{
		
		/*if(row > column)
		{
			int temp;
			temp = column;
			column = row;
			row = temp;
		}*/
		
		Double distance = this.matrix.get(column, row);

        // check if value exists, only return a calculated value.
        // 0.0 means the documents are the same.
		distance =  ((distance == null) ||
					(Double.isNaN(distance)) ||
					(distance == 0.0)) ? null : distance;
		
		return distance.floatValue();
		
	}
	
	public boolean set(int row, int column, Float distance)
	{
		// only calculate half matrix
		/*if(row > column)
		{
            int temp;
			temp = column;
			column = row;
			row = temp;
		}*/
		
		if(distance == null)
		{
			this.matrix.set(column, row, Float.NaN);
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

    public int getRowsNumber()
    {
        return this.matrix.height();
    }

    public int getColumnsNumber()
    {
        return this.matrix.width();
    }
    
    
}
