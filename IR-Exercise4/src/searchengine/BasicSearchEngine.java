package searchengine;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import searchengine.indexer.BasicIndexer;
import searchengine.indexer.ImprovedIndexer;
import searchengine.query.BasicSearchQuery;
import searchengine.query.ImprovedSearchQuery;
import searchengine.query.TfIdfMatrix;

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
		File file = new File(engineDir);
		if(file.isDirectory() && file.exists())
		{
			FileUtils.deleteDirectory(file);
		}
		this.luceneDir = FSDirectory.open(new File(engineDir));
		this.indexChanged = false;
		this.stopwords = null;
	}
	
    protected BasicIndexer getIndexWriter() {

	if (this.indexer == null) {
	    BasicIndexer indexer = new ImprovedIndexer(this.luceneDir);
	    //indexer.setStopWords(this.stopwords);

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
	    
	    //run query and calculate cosine similarity between query and results received
		List<ScoreDoc> docs = searcher.query(irDoc, retSize);
		String id;
		//double cosineSim;
		for (ScoreDoc doc : docs) {
		    Document tempDoc = searcher.getDoc(doc.doc);
		    id = tempDoc.get("docid");
		    //cosineSim = this.searcher.getCosineSimilarity(luceneDocId, doc.doc);
		    
			result.add(new SearchResult(Integer.valueOf(id), doc.score));
		}

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	return result;
    }
    
    public Double getCosineSimilarity(int docId1, int docId2)
    {
    	Double result = null;
    	Integer luceneDocId1 = this.searcher.getLuceneDocIdByForeignId(docId1);
    	Integer luceneDocId2 = this.searcher.getLuceneDocIdByForeignId(docId2);
    	
    	if(luceneDocId1 == null || luceneDocId2 == null)
    	{
    		System.out.println("ERROR: could not find documents in lucene with following ids: " + docId1 + ", " + docId2 );
    		return result;
    	}
    	
    	result = this.searcher.getCosineSimilarity(luceneDocId1, luceneDocId2);
    	
    	return result;
    }
    
    protected BasicSearchQuery getSearcher() {
	if (this.searcher == null || this.indexChanged) {
	    try {
		if (this.searcher != null) {
		    this.searcher.close();
		}

		this.searcher = new ImprovedSearchQuery(this.luceneDir);
		//this.searcher.setStopWords(this.stopwords);
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
    
    public TfIdfMatrix getTfIdfMatrix()
    {
    	if(this.searcher == null)
    		this.getSearcher();
    	
    	return this.searcher.getTfIdfMatrix();
    }
    
    @Override
    public void setStopwords(List<String> stopwords) {
	//this.stopwords = stopwords;
    }
}
