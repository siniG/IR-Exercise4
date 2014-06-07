package entities;

import java.util.Enumeration;
import java.util.Iterator;


public interface IDocVector{
	public float[] getTfIdfVector(int docId);
	public int getNumberOfTerms();
	public int getNumberOfDocs();
	public Enumeration<Integer> getDocIdEnumerator();
	public Iterator<Integer> getDocIdIterator();
	public void init() throws Exception;
	public int getDocIdAtIndex(int index);
}
