package entities;

import java.util.Enumeration;


public interface IDocVector{
	public float[] getTfIdfVector(int docId);
	public int getNumberOfTerms();
	public int getNumberOfDocs();
	public Enumeration<Integer> getDocIdEnumerator();
	public void init() throws Exception;
	public int getDocIdAtIndex(int index);
}