
package ExerciseManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Program.DocumentsLoader;
import Program.IDocumentsLoader;
import Program.ParametersEnum;
import entities.BasicIRDoc;
import entities.Coordinate;
import entities.CoordinateMatrix;
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
	
	public boolean LoadData() {
		docLoader = new DocumentsLoader(this.params.get(ParametersEnum.DocsFile));
		
		if(!docLoader.LoadDocuments())
		{
			System.out.println("Error loading documents from doc loader");
			return false;
		}
		
		java.util.Iterator<Integer> docIter = docLoader.GetDocumentIterator();
		numOfDocs = docLoader.GetDocumentsCount();
		
		//iterate over documents
		System.out.println("Info: starting to iterate over docs");
		while(docIter.hasNext())
		{
			Integer id = docIter.next();
			
			if(id == null)
			{
				System.out.println("Doc id received as null!");
				continue;
			}
			//get document by id
			String rawDoc = docLoader.GetDocument(id);
			
			//create ir-doc (strips html)
			IRDoc doc = BasicIRDoc.create(id, rawDoc);
			irDocs.add(doc);
			
			}
		
		//insert docs to search engine
		System.out.println("Info: completed iterating over docs. total docs=" + irDocs.size());
		System.out.println("Info: start indexing docs");
		this.searchEngine.index(irDocs);
		
		return true;
	}
	
	public void ProcessData() throws Exception 
	{
		this.matrix = new CoordinateMatrix(numOfDocs, numOfDocs);
		this.matrix.init();
		
		System.out.println("Info: Start processing data");
		for(int i = 0; i < irDocs.size(); i++)
		{
			//for every irDoc, run search query matching this document
			List<SearchResult> results = this.searchEngine.search(irDocs.get(i), numOfDocs);
			System.out.println("Info: not of results returned for doc " + irDocs.get(i).getId() + " from search=" + results.size());
			//calculate distance between result and query
			CalculateAndStoreDistance(irDocs.get(i).getId(), results);
		}
		
		
		System.out.println("Info: Done processing data");
	}

	private void CalculateAndStoreDistance(int id, List<SearchResult> results) {
		
		Coordinate coordinateOnGraph;
		for(SearchResult result : results)
		{
			if((coordinateOnGraph = this.matrix.get(id, result.getDocId())) == null)
			{
				coordinateOnGraph = utils.GetOppositeByTangent(result.getCosineSimilariy(), result.getScore());
				this.matrix.set(id, result.getDocId(), coordinateOnGraph);
			}
		}
	}
	
	
}
