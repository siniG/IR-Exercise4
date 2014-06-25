package entities;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;

import org.jsoup.Jsoup;

public class BasicIRDoc implements IRDoc {
	
	public static final FieldType TYPE_STORED = new FieldType();

    static {
        TYPE_STORED.setIndexed(true);
        TYPE_STORED.setTokenized(true);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setStoreTermVectors(true);
        TYPE_STORED.setStoreTermVectorPositions(true);
        TYPE_STORED.freeze();
    }
	
    public static IRDoc create(int docId, String content) {
    	
	return new BasicIRDoc(docId, content);
    }

    private final int id;

    private final String rawContent;
    
    private String content;

    protected float boost;

    protected BasicIRDoc(int docId, String content) {
	this.id = docId;
	
	this.rawContent = content;
	this.boost = 1.0f;
	this.content = "";
    }

    @Override
    public Document createDocument() {
	Document newDoc = new Document();
	
	Field f;

	this.content = Jsoup.parse(this.rawContent).text().replaceAll("\\s+", " ").replaceAll("\n", " ");
	f = new Field("content", this.content, TYPE_STORED);	
	newDoc.add(f);

	f = new IntField("docid", this.id, Field.Store.YES);
	newDoc.add(f);

	return newDoc;

    }
}
