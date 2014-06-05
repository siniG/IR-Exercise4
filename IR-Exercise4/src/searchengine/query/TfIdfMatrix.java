package searchengine.query;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;

import entities.IMatrix;

public class TfIdfMatrix implements IMatrix {
	
	private IndexReader reader;
	private final Map<String, Integer> termIdMap;
	private float[][] _tfidfMatrix;
	
	public TfIdfMatrix(IndexReader reader)
	{
		this.reader = reader;
		termIdMap = new HashMap<String, Integer>();
		_tfidfMatrix = null;
	}
	
	public boolean set(int row, int col, Float c)
	{
		if (this._tfidfMatrix.length >= row || this._tfidfMatrix[row].length >= col)
		{
			throw new IndexOutOfBoundsException();
		}
		
		this._tfidfMatrix[row][col] = (c == null) ? 0 : c;
		
		return true;
	}
	
	public Float get(int row, int col)
	{
		if (this._tfidfMatrix.length >= row || this._tfidfMatrix[row].length >= col)
		{
			throw new IndexOutOfBoundsException();
		}
		
		return this._tfidfMatrix[row][col];
	}
	
	public float[] getRow(int row)
	{
		if (this._tfidfMatrix.length <= row)
		{
			throw new IndexOutOfBoundsException();
		}
		
		return this._tfidfMatrix[row];
	}
	
	public int getColumnsNumber()
	{
		return this._tfidfMatrix[0].length;
	}
	public int getRowsNumber()
	{
		return this._tfidfMatrix.length;
	}
	
	public void init() throws Exception
	{
		this._tfidfMatrix = getTfIdfMatrix();
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
        for (int docID = 0; docID < reader.maxDoc(); docID++) 
        {
        	Map<String, Float> termFrequencies = new HashMap<String, Float>();
        	TermsEnum termsEnum = MultiFields.getTerms(reader, "content").iterator(null);
        	DocsEnum docsEnum = null;
            Terms vector = reader.getTermVector(docID, "content");
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
					tfidfMatrix[docID][termId] = tfidf;
				}
            }
			}
            else
            {
            	System.out.println("vector is null. doc id=" + docID);
            }
		}
        return tfidfMatrix;
	}
}
