package ClusteringAlgorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 31/05/2014.
 */
public class Cluster<T>
{
    private Centroid centroid;
    private List<T> members;

    public Cluster(Centroid centroid)
    {
        this.centroid = centroid;
        members = new ArrayList<T>();
    }

    public ICentroid GetCentroid()
    {
        return centroid;
    }

    public boolean AddMember(T memberId)
    {
        if (members.contains(memberId))
        {
            return false;
        }
        else
        {
            members.add(memberId);
        }
        return true;
    }
}
