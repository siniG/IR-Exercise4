package entities;

public interface IMatrix{

	public boolean set(int row, int col, Float c);
	public Float get(int row, int col);
	public float[] getRow(int row);
	public int getColumnsNumber();
	public int getRowsNumber();
	public void init() throws Exception;
}
