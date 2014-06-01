package ClusteringAlgorithms;

/**
 * Created by amit on 31/05/2014.
 */
public interface ICentroid
{
    double[] GetCoordinates();
    double GetDistance(double[] otherVector);
}
