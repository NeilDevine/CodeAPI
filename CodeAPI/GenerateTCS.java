
import java.io.*;
import java.sql.*;  
import java.util.*;
/**
 * This standalone program generates and stores a given test case (tested against a correct program).
 * 
 * This program was used when creating a problem to test against correct input.
 * 
 * @author Fun
 * @version Fun
 */
public class GenerateTCS
{
    /**
     * The main program.
     * 
     * @param args Command Line Arguments
     * @param args[0] The name of the Correct Program
     * @param args[1] The language of the Correct Program
     * @param args[2] The directory of the program
     * @param args[3] The ProblemID
     * @param args[4] The maximal tcID
     */
    public static void main(String[] args){
        if(args.length != 5){
            return;
        }

        Program p = new Program(args[0], Language.getLang(args[1]), args[2]);
        
        int tc = Integer.parseInt(args[4]);

        for(int i = 1; i <= tc; i++){
            String tcPath = "../files/Problems/p" + args[3] + "/tcs/tc" + i + "in.txt";
            String outPath = "../files/Problems/p" + args[3] + "/tcs/tc" + i + "out.txt";
            
            GenerateTC.run(p, tcPath, outPath, 2000);
        }
    }
}
