package Program;

import ExerciseManager.ExManager;
import ExerciseManager.IExManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException
	{		
        // check the parameter file is given to the program.
        if (args.length == 0)
        {
            System.out.println("ERROR: Argument 1 is missing. first and only argument should be parameters file.");
        }
        else
        {
            // create a dictionary to hold all parameters
            Hashtable<ParametersEnum, String> parameters = new Hashtable<ParametersEnum, String>();


            // try to load the parameters file
            if (LoadParametersFile(args[0], parameters))
            {
                System.out.println("INFO: Parameters loaded successfully, executing clustering process.");

                try
                {
                    IExManager exerciseManager = new ExManager(parameters);
                    if(!exerciseManager.LoadData())
                    {
                        System.out.println("ERROR: Unable to load exercise data.");
                    }
                    else if (!exerciseManager.ProcessData())
                    {
                        System.out.println("ERROR: Unable to complete the data process.");
                    }
                    else
                    {
                        exerciseManager.DisplayResults();
                    }

                }
                catch (Exception e)
                {
                    System.out.println("ERROR: Exception during execution of exercise. ex=" + e.getMessage() + " stack=" + e.getStackTrace());
                }
            }
            else
            {
                System.out.println("ERROR: Unable to load parameter file: \"" + args[0] + "\". Quitting.");
            }
        }
	}

    /**
     * tries to load the parameters from the parameters file.
     * returns true if succeeds, else - false.
     */
    private static boolean LoadParametersFile (String parametersFilePath, Hashtable<ParametersEnum, String> parametersHashtable)
    {
        System.out.println("INFO: Loading Parameters file: \"" + parametersFilePath + "\".");
        boolean result = false;
        FileReader parameterFileReader;
        BufferedReader parameterFileBufferedReader;
        try
        {
            parameterFileReader = new FileReader(parametersFilePath);
            parameterFileBufferedReader = new BufferedReader(parameterFileReader);
            String line;

            // load all parameters from the parameter file.
            while ((line = parameterFileBufferedReader.readLine()) != null)
            {
                ProcessParameterLine(line, parametersHashtable);
            }

            // check we have all the parameters.
            if (parametersHashtable.size() == ParametersEnum.values().length)
            {
                // all parameters exist.
                result = true;
            }
            else
            {
                System.out.println("ERROR: Some parameters are missing in the parameters file: \"" + parametersFilePath + "\". Quitting.");
            }

        } catch (Exception e)
        {
            System.out.println("ERROR: Unable to read file: \"" + parametersFilePath + "\". Quitting.");
            System.out.println(e);
        }
        return result;
    }

    /**
     * loads a single line for the parameters file to the parameters hash table.
     */
    private static void ProcessParameterLine(String line, Hashtable<ParametersEnum, String> parametersHashtable)
    {
        System.out.println("INFO: Handling parameter line: \"" + line + "\".");

        StringTokenizer stringTokenizer = new StringTokenizer(line, "=");
        if (stringTokenizer.countTokens() == 2)
        {
            String parameterName = stringTokenizer.nextToken().toLowerCase();

            if (parameterName.toLowerCase().equals(ParametersEnum.DocsFile.name().toLowerCase()))
            {
                String docsFileParameter = stringTokenizer.nextToken();
                parametersHashtable.put(ParametersEnum.DocsFile, docsFileParameter);
                System.out.println("INFO: Added parameter for documet file name: \"" + docsFileParameter + "\".");
            }
            else if (parameterName.toLowerCase().equals(ParametersEnum.OutputFile.name().toLowerCase()))
            {
                String outputFileParameter = stringTokenizer.nextToken();
                parametersHashtable.put(ParametersEnum.OutputFile, outputFileParameter);
                System.out.println("INFO: Added parameter for output file name: \"" + outputFileParameter + "\".");
            }
            else if (parameterName.toLowerCase().equals(ParametersEnum.K.name().toLowerCase()))
            {
                String kNumberOfClusters = stringTokenizer.nextToken();
                parametersHashtable.put(ParametersEnum.K, kNumberOfClusters);
                System.out.println("INFO: Added parameter for k number of clusters: \"" + kNumberOfClusters + "\".");
            }
            else if (parameterName.toLowerCase().equals(ParametersEnum.ClusteringAlgorithm.name().toLowerCase()))
            {
                String clusterAlgorithmName = stringTokenizer.nextToken().toLowerCase();

                if (clusterAlgorithmName.equals(ClusteringAlgorithmEnum.Basic.name().toLowerCase()))
                {
                    parametersHashtable.put(ParametersEnum.ClusteringAlgorithm, ClusteringAlgorithmEnum.Basic.name());
                    System.out.println("INFO: Added parameter for clustering algorithm type: \"" + clusterAlgorithmName + "\".");
                }
                else if (clusterAlgorithmName.equals(ClusteringAlgorithmEnum.BasicPlusPlus.name().toLowerCase()) ||
                         clusterAlgorithmName.equals("basic++"))
                {
                    parametersHashtable.put(ParametersEnum.ClusteringAlgorithm, ClusteringAlgorithmEnum.BasicPlusPlus.name());
                    System.out.println("INFO: Added parameter for clustering algorithm type: \"" + clusterAlgorithmName + "\".");
                }
                else if (clusterAlgorithmName.equals(ClusteringAlgorithmEnum.Improved.name().toLowerCase()))
                {
                    parametersHashtable.put(ParametersEnum.ClusteringAlgorithm, ClusteringAlgorithmEnum.Improved.name());
                    System.out.println("INFO: Added parameter for clustering algorithm type: \"" + clusterAlgorithmName + "\".");
                }
                else
                {
                    System.out.println("ERROR: The retrieval algorithm name: \"" + clusterAlgorithmName + "\" from the parameter file is unknown. Ignoring.");
                }
            }
            else
            {
                System.out.println("WARN: Unknown parameter in parameter file: \"" + parameterName + "\". Ignoring.");
            }
        }
        else
        {
            System.out.println("WARN: Wrong line structure. Each parameter line should contain the equal sign (\"=\").");
        }
    }
}
