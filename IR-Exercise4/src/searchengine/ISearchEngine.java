package searchengine;

import java.util.List;

import searchengine.query.TfIdfMatrix;
import entities.IRDoc;

public interface ISearchEngine {

    Boolean index(List<IRDoc> documents);
	TfIdfMatrix getTfIdfMatrix();
    
}
