package utilities;

public class utils
{
    public static double cosineSimilarity(float[] vectorA, float[] vectorB)
    {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += (vectorA[i] * vectorA[i]);
            normB += (vectorB[i] * vectorB[i]);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
