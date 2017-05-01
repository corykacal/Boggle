import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Dictionary {

    public Dictionary() {

    }


    public static TreeSet<String> getWords(String file) throws FileNotFoundException {
        
        Scanner input =  new Scanner(new File(file));
       
        TreeSet<String> words = new TreeSet<String>();
        while(input.hasNext()) {
            words.add(input.next());
        }
        input.close();
        return words;
    }
}
