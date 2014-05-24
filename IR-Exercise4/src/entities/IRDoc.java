package entities;

import org.apache.lucene.document.Document;

public interface IRDoc {

    public Document createDocument();

    public String getContent();
    
    public String getRawContent();

    public int getId();
    
    public void setDocBoost(float boost);
    
    public IRDoc Clone();
}
