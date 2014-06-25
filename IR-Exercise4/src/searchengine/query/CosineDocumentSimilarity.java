package searchengine.query;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

public class CosineDocumentSimilarity {

	protected static final String CONTENT = "content";
	protected IndexReader _reader;
    
    public CosineDocumentSimilarity(IndexReader reader){
    	_reader = reader;
    }
    
    public double getCosineSimilarity(int doc1, int doc2) throws IOException
    {
    	Set<String> terms = new HashSet<String>();
        Map<String, Integer> f1 = getTermFrequencies(_reader, doc1, terms);
        Map<String, Integer> f2 = getTermFrequencies(_reader, doc2, terms);
        RealVector v1 = toRealVector(terms, f1);
        RealVector v2 = toRealVector(terms, f2);
        return (v1.dotProduct(v2)) / (v1.getNorm() * v2.getNorm());
    }
    
    Map<String, Integer> getTermFrequencies(IndexReader reader, int docId, Set<String> terms)
            throws IOException {
        Terms vector = reader.getTermVector(docId, CONTENT);
        TermsEnum termsEnum = null;
        termsEnum = vector.iterator(termsEnum);
        Map<String, Integer> frequencies = new HashMap<String, Integer>();
        BytesRef text = null;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
            frequencies.put(term, freq);
            terms.add(term);
        }
        return frequencies;
    }
    
    RealVector toRealVector(Set<String> terms, Map<String, Integer> map) {
        RealVector vector = new ArrayRealVector(terms.size());
        int i = 0;
        for (String term : terms) {
            int value = map.containsKey(term) ? map.get(term) : 0;
            vector.setEntry(i++, value);
        }
        return vector.mapDivide(vector.getL1Norm());
    }
}
