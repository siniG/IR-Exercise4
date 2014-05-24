package Program;

import java.io.*;
import java.util.*;

/**
 * handles all document loading and id handling.
 */
public class DocumentsLoader implements IDocumentsLoader
{
    private String documentsFile;
    private Hashtable<Integer, String> documentNameByDocumentId;
    private Hashtable<String, Integer> documentIdByDocumentName;
    private Hashtable<Integer, Integer> documentClusterIdByDocumentId;
    private Hashtable<Integer, String> rawDocumentByDocumentId;

    public DocumentsLoader(String documentsFile)
    {
        this.documentsFile = documentsFile;

        this.documentNameByDocumentId = new Hashtable<Integer, String>();
        this.documentIdByDocumentName = new Hashtable<String, Integer>();
        this.documentClusterIdByDocumentId = new Hashtable<Integer, Integer>();
        this.rawDocumentByDocumentId = new Hashtable<Integer, String>();
    }

    public boolean LoadDocuments()
    {
        boolean result = false;

        try
        {
            FileReader documentsFileReader = new FileReader(documentsFile);
            BufferedReader documentsFileBufferedReader = new BufferedReader(documentsFileReader);

            String line;

            // load the file names, their document ids and the their gold standard cluster ids
            while ((line = documentsFileBufferedReader.readLine()) != null)
            {
                ProcessLineFromDocumentsFile(line);
            }

            System.out.println("Loaded documents file " + documentsFile + " successfully, " + documentNameByDocumentId.size() + " documents listed in the file.");
            System.out.println("Starting to load all the documents themselves, this might take some time...");

            // try loading all the actual document data from the disk.
            if (!LoadDocumentsContentFromFiles())
            {
                System.out.println("Not all document were successfully loaded, check your input.");
            }
            else
            {
                // all the documents form the documents file loaded successfully.
                result = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * gets a root folder, and returns all the files in it and in all the subdirectories under it.
     * @param rootDirectory
     * @return
     */
    private List<File> GetFilesInDirectory(File rootDirectory)
    {
        List<File> result = new ArrayList<File>();

        File[] filesArray = rootDirectory.listFiles();
        for (File currentFile : filesArray)
        {
            if (currentFile.isDirectory())
            {
                result.addAll(GetFilesInDirectory(currentFile));
            }
            else
            {
                result.add(currentFile);
            }
        }

        return result;
    }

    private boolean LoadDocumentsContentFromFiles()
    {
        boolean result = false;

        String userDirectory = System.getProperty("user.dir");
        File rootUserDirectory = new File(userDirectory);

        List<File> allFiles = GetFilesInDirectory(rootUserDirectory);

        for (File fileToLoad : allFiles)
        {
            if (documentIdByDocumentName.containsKey(fileToLoad.getName()))
            {
                // this file should be loaded since it represents a document
                int currentFileDocumentId = documentIdByDocumentName.get(fileToLoad.getName());

                try
                {
                    Scanner fileScanner = new Scanner(fileToLoad);
                    String entireFileContent = fileScanner.next();
                    if (entireFileContent.length() == 0)
                    {
                        System.out.println("No content found for file: " + fileScanner.toString());
                    }
                    else
                    {
                        // file should be loaded since it is a document, and it has content.
                        rawDocumentByDocumentId.put(currentFileDocumentId, entireFileContent);
                    }
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("Unable to load file: " + fileToLoad.toString());
                }
            }
        }

        if (rawDocumentByDocumentId.size() == documentNameByDocumentId.size())
        {
            // this means we successfully loaded all the documents we received in the docs file parameter.
            result = true;
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
