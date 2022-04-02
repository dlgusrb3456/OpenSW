import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.*;
import java.util.*;

public class searcher {
    private String path="";
    private String findStr="";
    public searcher(String path,String findStr){
        this.path=path;
        this.findStr=findStr;
    }

    public static File[] getFile(String path){
        File dir = new File(path);
        return dir.listFiles();
    }

    public void mkSearcher() throws IOException, ClassNotFoundException {
        //get index.post file
        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap<String,String[]> hashs = (HashMap<String, String[]>) object;
        //makeInverseIndex mI = new makeInverseIndex();
        //mI.rdInverseIndex(object);

        //make kkm list
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(findStr,true);

        //test kkm
//        for(int i=0;i<kl.size();i++){
//            Keyword kw = kl.get(i);
//            System.out.println(kw.getString()+" "+kw.getCnt());
//        }


        for(int i=0;i<kl.size();i++){
            Keyword kwrd = kl.get(i);
            if(hashs.containsKey(kwrd.getString())){
                Map<Integer,Double> tmpMap = new HashMap<Integer,Double>(); //Integer = fileIndex, Double = each file's weight
                String[] tmpString = hashs.get(kwrd.getString()); //each file's weight, 0 to 4
                for(int j=0;j<tmpString.length;j++){
                    Double tmpDouble = Double.parseDouble(tmpString[j]);
                    tmpDouble *= (double)kwrd.getCnt(); //change value to find top three weight file. (* new keyword weight)
                    tmpMap.put(j,tmpDouble);
                }
                //current status: tmpMap have <file index, file weight> of key String
                //to do: sort tmpMap to choose top three file index
                List<Map.Entry<Integer,Double>> list_entries = new ArrayList<Map.Entry<Integer, Double>>(tmpMap.entrySet());
                Collections.sort(list_entries, new Comparator<Map.Entry<Integer,Double>>(){

                    @Override
                    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                //check sort
                int k=0;
                String answer = "("+kwrd.getString()+") ";
                for(Map.Entry<Integer,Double> entry: list_entries){
                    if(k>=3){
                        break;
                    }
                    k++;
                    String title = getTitle(entry.getKey());
                    answer += Integer.toString(k)+"등:"+title+" ";
                }
                System.out.println(answer);


            }
            else{
                System.out.println("No "+kwrd.getString()+" word in files");
            }

        }



    }

    public String getTitle(int index) throws IOException {
        //get collection file to get title
        File collectionFile = new File("./src/collection.xml");
        Document fileDoc = Jsoup.parse(collectionFile, "UTF-8");
        Elements els = fileDoc.select("doc");
        return els.get(index).select("title").text();

    }

}
