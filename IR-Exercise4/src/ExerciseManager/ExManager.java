
package ExerciseManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Program.DocumentsLoader;
import Program.IDocumentsLoader;
import Program.ParametersEnum;

import entities.BasicIRDoc;
import entities.IMatrix;
import entities.IRDoc;
import entities.MappedMatrix;
import entities.SearchResult;

import searchengine.BasicSearchEngine;
import searchengine.ISearchEngine;

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
	
	public void ProcessData() throws FileNotFoundException
	{
		this.matrix = new MappedMatrix(numOfDocs, numOfDocs);
		
		int docsToIterate = irDocs.size()/2;
		System.out.println("Info: Start processing data");
		for(int i = 0; i <= docsToIterate; i++)
		{
			List<SearchResult> results = this.searchEngine.search(irDocs.get(i), numOfDocs);
			System.out.println("Info: not of results returned for doc " + irDocs.get(i).getId() + " from search=" + results.size());
			
		}
		
		
		System.out.println("Info: Done processing data");
	}
	
	
}
