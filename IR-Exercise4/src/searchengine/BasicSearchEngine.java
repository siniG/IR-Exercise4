package searchengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import searchengine.indexer.BasicIndexer;
import searchengine.indexer.ImprovedIndexer;
import searchengine.query.BasicSearchQuery;
import searchengine.query.ImprovedSearchQuery;

import entities.IRDoc;
import entities.SearchResult;

public class BasicSearchEngine implements ISearchEngine {

	protected boolean indexChanged;
    protected BasicIndexer indexer;
    protected Directory luceneDir;
    protected ImprovedSearchQuery searcher;
    protected List<String> stopwords;
    
	public BasicSearchEngine(String engineDir)
		throws IOException
	{
		this.luceneDir = FSDirectory.open(new File(engineDir));
		this.indexChanged = false;
		this.stopwords = null;
	}
	
    protected synchronized BasicIndexer getIndexWriter() {

	if (this.indexer == null) {
	    BasicIndexer indexer = new ImprovedIndexer(this.luceneDir);
	    indexer.setStopWords(this.stopwords);

	    if (indexer.OpenIndexWriter()) {
		this.indexer = indexer;
	    }
	}

	return this.indexer;
    }
    

    /**
     * Index a list of documents into lucene engine
     */
    @Override
    public Boolean index(List<IRDoc> documents) {
	BasicIndexer indexer = getIndexWriter();
	Integer indexedDocsCount = 0;

	if (indexer != null) {
	    Document doc;
	    for (IRDoc myDoc : documents) {
	    	doc = myDoc.createDocument();

		if (doc != null) {
		    try {
			indexer.index(doc);
			indexedDocsCount++;
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }
	    this.indexChanged = true;

	    indexer.closeIndexWriter();
	}

	return documents.size() == indexedDocsCount;
    }
    
    @Override
    public List<SearchResult> search(IRDoc irDoc, int retSize) throws Exception {
	List<SearchResult> result = new LinkedList<SearchResult>();
	BasicSearchQuery searcher = getSearcher();

	if (searcher != null) {
	    try {
	    
	    	//check to see if a document with given id is found in lucene
	    Integer luceneDocId = this.searcher.getLuceneDocIdByForeignId(irDoc.getId());
	    if(luceneDocId == null)
	    {
	    	throw new Exception("Cannot find lucene document with irDoc id=" + irDoc.getId());
	    }
	    
	    //run query and calculate cosine similarity between query and results received
		List<ScoreDoc> docs = searcher.query(irDoc, retSize);
		String id;
		double cosineSim;
		for (ScoreDoc doc : docs) {
		    Document tempDoc = searcher.getDoc(doc.doc);
		    id = tempDoc.get("id");
		    cosineSim = this.searcher.getCosineSimilarity(luceneDocId, doc.doc);
		    
			result.add(new SearchResult(Integer.valueOf(id), doc.score, cosineSim));
		}

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	return result;
    }
    
    protected synchronized BasicSearchQuery getSearcher() {
	if (this.searcher == null || this.indexChanged) {
	    try {
		if (this.searcher != null) {
		    this.searcher.close();
		}

		this.searcher = new ImprovedSearchQuery(this.luceneDir);
		this.searcher.setStopWords(this.stopwords);
		this.searcher.Init();
	    } catch (IOException e) {
		this.searcher = null;
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } finally {
		this.indexChanged = false;
	    }
	}

	return this.searcher;
    }
    
    @Override
    public void setStopwords(List<String> stopwords) {
	this.stopwords = stopwords;
    }
}
