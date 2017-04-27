
import java.io.*;
import java.sql.*;  
import java.util.*;
/**
 * This program checks a program against a certain number of test cases.
 * 
 * @author Fun
 * @version Fun
 */
public class CheckProgram
{
    /**
     * The main program.
     * 
     * @param args Command Line Arguments
     * @param args[0] The name of the program to run
     * @param args[1] The language of the program
     * @param args[2] The directory of the program
     * @param args[3] The id of the problem
     * @param args[4] The maximal tcID (this program will check from tc(1) to tc(args[4])
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

            File input = new File(tcPath);;
            String correctOut;
            try{
                correctOut = Utils.fts(new File(outPath));
            }catch(IOException e){
                continue;
            }

            Response res = p.runMain(input, 2000);
            String output = res.getOutput().toString();

            res.updateStatus(correctOut);

            System.out.print(res.getStatus());
        }

        /*
        List<List<String>> testCases = Utils.executeQuery("select * from TEST_CASES where ProblemID = " + args[3] + " order by tcID asc");
        for(int r = 0; r < testCases.size(); r++){
        // For each test case
        List<String> row = testCases.get(r);
        String input = row.get(2);
        String output = row.get(3);

        Response response = p.runMain(input, 2000);
        String resOut = response.getOutput().toString();
        if(response.programFinished()){
        if(output.trim().equals(resOut.trim())){
        response.setStatus(Response.AC);
        }
        else{
        response.setStatus(Response.WA);
        }
        }

        System.out.print(response.getStatus());
        //System.out.println(response.getOutput());
        //System.out.println("__________");
        }
         */
    }
}
