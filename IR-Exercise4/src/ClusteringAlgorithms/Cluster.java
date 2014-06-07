package ClusteringAlgorithms;

import entities.IDocVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 31/05/2014.
 */
public class Cluster<T> implements ICluster<T>
{
    private ICentroid centroid;
    private List<T> members;
    private IDocVector tfIdfMatrix;

    public Cluster(IDocVector tfIdfMatrix)
    {
        members = new ArrayList<T>();
        this.tfIdfMatrix = tfIdfMatrix;
    }

    public int Size()
    {
        int result = 0;

        if (members != null)
        {
            result = members.size();
        }

        return result;
    }

    public ICentroid GetCentroid()
    {
        if (this.centroid == null)
        {
            if (members.isEmpty())
            {
                System.out.println("ERROR: unable to calculate centroid. there are no members.");
                centroid = new Centroid(tfIdfMatrix.getNumberOfTerms(), this);
            }
            else
            {
                float [] averageVectorCoordinates = GetAverageVectorCoordinates();
                centroid = new Centroid(averageVectorCoordinates, this);
            }
        }

        return centroid;
    }

    private float[] GetAverageVectorCoordinates()
    {
        // initialize the vector
        float[] result = new float[tfIdfMatrix.getNumberOfTerms()];

        // calculate the sum of all values from all members.
        for (int i = 0; i < members.size(); i++)
        {
            Integer currentDocumentId = (Integer)members.get(i);

            float[] currentDocumentVector = this.tfIdfMatrix.getTfIdfVector(currentDocumentId);

            for (int j = 0; j < result.length; j++)
            {
                result[j] = result[j] + currentDocumentVector[j];
            }
        }

        // divide the sum from all vector in the number of members to get the average.
        for (int i = 0; i < result.length; i++)
        {
            result[i] = result[i] / members.size();
        }

        return result;
    }

    public synchronized boolean AddMember(T memberId)
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

    public void Clear()
    {
        this.members.clear();
        this.centroid = null;
    }
}
