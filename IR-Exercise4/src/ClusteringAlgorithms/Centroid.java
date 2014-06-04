package ClusteringAlgorithms;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

/**
 * Created by amit on 31/05/2014.
 */
public class Centroid implements ICentroid
{
    private double coordinates[];
    private EuclideanDistance euclideanDistance = new EuclideanDistance();

    public Centroid(double[] coordinates)
    {
        this.coordinates = coordinates;
    }

    public Centroid(int numberOfDimensions)
    {
        coordinates = new double[numberOfDimensions];
    }

    public double GetDistance(double[] otherVector)
    {
        if (this.coordinates.length != otherVector.length)
        {
            return Double.NaN;
        }
        return euclideanDistance.compute(GetCoordinates(), otherVector);
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
