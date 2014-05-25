package utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class HtmlStripper implements IHtmlStripper{
	public String GetHtmlBody(String html)
	{
		String result = "";
		
        ContentHandler contenthandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
			parser.parse(stream, contenthandler, metadata, new ParseContext());
			result = contenthandler.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return result;
	}
	public String GetHtmlHeaders(String html)
	{
		//TODO:
		return html;
		
	}
}
