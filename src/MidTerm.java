import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.File;
import java.io.IOException;

public class MidTerm {
    String qString="";
    String path="";
    public MidTerm(String path,String qString){
        this.qString=qString;
        this.path =path;
    }

    public void showSnippet() throws IOException {
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(qString,true);

        for(int k=0;k<kl.size();k++){
            Keyword kwrd = kl.get(k);
            kwrd.getString();
        }

        for(int i=0;i<5;i++){
            int tempAnswer = 0;
            String tempAnswerStr = "";
            String title = getTitle(i);
            String text = getText(i);
            for(int j=0;j<text.length()-30;j++){
                String tempSnippet = "";
                for(int k=j;k<j+30;k++){
                    tempSnippet+= text.charAt(k);
                }
                //System.out.println(tempSnippet);
                String tempSnippetArr[] = tempSnippet.split(" ");
                int temps=0;
                for(int u=0;u<kl.size();u++){
                    Keyword kwrd = kl.get(u);

                    for(int l =0;l<tempSnippetArr.length;l++){
                        if(tempSnippetArr[l].equals(kwrd.getString())){
                            temps++;
                        }
                    }
                }
                if(temps>tempAnswer){
                    tempAnswer=temps;
                    tempAnswerStr=tempSnippet;
                }
            }
            if(tempAnswer>0){
                System.out.println(title+", "+tempAnswerStr+ ", " +Integer.toString(tempAnswer));
            }
        }
    }
    public String getTitle(int index) throws IOException {
        //get collection file to get title
        File collectionFile = new File(path);
        Document fileDoc = Jsoup.parse(collectionFile, "UTF-8");
        Elements els = fileDoc.select("doc");
        return els.get(index).select("title").text();
    }

    public String getText(int index) throws IOException {
        //get collection file to get title
        File collectionFile = new File(path);
        Document fileDoc = Jsoup.parse(collectionFile, "UTF-8");
        Elements els = fileDoc.select("doc");
        return els.get(index).textNodes().get(0).text();
    }
}
