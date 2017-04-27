
import java.util.*;
import java.io.*;
/**
 * This class represents a program which may be ran or compiled.
 * 
 * @author Fun
 * @version Fun
 */
public class Program
{
    String name; // Stores the name of the program (for example: "Solution")
    File dir; // Stores the directory of this file
    Language lang; // Stores the language this program is written in
    String[] compileCmd; // Stores the command needed to compile this program
    String[] runCmd; // Stores the command needed to run this program
    boolean compiled; // Whether this Program has been compiled
    /**
     * Constructor for objects of class Program
     * 
     * @param name The name of this program
     * @param lang The language which this program is written in
     * @param dirPath The path of this program
     */
    public Program(String name, Language lang, String dirPath)
    {
        this.name = name;
        this.lang = lang;
        dir = new File(dirPath);
        compileCmd = lang.getCmd(name, true);
        runCmd = lang.getCmd(name, false);
        compiled = false;
    }

    /**
     * This method compiles this program if it can be compiled
     * 
     * @param args The Arguments for this compile
     */
    public Response compile(String... args){
        if(!lang.isCompilable()){
            return null;
        }
        compiled = true;
        ProcessBuilder builder = new ProcessBuilder(compileCmd);
        builder.directory(dir);
        Process process = null;
        try{
            process = builder.start();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

        InputStream err = process.getErrorStream();
        BufferedReader errReader = new BufferedReader(new InputStreamReader(err));
        StringBuilder build = new StringBuilder();
        String line = "";
        Response res = new Response("", 0);
        try{
            while((line = errReader.readLine())!= null){
                compiled = false;
                res.appendErr(line+"\n");
                res.setStatus(Response.COMPILE_TIME_ERROR);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * This method runs the main program of this program using STD:IN and STD:OUT
     * 
     * @param input The file of the input of the program (in STD:IN)
     * @param timeLimit The timeframe in which this program must run
     * @param args The Command Line arguments (if any)
     * 
     * @return The response from this program (including any errors)
     */
    public Response runMain(File input, final long timeLimit, String... args){
        // Compile the code if you need to

        if(lang.isCompilable() && !compiled){
            Response compileResponse = compile();
            if(compileResponse == null || compileResponse.getStatus() == Response.COMPILE_TIME_ERROR){
                return compileResponse;
            }
        }

        // Now that the code is compiled (if need be), you can run it

        Response res = new Response("", 0);
        ProcessBuilder builder = new ProcessBuilder(runCmd)
            .redirectInput(input); // Redirect the input to the input file itself
            
        builder.directory(dir);
        Process process = null;
        try{
            process = builder.start();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

        InputStream pOut = process.getInputStream(); // STD:OUT
        InputStream pErr = process.getErrorStream(); // STD:ERR

        InputHandler stdErrHandler = new InputHandler(process.getErrorStream());
        Thread erThread = new Thread(stdErrHandler);
        erThread.start();
        InputHandler stdOutHandler = new InputHandler(process.getInputStream());
        Thread outThread = new Thread(stdOutHandler);
        outThread.start();

        // Check if the process has exceeded its time limit (and kill it if it has) - Yes, it's ugly

        if(!waitFor(process, timeLimit)){
            res.setStatus(Response.TLE);
        }

        // Wait for both threads to finish reading the output and error...

        try{
            erThread.join();
            outThread.join();
        }catch(InterruptedException e){ // This really shouldn't execute...
            e.printStackTrace(); // But just in case
        }

        // Read from STD:ERR

        res.setErr(new StringBuilder(stdErrHandler.output));
        if(res.getStatus() != Response.TLE && process.exitValue() != 0){
            res.setStatus(Response.RUNTIME_ERROR);
        }

        // Read from STD:OUT

        res.setOutput(new StringBuilder(stdOutHandler.output));
        
        // Close streams
        try{
            pErr.close();
            pOut.close();
        }catch(IOException e){

        }

        return res;
    }

    /**
     * This is a psuedo-implementation of the Java 1.8 Process.waitFor(long amount, TimeUnit units)
     * method. Since we don't have Java 1.8 on our server, I have implemented it manually (it's a
     * bit gross though).
     * 
     * @param p The Process to wait for
     * @param time The time, in milliseconds which this method will wait for 'p'
     * 
     * @return Whether 'p' has terminated within 'time' milliseconds
     */
    public static boolean waitFor(Process p, long time){
        long now = System.currentTimeMillis();
        long finish = now + time;
        while ( isAlive( p ) && ( System.currentTimeMillis() < finish ) ){
            try{
                Thread.sleep( 10 );
            }catch(Exception e){}
        }

        if ( isAlive( p ) ){
            p.destroy();
            return false;
        }
        return true;
    }

    /**
     * This method merely checks a Process's exit value to determine if it is alive
     * 
     * @param p The Process which may or may not be alive
     * 
     * @return Whether 'p' is alive
     */
    public static boolean isAlive( Process p ) {
        try{
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }
}

/**
 * This class is an implementation of a thread whose sole purpose is to read the input of
 * an inputstream (so that we may read a programs input even if it doesn't fully finish)
 */
class InputHandler implements Runnable {
    InputStream input;
    StringBuilder output = new StringBuilder();

    InputHandler(InputStream input) {
        this.input = input;
    }

    public void run() {
        try {
            int c;
            int length = 0;

            while ((c = input.read()) != -1) {
                if (((char) c == '\r') || ((char) c == '\n')) {
                    if (length != 0) output.append( '\n' );
                    length = 0;
                } else{
                    output.append( (char) c );
                    length++;
                }
            }
        } catch (Throwable t) {
        }
    }
}
