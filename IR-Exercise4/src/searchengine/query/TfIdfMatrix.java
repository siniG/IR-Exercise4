package searchengine.query;


import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;

import entities.IDocVector;

public class TfIdfMatrix implements IDocVector {
	
	private IndexReader reader;
	private final Map<String, Integer> termIdMap;
	private float[][] _tfidfMatrix;
	private Map<Integer, Integer>  _DocIdToLuceneDocId;
	private List<Integer> _DocList;
	public TfIdfMatrix(IndexReader reader)
	{
		this.reader = reader;
		termIdMap = new HashMap<String, Integer>();
		_tfidfMatrix = null;
		_DocIdToLuceneDocId = null;
		_DocList = null;
	}
	
	public float[] getTfIdfVector(int docId)
	{
		Integer luceneId = this._DocIdToLuceneDocId.get(docId);
		
		if(luceneId != null)
			return this._tfidfMatrix[luceneId];
		
		return null;
	}
	
	public int getNumberOfTerms()
	{
		return this._tfidfMatrix[0].length;
	}
	public int getNumberOfDocs()
	{
		return this._tfidfMatrix.length;
	}
	
	public Enumeration<Integer> getDocIdEnumerator()
	{
		return Collections.enumeration(this._DocList);
	}
	
	public void init() throws Exception
	{
		this._tfidfMatrix = getTfIdfMatrix();
	}
	
	public int getDocIdAtIndex(int index)
	{
		if(index >= this._DocList.size())
			throw new IndexOutOfBoundsException();
		
		return this._DocList.get(index);
	}
	
	private float[][] getTfIdfMatrix() throws Exception{
		Map<String, Float> docFrequencies = new HashMap<String, Float>();
        DefaultSimilarity similarity = new DefaultSimilarity();
        
        termIdMap.clear();
        long numOfTerms = MultiFields.getTerms(reader, "content").size();
        long numOfDocs = reader.maxDoc();
          
        float[][] tfidfMatrix = new float[(int) numOfDocs][(int) numOfTerms];
         
        TermsEnum termEnum = MultiFields.getTerms(reader, "content").iterator(null);
        BytesRef bytesRef;
        int termIdIdx = 0;
          
        while ((bytesRef = termEnum.next()) != null) {
        	if (termEnum.seekExact(bytesRef)) {
        		String term = bytesRef.utf8ToString();
        		float idf = similarity.idf(termEnum.docFreq(), reader.numDocs());
        		docFrequencies.put(term, idf);
        		termIdMap.put(term, termIdIdx);
        		termIdIdx++;
              }
          }
          
        int termId;
        float idf, tfidf, tf; 
        _DocIdToLuceneDocId = new HashMap<Integer, Integer>();
        _DocList = new LinkedList<Integer>();

        for (int luceneDocID = 0; luceneDocID  < reader.maxDoc(); luceneDocID++) 
        {
        	Map<String, Float> termFrequencies = new HashMap<String, Float>();
        	TermsEnum termsEnum = MultiFields.getTerms(reader, "content").iterator(null);
        	DocsEnum docsEnum = null;
            Terms vector = reader.getTermVector(luceneDocID, "content");
            
			Document doc = reader.document(luceneDocID);
			String sDocId = doc.get("docid");
			Integer nDocId = Integer.parseInt(sDocId);
            this._DocIdToLuceneDocId.put(nDocId, luceneDocID);
            _DocList.add(nDocId);
            
            if(vector != null)
            {
            termsEnum = vector.iterator(termsEnum);
            boolean inWhile=false;
            while ((bytesRef = termsEnum.next()) != null) {
            	inWhile=true;
                if (termsEnum.seekExact(bytesRef)) {
                	tf = 0;
                	String term = bytesRef.utf8ToString();
					
					docsEnum = termsEnum.docs(null, null, DocsEnum.FLAG_FREQS);
					
					while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
						tf = similarity.tf(docsEnum.freq());
						termFrequencies.put(term, tf);
					}
					idf = docFrequencies.get(term);
					tfidf = tf * idf;
					// tf_Idf_Weights.put(term, w);
					termId = termIdMap.get(term);
					tfidfMatrix[luceneDocID][termId] = tfidf;
				}
            }
			}
            else
            {
            	System.out.println("vector is null. doc id=" + nDocId);
            }
		}
        
        return tfidfMatrix;
	}
}
