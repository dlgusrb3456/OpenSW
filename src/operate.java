import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class operate {


    public static void main(String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException, TransformerException {
        String command = args[2];// -i
        String path = args[3];

        if(command.equals("-i")){ //config: java kuir -i ./src/index.xml
            indexer mII = new indexer(path);
            mII.mkHashMapFile();
        } else if (command.equals("-k")) { //config: java kuir -k ./src/collection.xml
            makeKeyword mK = new makeKeyword(path);
            mK.mkKeyword();
        }else if(command.equals("-c")){ //config: java kuir -c ./data
            makeCollection mC = new makeCollection(path);
            mC.mkCollecton();
        }
        else if(command.equals("-s")){
            searcher sc = new searcher(path,args[5]);
            sc.mkSearcher();
        }
        else {
            System.out.println("none");
        }


    }
}
