
package ExerciseManager;

import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import ClusteringAlgorithms.IResultsWrapper;
import ClusteringAlgorithms.KMeans.ImprovedKmeansPlusPlus;
import ClusteringAlgorithms.KMeans.KMeans;
import ClusteringAlgorithms.KMeans.KmeansPlusPlus;
import ClusteringAlgorithms.ResultsWrapper;
import Program.ClusteringAlgorithmEnum;
import Program.DocumentsLoader;
import Program.IDocumentsLoader;
import Program.ParametersEnum;
import entities.BasicIRDoc;
import entities.IDocVector;
import entities.IRDoc;
import entities.KeyValuePair;
import searchengine.BasicSearchEngine;
import searchengine.ISearchEngine;
import searchengine.query.TfIdfMatrix;
import utilities.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.sun.tools.javac.resources.javac;

public class ExManager implements IExManager {

	ISearchEngine searchEngine;
	Hashtable<ParametersEnum, String> params;
	List<IRDoc> irDocs;
	IDocVector matrix;
	int numOfDocs;
	IDocumentsLoader docLoader;
	IResultsWrapper clusteringAlgorithmResults;
	int numberOfClusters;
	
	public ExManager(Hashtable<ParametersEnum, String> parameters) throws IOException
	{
		irDocs = new ArrayList<IRDoc>();
		this.params = parameters;
		searchEngine = new BasicSearchEngine("basic_search_engine_dir");
		matrix = null;
	}
	
	public boolean LoadData()
    {
		docLoader = new DocumentsLoader(this.params.get(ParametersEnum.DocsFile));

		if(!docLoader.LoadDocuments())
		{
			System.out.println("ERROR: document loader couldn't load all documents.");
			return false;
		}
		
		java.util.Iterator<Integer> docIter = docLoader.GetDocumentIterator();
		numOfDocs = docLoader.GetDocumentsCount();
		
		//iterate over documents
		System.out.println("INFO: starting to iterate over docs");
		while(docIter.hasNext())
		{
			Integer id = docIter.next();
			
			if(id == null)
			{
				System.out.println("WARN: Doc id received as null! skipping document.");
				continue;
			}
			//get document by id
			String rawDoc = docLoader.GetDocument(id);
			
			//create ir-doc (strips html)
			IRDoc doc = BasicIRDoc.create(id, rawDoc);
			irDocs.add(doc);
		}
		
		//insert docs to search engine
		System.out.println("INFO: completed iterating over docs. total docs=" + irDocs.size());
		System.out.println("INFO: start indexing docs");
		if (!this.searchEngine.index(irDocs))
        {
            System.out.println("ERROR: Search engine could index all documents, Quitting.");
            return false;
        }

		return true;
	}
	
	public boolean ProcessData() throws Exception
	{
        boolean result = false;
		//this.matrix = new VectorMatrixTest(numOfDocs+1, numOfDocs+1);
		//this.matrix.init();
		
		System.out.println("INFO: Start processing data");
		
		Date dateStart = new Date();
		TfIdfMatrix tfIdfMatrix = this.searchEngine.getTfIdfMatrix();
		Date dateEnd = new Date();
		
		System.out.println("INFO: tf-idf processing time took: " + (dateEnd.getTime() - dateStart.getTime()) + " milliseconds");

        String numberOfClusterStr = params.get(ParametersEnum.K);
        this.numberOfClusters = Integer.parseInt(numberOfClusterStr);

        if (params.get(ParametersEnum.ClusteringAlgorithm).toLowerCase().equals(ClusteringAlgorithmEnum.Basic.toString().toLowerCase()))
        {
            IClusteringAlgorithm<Integer> kmeans = new KMeans<Integer>(numberOfClusters, tfIdfMatrix);
            this.clusteringAlgorithmResults = new ResultsWrapper(kmeans.GetClusters(), this.docLoader);
            result = true;
        }
        else if (params.get(ParametersEnum.ClusteringAlgorithm).toLowerCase().equals(ClusteringAlgorithmEnum.BasicPlusPlus.toString().toLowerCase()))
        {
            IClusteringAlgorithm<Integer> kmeansPlusPlus = new KmeansPlusPlus<Integer>(numberOfClusters, tfIdfMatrix);
            this.clusteringAlgorithmResults = new ResultsWrapper(kmeansPlusPlus.GetClusters(), this.docLoader);
            result = true;
        }
        else if (params.get(ParametersEnum.ClusteringAlgorithm).toLowerCase().equals(ClusteringAlgorithmEnum.Improved.toString().toLowerCase()))
        {
            IClusteringAlgorithm<Integer> improvedKmeansPlusPlus = new ImprovedKmeansPlusPlus<Integer>(numberOfClusters, tfIdfMatrix, this.docLoader);
            this.clusteringAlgorithmResults = new ResultsWrapper(improvedKmeansPlusPlus.GetClusters(), this.docLoader);
            result = true;
        }
        else
        {
            System.out.println("ERROR: Unknown clustering algorithm requested.");
        }

        if (result) System.out.println("INFO: Done processing data" + utils.NewLineCharacter);

        return result;
	}

    public void DisplayResults()
    {
        System.out.println("---------------------------------" + utils.NewLineCharacter + "   FINAL RESULTS" + utils.NewLineCharacter);
        System.out.println("Algorithm used: " + params.get(ParametersEnum.ClusteringAlgorithm).toUpperCase() + utils.NewLineCharacter);
        System.out.println("Purity of results:");

        for(KeyValuePair<Integer, Double> kvp : clusteringAlgorithmResults.calculatePurity())
        {
            System.out.println("    Cluster id: " + kvp.getKey() + " purity: " + String.format("%.3f", kvp.getValue()));
        }
        System.out.println(utils.NewLineCharacter);
        System.out.println(String.format("Average purity of results: %.3f", clusteringAlgorithmResults.calculateAvgPurity()));
        System.out.println(String.format("Rand Index value of results: %.3f", clusteringAlgorithmResults.calculateRandIndex()));
    }
    
    public void WriteToOutputFile()
    {
    	Hashtable<Integer, Integer> table = new Hashtable<Integer,Integer>(); 
    	List<ICluster<Integer>> clusters = clusteringAlgorithmResults.GetResults();
    	
    	Integer member;
    	Enumeration<Integer> clusterMembers;
    	int i = 1;
    	List<Integer> members = new ArrayList<Integer>();
    	
    	for(ICluster<Integer> cluster : clusters)
    	{
    		clusterMembers = cluster.GetMemberIds();
    				
    		while(clusterMembers.hasMoreElements())
    		{
    			member = clusterMembers.nextElement();
    			members.add(member);
    			table.put(member, i);
    		}
    		
    		i++;
    	}
    	
    	java.util.Collections.sort(members);

    	PrintWriter printwriter = null;
        try
        {
        	File file = new File(this.params.get(ParametersEnum.OutputFile));
        	printwriter = new PrintWriter(file); 

        	for(int k = 0; k < members.size(); k++)
        	{
        		printwriter.println(members.get(k) + "," + table.get(members.get(k)));
        	}
        	
        }catch(Exception ex)
        {
        	System.out.println(ex.getMessage());
        }
        finally
        {
        	if(printwriter != null)
        	{
        		printwriter.flush();
        		printwriter.close();
        	}
        }
    }
	
}
