package Program;

public interface IDocumentsLoader
{
    boolean LoadDocuments();
    int GetDocumentId(String documentName);
    int GetDocumentName(int documentId);
    int GetDocumentCluster(int documentId);
    String GetDocument(int documentId);
    int GetDocumentsCount();

}