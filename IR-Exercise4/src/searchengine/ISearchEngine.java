package searchengine;

import java.util.List;
import entities.IRDoc;

public interface ISearchEngine {

    Boolean index(List<IRDoc> documents);
    void setStopwords(List<String> stopwords);
}
