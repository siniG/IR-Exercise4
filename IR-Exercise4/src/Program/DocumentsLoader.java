package Program;

import java.io.*;
import java.util.*;

import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
                    String entireFileContent = FileUtils.readFileToString(fileToLoad);
/*                    Scanner fileScanner = new Scanner(fileToLoad);
                    String entireFileContent = fileScanner.next();
*/                    if (entireFileContent.length() == 0)
                    {
                        System.out.println("No content found for file: " + fileToLoad.getName());
                    }
                    else
                    {
                        // file should be loaded since it is a document, and it has content.
                        rawDocumentByDocumentId.put(currentFileDocumentId, entireFileContent);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Unable to load file: " + fileToLoad.toString());
                }
            }
        }

        if (rawDocumentByDocumentId.size() == documentNameByDocumentId.size() &&
            rawDocumentByDocumentId.size() == documentIdByDocumentName.size() &&
            rawDocumentByDocumentId.size() == documentClusterIdByDocumentId.size())
        {
            // this means we successfully loaded all the documents we received in the docs file parameter.
            result = true;
        }
        else
        {
            System.out.println("Not all internal dictionaries loaded successfully. see which size does not match:");
            System.out.println("raw documents by document id    = " + rawDocumentByDocumentId.size());
            System.out.println("document name by document id    = " + documentNameByDocumentId.size());
            System.out.println("document id by document name    = " + documentIdByDocumentName.size());
            System.out.println("document cluster by document id = " + documentClusterIdByDocumentId.size());

            Set<Integer> documentIdsMinusRawDocumentIds = Sets.difference(documentNameByDocumentId.keySet(), rawDocumentByDocumentId.keySet());
            System.out.println("different ids between all raw documents and documents file data: " + documentIdsMinusRawDocumentIds.toString());

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
            if (!StringUtils.isNumeric(documentIdAsString))
            {
                System.out.println("Document id is not a number, ignoring line: " + line);
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
                    String goldStandardClusterIdAsString = stringTokenizer.nextToken();
                    if (!StringUtils.isNumeric(goldStandardClusterIdAsString))
                    {
                        System.out.println("Cluster id is not a number, ignoring line: " + line);
                    }
                    else
                    {
                        // all parameters of the document are valid, insert data into dictionaries.
                        documentClusterIdByDocumentId.put(Integer.parseInt(documentIdAsString), Integer.parseInt(goldStandardClusterIdAsString));
                        documentIdByDocumentName.put(documentName, Integer.parseInt(documentIdAsString));
                        documentNameByDocumentId.put(Integer.parseInt(documentIdAsString), documentName);
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
