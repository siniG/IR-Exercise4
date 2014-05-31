
package ExerciseManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Program.DocumentsLoader;
import Program.IDocumentsLoader;
import Program.ParametersEnum;
import entities.BasicIRDoc;
import entities.DistanceMatrix;
import entities.IMatrix;
import entities.IRDoc;
import entities.SearchResult;
import searchengine.BasicSearchEngine;
import searchengine.ISearchEngine;
import utilities.utils;

public class ExManager implements IExManager {

	ISearchEngine searchEngine;
	Hashtable<ParametersEnum, String> params;
	List<IRDoc> irDocs;
	IMatrix matrix;
	int numOfDocs;
	IDocumentsLoader docLoader;
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
		this.matrix = new DistanceMatrix(numOfDocs+1, numOfDocs+1);
		this.matrix.init();
		
		System.out.println("INFO: Start processing data");
		
		for(int i = 0; i < irDocs.size(); i++)
		{
			//for every irDoc, run search query matching this document
			List<SearchResult> results = this.searchEngine.search(irDocs.get(i), numOfDocs);
			System.out.println("INFO: not of results returned for doc " + irDocs.get(i).getId() + " from search=" + results.size());
			//calculate distance between result and query
			CalculateAndStoreDistance(irDocs.get(i).getId(), results);
		}

		System.out.println("INFO: Done processing data");
	}

	private void CalculateAndStoreDistance(int id, List<SearchResult> results) {
		
		//TODO: get cosine similarity from matrix, and if doesn't exist only then calculate based on lucene!!!!
		Double distance;
		Double cosineSimilarity;
		for(SearchResult result : results)
		{	
			if((distance = this.matrix.get(id, result.getDocId())) == null)
			{
				cosineSimilarity = this.searchEngine.getCosineSimilarity(id, result.getDocId());
				distance = utils.CalculateHypotenuseFromOrigin(cosineSimilarity, result.getScore());
				this.matrix.set(id, result.getDocId(), distance);
			}
		}
	}
}
