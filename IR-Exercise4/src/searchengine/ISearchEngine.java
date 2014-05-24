package searchengine;

import java.util.List;
import entities.IRDoc;
import entities.SearchResult;

public interface ISearchEngine {

    Boolean index(List<IRDoc> documents);
    void setStopwords(List<String> stopwords);
	List<SearchResult> search(IRDoc irDoc, int retSize) throws Exception;
    
}
