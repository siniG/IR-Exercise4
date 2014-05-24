package Program;

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * handles all document loading and id handling.
 */
public class DocumentsLoader implements IDocumentsLoader
{
    private String documentsFile;
    private Hashtable<Integer, String> documentNameByDocumentId;
    private Hashtable<String, Integer> documentIdByDocumentName;
    private Hashtable<Integer, Integer> documentClusterIdByDocumentId;

    public DocumentsLoader(String documentsFile)
    {
        this.documentsFile = documentsFile;

        this.documentNameByDocumentId = new Hashtable<Integer, String>();
        this.documentIdByDocumentName = new Hashtable<String, Integer>();
        this.documentClusterIdByDocumentId = new Hashtable<Integer, Integer>();
    }

    public boolean LoadDocuments()
    {
        boolean result = false;

        try
        {
            FileReader documentsFileReader = new FileReader(documentsFile);
            BufferedReader documentsFileBufferedReader = new BufferedReader(documentsFileReader);

            String line;

            while ((line = documentsFileBufferedReader.readLine()) != null)
            {
                ProcessLineFromDocumentsFile(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * processes a single line from the documents file according to the following format:
     * <document id> <file name> <gold standard cluster id>
     * updates all the relevant dictionaries with the relevant data from the file
     * @param line
     */
    private void ProcessLineFromDocumentsFile(String line)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(line, " ");

        if (stringTokenizer.countTokens() == 3)
        {
            // get the document id
            String documentIdAsString = stringTokenizer.nextToken();
            int documentIdAsInteger = Integer.parseInt(documentIdAsString);
            if (documentIdAsInteger < 0)
            {
                System.out.println("Document id cannot be negative, ignoring line: " + line);
            }
            else
            {
                // get the document name
                String documentName = stringTokenizer.nextToken();

                if (documentName.length() <= 0)
                {
                    System.out.println("Document name is illegal, ignoring line: " + line);
                }
                else
                {
                    // get the gold stadard cluster id
                    String goldStadardClusterIdAsString = stringTokenizer.nextToken();
                    int goldStandardClusterIdAsInteger = Integer.parseInt(goldStadardClusterIdAsString);
                    if (goldStandardClusterIdAsInteger < 0)
                    {
                        System.out.println("Cluster id cannot be negative, ignoring line: " + line);
                    }
                    else
                    {
                        // all parameters of the document are valid, insert data into distionaries.
                        documentClusterIdByDocumentId.put(documentIdAsInteger, goldStandardClusterIdAsInteger);
                        documentIdByDocumentName.put(documentName, documentIdAsInteger);
                        documentNameByDocumentId.put(documentIdAsInteger, documentName);
                    }
                }
            }
        }
        else
        {
            System.out.println("Unable to process following line from documents file (wrong line structure): " + line);
        }
    }

    public int GetDocumentId(String documentName) {
        int result = Integer.MIN_VALUE;

        if (documentNameByDocumentId.containsKey(documentName))
        {

        }

        return result;
    }

    public int GetDocumentName(int documentId) {
        int result = Integer.MIN_VALUE;

        return result;
    }

    public int GetDocumentCluster(int documentId) {
        int result = Integer.MIN_VALUE;


        return result;
    }

    public String GetDocument(int documentId) {
        String result = null;

        return result;
    }

    public int GetDocumentsCount() {
        return documentIdByDocumentName.size();
    }

    public Iterator<Integer> GetDocumentIterator() {
        return null;
    }
}
