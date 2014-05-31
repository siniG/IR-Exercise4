package ClusteringAlgorithms;

/**
 * Created by amit on 31/05/2014.
 */
public class Centroid implements ICentroid
{
    private double coordinates[];

    public Centroid(double[] coordinates)
    {
        this.coordinates = coordinates;
    }

    public Centroid(int numberOfDimensions)
    {
        coordinates = new double[numberOfDimensions];
    }

    public double []GetCoordinates()
    {
        return coordinates;
    }
}
