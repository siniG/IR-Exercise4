package entities;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import utilities.HtmlStripper;
import utilities.IHtmlStripper;


public class BasicIRDoc implements IRDoc {
	
    public static IRDoc create(int docId, String content) {
    	
	return new BasicIRDoc(docId, content);
    }

    private static IHtmlStripper stripper = new HtmlStripper();

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
    public BasicIRDoc Clone() {
	BasicIRDoc newDoc = new BasicIRDoc(this.getId(), this.getContent());
	return newDoc;
    }

    @Override
    public Document createDocument() {
	Document newDoc = new Document();
	
	Field f;
	
	this.content = stripper.GetHtmlBody(this.rawContent);
	
	f = new TextField("content", this.content, Field.Store.YES);
	f.setBoost(this.boost);
	newDoc.add(f);

	f = new LongField("id", this.id, Field.Store.YES);
	newDoc.add(f);

	return newDoc;

    }

    @Override
    public String getContent() {
	return this.content;
    }

    @Override
    public int getId() {
	return this.id;
    }

    @Override
    public void setDocBoost(float boost) {
	this.boost = boost;
    }
    
    @Override
    public String getRawContent()
    {
    	return this.rawContent;
    }

}
