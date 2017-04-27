
import java.io.*;
import java.sql.*;  
import java.util.*;
/**
 * This program generates and stores a given test case (tested against a correct program)
 * 
 * @author Fun
 * @version Fun
 */
public class GenerateTC
{
    /**
     * The main program.
     * 
     * @param args Command Line Arguments
     * @param args[0] The name of the Correct Program
     * @param args[1] The language of the Correct Program
     * @param args[2] The directory of the program
     * @param args[3] The ProblemID
     * @param args[4] The tcID
     */
    public static void main(String[] args){
        if(args.length != 5){
            return;
        }

        Program p = new Program(args[0], Language.getLang(args[1]), args[2]);

        String tcPath = "../files/Problems/p" + args[3] + "/tcs/tc" + args[4] + "in.txt";
        String outPath = "../files/Problems/p" + args[3] + "/tcs/tc" + args[4] + "out.txt";

        run(p, tcPath, outPath, 2000);
    }

    public static void run(Program p, String tcPath, String outPath, long timeLimit){
        File input = new File(tcPath);
        
        Response res = p.runMain(input, timeLimit);
        if(!res.programFinished()){
            res.writeToFile(outPath, false);
            return;
        }

        res.writeToFile(outPath, true);
    }
}
