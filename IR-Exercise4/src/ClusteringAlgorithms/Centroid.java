package ClusteringAlgorithms;

import utilities.utils;

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

    public double GetDistance(float[] otherVector)
    {
        if (this.coordinates.length != otherVector.length)
        {
            return Double.NaN;
        }
        return utils.cosineSimilarity(GetCoordinates(), utils.floatArrayToDoubleArray(otherVector));
    }

    public double[] GetCoordinates()
    {
        return coordinates;
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
                if (this.GetCoordinates()[i] != other.GetCoordinates()[i])
                {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
