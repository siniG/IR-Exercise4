package ExerciseManager;

import java.util.List;

import entities.KeyValuePair;


public interface IExManager {

	public boolean LoadData();
	public void ProcessData() throws Exception;
	public List<KeyValuePair<Integer, Double>> CalculatePurity();
	
	
}
