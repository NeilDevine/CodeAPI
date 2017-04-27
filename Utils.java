
import java.io.*;
import java.sql.*;
import java.util.*;
/**
 * Simply Utility class
 * 
 * @author Fun
 * @version Fun
 */
public class Utils
{
    /**
     * This method merely converts a file to a string
     */
    public static String fts(File f) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(f));

        String line = "";
        StringBuilder out = new StringBuilder();
        while((line = reader.readLine()) != null){
            out = out.append(line + "\n");
        }
        return out.toString();

    }

    /*
     * This method merely gets a given test case given its tcID and problem ID
     */
    public static File getTC(int probID, int tcID){
        String path = "../files/Problems/p" + probID + "/tcs/tc" + tcID + "in.txt";
        return new File(path);
    }
}
