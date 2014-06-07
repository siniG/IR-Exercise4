package ClusteringAlgorithms;

/**
 * Created by amit on 31/05/2014.
 */
public interface ICentroid
{
    float[] GetCoordinates();
    double GetDistance(float[] otherVector);
    boolean equals(ICentroid other);
    ICluster<Integer> GetCluster();
}
