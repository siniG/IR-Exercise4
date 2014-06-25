package searchengine.query;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class BasicSearchQuery {

	protected List<String> stopwords = Arrays.asList("a", "an", "and", "are",
			"as", "at", "be", "but", "by", "for", "if", "in", "into", "is",
			"it", "no", "not", "of", "on", "or", "such", "that", "the",
			"their", "then", "there", "these", "they", "this", "to", "was",
			"will", "with");

	private Directory luceneDir;
	protected IndexSearcher searcher;
	private IndexReader reader;
	protected Analyzer analyzer;
	CosineDocumentSimilarity cosineSimilarity;

	public BasicSearchQuery(Directory luceneDir) {
		this.luceneDir = luceneDir;
	}

	public void close() {
		try {
			if (this.reader != null) {
				this.reader.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.reader = null;
			this.searcher = null;
		}
	}

	public void Init() throws IOException {
		initAnalyzer();
		this.reader = DirectoryReader.open(this.luceneDir);
		this.searcher = new IndexSearcher(this.reader);
		BooleanQuery.setMaxClauseCount(5000);
		this.searcher.setSimilarity(new DefaultSimilarity());
		this.cosineSimilarity = new CosineDocumentSimilarity(this.reader);
	}

	protected void initAnalyzer() {
		CharArraySet set = new CharArraySet(Version.LUCENE_47, this.stopwords,
				true);
		this.analyzer = new StandardAnalyzer(Version.LUCENE_47, set);

	}

	public TfIdfMatrix getTfIdfMatrix()
	{
		TfIdfMatrix matrix = new TfIdfMatrix(this.reader);
		try {
			matrix.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			matrix = null;
		}
		
		return matrix;
	}
}
