
/**
 * This Program merely runs a program from an input file
 * 
 * @author Fun
 * @version Fun
 */
public class RunProgramWithFileInput
{
    /**
     * The main program.
     * 
     * @param args Command Line Arguments
     * @param args[0] The name of the program to run
     * @param args[1] The language of the program
     * @param args[2] The directory of the program
     * @param args[3] The input file
     * @param args[4] The output file
     * @param args[5] The Time Limit of the program (in milliseconds)
     */
    public static void main(String[] args){
        
        if(args.length != 6){
            System.out.println("Wrong number of parameters - actual length = " + args.length);
            return;
        }

        Program p = new Program(args[0], Language.getLang(args[1]), args[2]);
        
        //String tcPath = "../files/Problems/p" + args[3] + "/tcs/tc" + args[4] + "in.txt";
        //String outPath = "../files/Problems/p" + args[3] + "/tcs/tc" + args[4] + "out.txt";
        long timeLimit = Long.parseLong(args[5]);
        GenerateTC.run(p, args[3], args[4], timeLimit);
    }
}
