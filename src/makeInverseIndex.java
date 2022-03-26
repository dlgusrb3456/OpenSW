import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Element;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class makeInverseIndex {
    int N = 5;
    public makeInverseIndex(String path){

    }

    public void mkHashMapFile() throws IOException {
        HashMap[] hashs = new HashMap[N];
        File file = new File("C:\\Users\\dlgus\\OneDrive\\바탕 화면\\SimpleLR\\src\\index.xml");
        Document fileDoc = Jsoup.parse(file, "UTF-8");
        Elements els = fileDoc.select("doc");
        for(int i=0;i<els.size();i++){
            //System.out.println(els.get(i).select("title").text());
            String[] words =els.get(i).textNodes().get(0).text().split("#");
            HashMap tempHash = new HashMap();
            for(int j=0;j<words.length-1;j++){
                String[] tempKeyVal = words[j].split(":");
                //System.out.println(tempKeyVal.length);
                tempHash.put(tempKeyVal[0],tempKeyVal[1]);
            }
            hashs[i]=tempHash;
        }

        FileOutputStream fileStream = new FileOutputStream("src/index.post");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
        objectOutputStream.writeObject(hashs);
        objectOutputStream.close();
    }

    public void rdInverseIndex() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("src/index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap[] hashs = (HashMap[]) object;
        double tfxy = 0.0;
        double dfxy= 0.0;
        HashMap All = new HashMap();
        for(int i=0;i<hashs.length;i++){
            Iterator<String> it = hashs[i].keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                double value = Double.parseDouble((String)hashs[i].get(key));
                if(All.containsKey(key)){
                    double tempKeyVal = (double) All.get(key);
                    All.put(key,value+tempKeyVal);
                }
                else {
                    All.put(key, value);
                }
            }
        }
        Iterator<String> it = All.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            double value = (double) All.get(key);
            String answer = "";
            answer+=key+" -> ";
            double[] counts = new double[5];
            double totalHave = 0;
            for(int i=0;i<hashs.length;i++){
                Object tempValue = hashs[i].get(key);
                if(tempValue != null){
                    counts[i]=Double.parseDouble((String)tempValue);
                    totalHave+=1.0;
                }else{
                    counts[i] = 0.0;
                    continue;
                }
            }
            for(int i=0;i<counts.length;i++){
                answer+= Integer.toString(i)+" ";
                double weight = counts[i]*Math.log(5.0/totalHave);
                answer+= String.format("%.2f",weight)+" ";
            }

            System.out.println(answer);
        }
    }
}
