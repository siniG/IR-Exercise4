
package ExerciseManager;

import ClusteringAlgorithms.ICluster;
import ClusteringAlgorithms.IClusteringAlgorithm;
import ClusteringAlgorithms.*;
import ClusteringAlgorithms.KMeans.KMeans;
import ClusteringAlgorithms.KMeans.KmeansPlusPlus;
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

import java.io.IOException;
import java.util.*;

public class ExManager implements IExManager {

	ISearchEngine searchEngine;
	Hashtable<ParametersEnum, String> params;
	List<IRDoc> irDocs;
	IDocVector matrix;
	int numOfDocs;
	IDocumentsLoader docLoader;
	List<ICluster<Integer>> kmeansClusters;
    List<ICluster<Integer>> kmeansPlusPlusClusters;
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
	
	public void ProcessData() throws Exception 
	{
		//this.matrix = new VectorMatrixTest(numOfDocs+1, numOfDocs+1);
		//this.matrix.init();
		
		System.out.println("INFO: Start processing data");
		
		Date dateStart = new Date();
		TfIdfMatrix tfIdfMatrix = this.searchEngine.getTfIdfMatrix();
		Date dateEnd = new Date();
		
		System.out.println("INFO: tf-idf processing time took: " + (dateEnd.getTime() - dateStart.getTime()) + " milliseconds");

        String numberOfClusterStr = params.get(ParametersEnum.K);
        this.numberOfClusters = Integer.parseInt(numberOfClusterStr);
        

		IClusteringAlgorithm<Integer> kmeans = new KMeans<Integer>(numberOfClusters, tfIdfMatrix);
        this.kmeansClusters = kmeans.GetClusters(numberOfClusters);
        List<KeyValuePair<Integer, Double>> kmeansPurity = utils.CalculatePurity(this.kmeansClusters, numberOfClusters, this.docLoader);
        utils.PrintPurity(kmeansPurity);
        
        IResultsWrapper goldResultsWrapper = new ResultsWrapper(this.docLoader.getDocumentClusterIdsByDocumentId());
        IResultsWrapper kmeansResultWrapper = new ResultsWrapper(this.kmeansClusters);
        
        double kmeanRandIndex = utils.CalculateRandIndex(goldResultsWrapper, kmeansResultWrapper);
        
        System.out.println("INFO: kmean RandIndex= " + kmeanRandIndex);

        IClusteringAlgorithm<Integer> kmeansPlusPlus = new KmeansPlusPlus<Integer>(numberOfClusters, tfIdfMatrix);
        this.kmeansPlusPlusClusters = kmeansPlusPlus.GetClusters(numberOfClusters);
        List<KeyValuePair<Integer, Double>> kmeansPlusPlusPurity = utils.CalculatePurity(this.kmeansPlusPlusClusters, numberOfClusters, this.docLoader);
        utils.PrintPurity(kmeansPlusPlusPurity);
        
        IResultsWrapper kmeansPlusPlusResultWrapper = new ResultsWrapper(this.kmeansPlusPlusClusters);
        double kmeanPlusPlsRandIndex = utils.CalculateRandIndex(goldResultsWrapper, kmeansPlusPlusResultWrapper);
        System.out.println("INFO: kmean++ RandIndex= " + kmeanPlusPlsRandIndex);
        
        System.out.println("INFO: Done processing data");
	}
	
}
