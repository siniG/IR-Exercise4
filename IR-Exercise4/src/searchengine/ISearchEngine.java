package searchengine;

import java.util.List;

import searchengine.query.TfIdfMatrix;
import entities.IRDoc;
import entities.SearchResult;

public interface ISearchEngine {

    Boolean index(List<IRDoc> documents);
    void setStopwords(List<String> stopwords);
	List<SearchResult> search(IRDoc irDoc, int retSize) throws Exception;
	Double getCosineSimilarity(int docId1, int docId2);
	TfIdfMatrix getTfIdfMatrix();
    
}
