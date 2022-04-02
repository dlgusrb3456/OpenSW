import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class makeCollection {
    private String path = "";
    public makeCollection(String path) {
        this.path=path;
    }

    public static File[] getFile(String path){
        File dir = new File(path);
        return dir.listFiles();
    }

    public void mkCollecton() throws ParserConfigurationException, IOException, TransformerException {
        File[] files = getFile(path);
        //"C:\\Users\\dlgus\\OneDrive\\바탕 화면\\SimpleLR\\dkfj"
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = docBuilder.newDocument();
        Element docs = doc.createElement("docs");
        doc.appendChild(docs);

        for(int i=0;i<files.length;i++){
            Document fileDoc = Jsoup.parse(files[i], "UTF-8");
            Element docID = doc.createElement("doc");
            docs.appendChild(docID);
            docID.setAttribute("id",Integer.toString(i));
            Element docTitle = doc.createElement("title");
            Element docBody = doc.createElement("body");
            docTitle.appendChild(doc.createTextNode(fileDoc.title()+'\n'));
            docBody.appendChild(doc.createTextNode(fileDoc.body().text()+'\n'));
            docID.appendChild(docTitle);
            docID.appendChild(docBody);
        };

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(new File("collection.xml")));
        transformer.transform(source,result);
    }
}
