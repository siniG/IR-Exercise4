package Program;

public interface IDocumentLoader
{
    boolean LoadDocuments(String documentsFile);
    int GetDocumentId(String documentName);
    int GetDocumentName(int documentId);
    int GetDocumentCluster(int documentId);
    String GetDocument(int documentId);
}