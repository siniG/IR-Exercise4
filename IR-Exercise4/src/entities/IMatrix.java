package entities;

public interface IMatrix {

	public boolean set(int row, int col, Coordinate c);
	public Coordinate get(int row, int col);
	public void init();
}
