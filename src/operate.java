import java.io.FileNotFoundException;
import java.io.IOException;

public class operate {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String command = args[0];// -i
        String path = args[1];//./data

        if(command.equals("-i")){
            makeInverseIndex mII = new makeInverseIndex(path);
            mII.mkHashMapFile();
            mII.rdInverseIndex();
        }
        else{
            System.out.println("none");
        }


    }
}
