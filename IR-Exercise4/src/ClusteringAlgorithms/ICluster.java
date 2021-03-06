package ClusteringAlgorithms;

import java.util.Enumeration;

/**
 * Created by amit on 31/05/2014.
 */
public interface ICluster<T> {
    ICentroid GetCentroid();
    boolean AddMember(T newMember);
    void Clear();
    int Size();
    Enumeration<T> GetMemberIds();
}
