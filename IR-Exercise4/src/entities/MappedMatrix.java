package entities;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class MappedMatrix implements IMatrix {

	int _numOfRows;
	int _numOfCols;
	RandomAccessFile memoryMappedFile;
	public MappedMatrix(int rowSize, int colSize) throws FileNotFoundException
	{
		this._numOfRows = rowSize;
		this._numOfCols = colSize;
		
		 memoryMappedFile = new RandomAccessFile("mappedMatrix.txt", "rw");
		 
	}
	
	public boolean insert(int row, int col, double val)
	{
		if(row >= this._numOfRows || col >= this._numOfCols)
			throw new IndexOutOfBoundsException();
		
		return false;
		
	}
	public double get(int row, int col)
	{
		return  0.0;
	}
}
