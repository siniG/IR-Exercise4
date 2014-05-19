package clusteringengine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import clusteringengine.indexer.ImprovedIndexer;
import clusteringengine.indexer.BasicIndexer;
import clusteringengine.query.BasicQueryEngine;
import clusteringengine.query.ImprovedQueryEngine;
import entities.IRDoc;

public class BasicClusteringEngine implements IClusterEngine {

	protected boolean indexChanged;
    protected BasicIndexer indexer;
    protected Directory luceneDir;
    protected ImprovedQueryEngine searcher;
    protected List<String> stopwords;
    
	public BasicClusteringEngine(String engineDir)
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
    
    protected synchronized BasicQueryEngine getSearcher() {
	if (this.searcher == null || this.indexChanged) {
	    try {
		if (this.searcher != null) {
		    this.searcher.close();
		}

		this.searcher = new ImprovedQueryEngine(this.luceneDir);
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
