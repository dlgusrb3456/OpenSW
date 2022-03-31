import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
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

public class makeKeyword {

    private String path="";
    public makeKeyword(String path){
        this.path=path;
    }

    public void mkKeyword() throws ParserConfigurationException, IOException, TransformerException {
        File file = new File(path);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = docBuilder.newDocument();
        Element docs = doc.createElement("docs");
        doc.appendChild(docs);

        Document fileDoc = Jsoup.parse(file, "UTF-8");
        Elements els = fileDoc.select("doc");
        for(int i=0;i<els.size();i++){
            //System.out.println(els.get(i).select("title").text());
            //System.out.println(els.get(i).textNodes().get(0).text());


            Element docID = doc.createElement("doc");
            docID.setAttribute("id",Integer.toString(i));
            docs.appendChild(docID);
            Element docTitle = doc.createElement("title");
            Element docBody = doc.createElement("body");

            docTitle.appendChild(doc.createTextNode(fileDoc.title()+'\n'));

            String setString = els.get(i).textNodes().get(0).text();
            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(setString,true);
            String str = "";
            for(int k=0;k<kl.size();k++){
                Keyword kwrd = kl.get(k);
                str+=kwrd.getString()+':'+kwrd.getCnt()+'#';
            }


            docBody.appendChild(doc.createTextNode(str+'\n'));
            docID.appendChild(docTitle);
            docID.appendChild(docBody);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(new File("src/index.xml")));
        transformer.transform(source,result);
    }


}
