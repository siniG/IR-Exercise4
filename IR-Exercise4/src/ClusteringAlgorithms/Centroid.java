package ClusteringAlgorithms;

import utilities.utils;

/**
 * Created by amit on 31/05/2014.
 */
public class Centroid implements ICentroid
{
    private float coordinates[];
    private ICluster cluster;

    /**
     * represents the minimum change between coordinates to consider them different.
     */
    private final double finesse = 0.0000003;


    public Centroid(float[] coordinates, ICluster cluster)
    {
        this.coordinates = coordinates;
        this.cluster = cluster;
    }

    public Centroid(int numberOfDimensions, ICluster cluster)
    {
        coordinates = new float[numberOfDimensions];
        this.cluster = cluster;
    }

    public double GetDistance(float[] otherVector)
    {
        if (this.coordinates.length != otherVector.length)
        {
            return Double.NaN;
        }
        return utils.cosineSimilarity(GetCoordinates(), otherVector);
    }

    public float[] GetCoordinates()
    {
        return coordinates;
    }

    public ICluster<Integer> GetCluster()
    {
        return this.cluster;
    }

    public final boolean equals (ICentroid other)
    {
        boolean result = true;

        if (this.GetCoordinates().length != other.GetCoordinates().length)
        {
            result = false;
        }
        else
        {
            for (int i = 0; i < this.GetCoordinates().length; i++)
            {
                if (Math.abs(this.GetCoordinates()[i] - other.GetCoordinates()[i]) > finesse)
                {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
