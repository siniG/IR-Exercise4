package entities;

import java.util.Enumeration;


public interface IMatrix{
	public float[] getTfIdfVector(int docId);
	public int getNumberOfTerms();
	public int getNumberOfDocs();
	public Enumeration<Integer> getDocIdEnumerator();
	public void init() throws Exception;
}
