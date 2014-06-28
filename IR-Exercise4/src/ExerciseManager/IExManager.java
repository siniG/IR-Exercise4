package ExerciseManager;

public interface IExManager {

	public boolean LoadData();
	public boolean ProcessData() throws Exception;
    public void DisplayResults();
    public void WriteToOutputFile();
}
