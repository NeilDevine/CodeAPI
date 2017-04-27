
import java.io.*;
import java.util.*;
/**
 * This program merely runs a certain program and prints its output against a single input / test case.
 * 
 * @author Fun
 * @version Fun
 */
public class RunProgram
{
    /**
     * The main program.
     * 
     * @param args Command Line Arguments
     * @param args[0] The name of the program to run
     * @param args[1] The language of the program
     * @param args[2] The directory of the program
     * @param args[3] The problemID
     * @param args[4] The time limit
     * @param args[5] The input file (only for custom test cases)
     */
    public static void main(String[] args){
        if(args.length != 5 && args.length != 6){
            return;
        }

        long timeLimit = Long.parseLong(args[4]);

        Program p = new Program(args[0], Language.getLang(args[1]), args[2]);

        if(args.length == 5){
            runAllTestCases(p, args[3], timeLimit);
        }
        else{
            File inputFile = new File(args[5]);
            runCustomTestCase(p, inputFile, timeLimit);
        }

        //String tcPath = "../files/Problems/p" + args[3] + "/tcs/tc" + args[4] + "in.txt";
        //String outPath = "../files/Problems/p" + args[3] + "/tcs/tc" + args[4] + "out.txt";

        //run(p, tcPath, outPath);
    }

    public static void runAllTestCases(Program p, String probId, long timeLimit){
        String folderPath = "../files/Problems/p" + probId + "/tcs";
        File folder = new File(folderPath);
        File[] allFiles = folder.listFiles();

        System.out.println("{");
        boolean first = true;
        for(int i = 0; i < allFiles.length; i++){
            File inFile = allFiles[i];
            String fileName = inFile.getName();
            if(fileName.endsWith("in.txt")){
                String tc = fileName.substring(0,fileName.length()-6);
                String outFilePath = folderPath + "/" + 
                    tc + "out.txt";
                File outFile = new File(outFilePath);

                String input = null;
                String correctOut = null;
                try{
                    input = Utils.fts(inFile);
                    correctOut = Utils.fts(outFile);
                }catch(IOException e){
                    e.printStackTrace();
                    return;
                }

                Response res = p.runMain(inFile, timeLimit);
                res.updateStatus(correctOut);
                String json = res.writeJSON(input, correctOut);
                if(!first){
                    System.out.println(", ");
                }
                System.out.println("\"" + tc + "\": " + json);
                first = false;
                //String output = res.getOutput().toString();
            }
        }

        System.out.println("}");
    }

    public static void runCustomTestCase(Program p, File inputFile, long timeLimit){
        Response res = p.runMain(inputFile, timeLimit);
        String input = null;
        try{
            input = Utils.fts(inputFile);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }
        
        String json = res.writeJSON(input, null);
        System.out.println(json);
    }

    public static void run(Program p, String tcPath, String outPath){
        File input = new File(tcPath);
        String correctOut;

        try{
            correctOut = Utils.fts(new File(outPath));
        }catch(IOException e){
            System.err.println("Files were not found at " + tcPath + " and " + outPath);
            return;
        }

        Response res = p.runMain(input, 2000);
        String output = res.getOutput().toString();

        res.updateStatus(correctOut);

        System.out.println(res.getStatus());
        if(res.hasError()){
            System.out.println(res.getErr());
        }else{
            System.out.println(res.getOutput());
        }
    }
}
