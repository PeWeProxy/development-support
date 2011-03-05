package sk.fiit.peweproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLUtils {
	public static void appendVariables (Document globalDocument, Document localDocument) {
		
		Node variableElement = localDocument.getDocumentElement().getFirstChild().getNextSibling();
		
		while (variableElement != null){
			Element element = globalDocument.createElement("variable");
			element.setAttribute("name", variableElement.getAttributes().getNamedItem("name").getNodeValue());
			element.setTextContent(variableElement.getTextContent());
			
			globalDocument.getDocumentElement().appendChild(element);
			variableElement = variableElement.getNextSibling().getNextSibling();
		}
	}
	
	public static void writeDocument (Document globalDocument) throws IOException{
		OutputFormat format = new OutputFormat(globalDocument);
		format.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(
				new FileOutputStream(new File("plugins/variables.xml")), format); 
		serializer.serialize(globalDocument);
	}
}
