package clusteringengine;

import java.util.List;
import entities.IRDoc;

public interface IClusterEngine {

    Boolean index(List<IRDoc> documents);
    void setStopwords(List<String> stopwords);
}
