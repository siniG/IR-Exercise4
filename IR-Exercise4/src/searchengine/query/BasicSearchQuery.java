package searchengine.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import entities.IRDoc;

public class BasicSearchQuery {

	
	
    protected List<String> stopwords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by", "for",
	    "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then",
	    "there", "these", "they", "this", "to", "was", "will", "with");

    private Directory luceneDir;
    protected IndexSearcher searcher;
    private IndexReader reader;
    protected Analyzer analyzer;
    CosineDocumentSimilarity cosineSimilarity; 

    public BasicSearchQuery(Directory luceneDir) {
	this.luceneDir = luceneDir;
    }

    public void close() {
	try {
	    if (this.reader != null) {
		this.reader.close();

	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    this.reader = null;
	    this.searcher = null;
	}
    }

    public Document getDoc(int docId) {
	Document doc = null;

	try {
	    doc = this.searcher.doc(docId);
	} catch (IOException e) {
	    System.out.println("Error: unable to fetch doc with id=" + docId);
	    e.printStackTrace();
	}
	
	return doc;
	}
	
	public int getDocByForiegnId(int docId)
	{
		int retval = -1;
	
		String sQuery = "id: " + docId;
		QueryParser queryParser = new QueryParser(Version.LUCENE_47, "id", this.analyzer);
		
		try {
			Query query = queryParser.parse(sQuery);
			TopDocs topDocs = this.searcher.search(query, 1);
			
			if (topDocs.totalHits > 0) {
				retval = topDocs.scoreDocs[0].doc;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retval;
    }

    public void Init() throws IOException {
	initAnalyzer();
	this.reader = DirectoryReader.open(this.luceneDir);
	this.searcher = new IndexSearcher(this.reader);
	this.cosineSimilarity = new CosineDocumentSimilarity(this.reader);
    }

    protected void initAnalyzer() {
	CharArraySet set = new CharArraySet(Version.LUCENE_47, this.stopwords, true);
	this.analyzer = new StandardAnalyzer(Version.LUCENE_47, set);

    }

    @SuppressWarnings("unchecked")
    public List<ScoreDoc> query(IRDoc doc, int retSize) throws IOException {
	List<ScoreDoc> docs = Collections.EMPTY_LIST;

	try {
	    QueryParser queryParser = new QueryParser(Version.LUCENE_47, "content", this.analyzer);
	    String escapeQueryStr = QueryParser.escape(doc.getContent());
	    Query query = queryParser.parse(escapeQueryStr);
	    TopDocs topDocs = this.searcher.search(query, retSize);

	    if (topDocs.totalHits > 0) {
		docs = Arrays.asList(topDocs.scoreDocs);
	    }

	} catch (ParseException e) {
	    e.printStackTrace();
	}

	return docs;
    }

    public void setStopWords(List<String> stopWords) {
	this.stopwords = stopWords;
    }
    
    public double getCosineSimilarity(int doc1, int doc2)
    {
    	double cosSim = 0;
		try {
			cosSim = this.cosineSimilarity.getCosineSimilarity(doc1, doc2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return cosSim;
    }

    public void TestAnalyzer() {
	try {
	    TokenStream stream = this.analyzer.tokenStream("myfield", new StringReader(
		    "hello there three word should not be present"));

	    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

	    try {
		stream.reset();

		while (stream.incrementToken()) {
		    System.out.println(termAtt.toString());
		}

		stream.end();
	    } finally {
		stream.close();
	    }
	} catch (Exception ex) {

	}
    }

}
