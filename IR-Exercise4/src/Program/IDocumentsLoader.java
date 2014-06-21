package Program;

import java.util.Hashtable;
import java.util.Iterator;

public interface IDocumentsLoader
{
    boolean LoadDocuments();
    int GetDocumentId(String documentName);
    String GetDocumentName(int documentId);
    int GetDocumentCluster(int documentId);
    String GetDocument(int documentId);
    int GetDocumentsCount();
    Iterator<Integer> GetDocumentIterator();
    Hashtable<Integer, Integer> getDocumentClusterIdsByDocumentId();
}