package ClusteringAlgorithms;

/**
 * Created by amit on 31/05/2014.
 */
public class Centroid implements ICentroid
{
    private float coordinates[];

    public Centroid(float[] coordinates)
    {
        this.coordinates = coordinates;
    }

    public Centroid(int numberOfDimensions)
    {
        coordinates = new float[numberOfDimensions];
    }

    public float[] GetCoordinates()
    {
        return coordinates;
    }
}
