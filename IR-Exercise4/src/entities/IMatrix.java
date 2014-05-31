package entities;

public interface IMatrix {

	public boolean set(int row, int col, Double c);
	public Double get(int row, int col);
	public void init();
}
