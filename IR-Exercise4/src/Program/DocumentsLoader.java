package Program;

import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

            System.out.println("INFO: Loaded documents file " + documentsFile + " successfully, " + documentNameByDocumentId.size() + " documents listed in the file.");
            System.out.println("INFO: Starting to load all the documents themselves, this might take some time...");

            // try loading all the actual document data from the disk.
            if (!LoadDocumentsContentFromFiles())
            {
                System.out.println("ERROR: Not all document were successfully loaded, check your input.");
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
            // inner loop is here since some of the file names are the same, and we need to perform a match using the
            // full file path (directory structure and file name.
            for (Map.Entry<String, Integer> entry : documentIdByDocumentName.entrySet())
            {
                // we need to compare on ly the length of the key from the docs file and the current full file path.
                int currentKeyLength = entry.getKey().length();
                int keyLength = fileToLoad.getAbsolutePath().length() - currentKeyLength;

                // only check file that actually might exist
                if (keyLength > 0)
                {
                    // some file name fixing for non-unix operating systems.
                    String fileNameSuffix = fileToLoad.getAbsolutePath().substring(keyLength);
                    fileNameSuffix = fileNameSuffix.replace('\\', '/');
                    if (entry.getKey().endsWith(fileNameSuffix))
                    {
                        // this file should be loaded since it represents a document from the documents file.
                        int currentFileDocumentId = entry.getValue();
                        try
                        {
                            String entireFileContent = FileUtils.readFileToString(fileToLoad);
                            if (entireFileContent.length() == 0)
                            {
                                System.out.println("ERROR: No content found for file: " + fileToLoad.getName());
                            }
                            else
                            {
                                // file should be loaded since it is a document, and it has content.
                                rawDocumentByDocumentId.put(currentFileDocumentId, entireFileContent);
                            }

                            // no need to continue the inner loop in search of other documents with same name and path.
                            break;
                        }
                        catch (Exception e)
                        {
                            System.out.println("ERROR: Unable to load file: " + fileToLoad.toString());
                        }
                    }
                }
            }
        }

        // check we loaded all the dictionaries.
        if (rawDocumentByDocumentId.size() == documentNameByDocumentId.size() &&
            rawDocumentByDocumentId.size() == documentIdByDocumentName.size() &&
            rawDocumentByDocumentId.size() == documentClusterIdByDocumentId.size())
        {
            // this means we successfully loaded all the documents we received in the docs file parameter.
            System.out.println("INFO: All documents loaded successfully. we have " + rawDocumentByDocumentId.size() + " documents.");
            result = true;
        }
        else
        {
            System.out.println("ERROR: Not all internal dictionaries loaded successfully. see which size does not match:");
            System.out.println("    raw documents by document id    = " + rawDocumentByDocumentId.size());
            System.out.println("    document name by document id    = " + documentNameByDocumentId.size());
            System.out.println("    document id by document name    = " + documentIdByDocumentName.size());
            System.out.println("    document cluster by document id = " + documentClusterIdByDocumentId.size());

            Set<Integer> documentIdsMinusRawDocumentIds = Sets.difference(documentNameByDocumentId.keySet(), rawDocumentByDocumentId.keySet());
            System.out.println("    different ids between all raw documents and documents file data: " + documentIdsMinusRawDocumentIds.toString());

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
                System.out.println("WARN: Document id is not a number, ignoring line: " + line);
            }
            else
            {
                // get the document name
                String documentName = stringTokenizer.nextToken();

                if (documentName.length() <= 0)
                {
                    System.out.println("WARN: Document name is illegal, ignoring line: " + line);
                }
                else
                {
                    // get the gold stadard cluster id
                    String goldStandardClusterIdAsString = stringTokenizer.nextToken();
                    if (!StringUtils.isNumeric(goldStandardClusterIdAsString))
                    {
                        System.out.println("WARN: Cluster id is not a number, ignoring line: " + line);
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
            System.out.println("WARN: Unable to process following line from documents file (wrong line structure): " + line);
        }
    }

    public int GetDocumentId(String documentName) {
        int result = Integer.MIN_VALUE;

        if (documentIdByDocumentName.containsKey(documentName))
        {
            result = documentIdByDocumentName.get(documentName);
        }

        return result;
    }

    public String GetDocumentName(int documentId) {
        String result = null;

        if (documentNameByDocumentId.containsKey(documentId))
        {
            result = documentNameByDocumentId.get(documentId);
        }

        return result;
    }

    public int GetDocumentCluster(int documentId) {
        int result = Integer.MIN_VALUE;

        if (documentClusterIdByDocumentId.containsKey(documentId))
        {
            result = documentClusterIdByDocumentId.get(documentId);
        }

        return result;
    }

    public String GetDocument(int documentId) {
        String result = null;

        if (rawDocumentByDocumentId.containsKey(documentId))
        {
            result = rawDocumentByDocumentId.get(documentId);
        }

        return result;
    }

    public int GetDocumentsCount()
    {
        return documentIdByDocumentName.size();
    }

    public Iterator<Integer> GetDocumentIterator()
    {
        return rawDocumentByDocumentId.keySet().iterator();
    }
}
