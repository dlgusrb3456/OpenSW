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
    private String path ="";
    public makeInverseIndex(String path){
        this.path=path;
    }
    public makeInverseIndex(){

    }

    public void mkHashMapFile() throws IOException {
        HashMap[] hashs = new HashMap[N]; //5개의 문장별 hashmap배열
        File file = new File(path);
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

        HashMap All = new HashMap(); //5개의 파일에서 나온 단어 총정리.
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

        HashMap<String,String[]> resultHash = new HashMap<>();
        //.post에 저장될 hashmap, String에 단어 이름 String[]에 파일별 가중치.
        /*
        <밀 ,[0.00, 0.00, 0.00, 0.00, 0.00]>
         */
        Iterator<String> it = All.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            double value = (double) All.get(key);
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
            String[] tmpWeight= new String[5];
            for(int i=0;i<counts.length;i++){
                double weight = counts[i]*Math.log(5.0/totalHave);
                tmpWeight[i]=String.format("%.2f",weight);
            }
            resultHash.put(key,tmpWeight);
        }

        FileOutputStream fileStream = new FileOutputStream("src/index.post");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
        objectOutputStream.writeObject(resultHash);
        objectOutputStream.close();
    }

    public void rdInverseIndex(Object object) throws IOException, ClassNotFoundException {
        //.post에 저장된 hashmap을 불러와서 출력하기.
        FileInputStream fileStream = new FileInputStream("src/index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object1 = objectInputStream.readObject(); //여기서 찾은 object
        objectInputStream.close();

        HashMap<String,String[]> hashs = (HashMap) object; //받아온 object


        Iterator<String> it = hashs.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String[] value = hashs.get(key);
            String answer = "";
            answer+=key+" -> ";

            for(int i=0;i<value.length;i++){
                answer+= " "+Integer.toString(i)+" "+value[i];
            }
            System.out.println(answer);
        }
    }
}
