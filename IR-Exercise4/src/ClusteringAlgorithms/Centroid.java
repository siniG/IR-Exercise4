package ClusteringAlgorithms;

/**
 * Created by amit on 31/05/2014.
 */
public class Centroid implements ICentroid
{
    private double xCoordinate;
    private double yCoordinate;

    public Centroid(double x, double y)
    {
        xCoordinate = x;
        yCoordinate = y;
    }

    public double GetXCoordinate()
    {
        return xCoordinate;
    }

    public double GetYCoordinate()
    {
        return yCoordinate;
    }
}
