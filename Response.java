
import java.io.*;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * This class represents a Response given by Program.
 * 
 * @author Fun
 * @version Fun
 */
public class Response
{
    private StringBuilder output; // STD:OUT
    private StringBuilder err = new StringBuilder(); // STD:ERR
    private int status = 0; // An integer representing what this response was (AC, WA, etc.)
    
    // Different options for 'status'
    static final int AC = 0;
    static final int WA = 1;
    static final int RUNTIME_ERROR = 2;
    static final int TLE = 3;
    static final int COMPILE_TIME_ERROR = 4;

    // Different Constructors for a Response

    public Response(StringBuilder out, int s){
        output = out;
        status = s;
    }

    public Response(String out, int s){
        output = new StringBuilder(out);
        status = s;
    }

    public Response(Response o){
        output = o.output;
        status = o.status;
    }

    
    // Getters and Setters
    public StringBuilder getOutput(){
        return output;
    }

    public void setOutput(StringBuilder out){
        output = new StringBuilder(out);
    }
    
    public StringBuilder getErr(){
        return err;
    }

    public void setErr(StringBuilder er){
        err = new StringBuilder(er);
    }

    public int getStatus(){
        return status;
    }

    public void setStatus(int s){
        status = s;
    }

    // Simple method to see whether the program actually finished (or stopped with an error)
    public boolean programFinished(){
        return status < 2;
    }

    // This method checks whether a Response is the same as another response
    public boolean equals(Response o){
        if(programFinished() && o.programFinished()){
            return output.equals(o.output);
        }
        return false;
    }

    // Checks whether this response was an error
    public boolean hasError(){
        return status == RUNTIME_ERROR || status == COMPILE_TIME_ERROR;
    }

    // Checks whether this response got TLE
    public boolean exceededTimeLimit(){
        return status == TLE;
    }

    // Appends to STD:OUT
    public void append(String a){
        output.append(a);
    }
    
    // Appends to STD:ERR
    public void appendErr(String a){
        err.append(a);
    }
    
    public void updateStatus(String correctOut){
        if(status == AC || status == RUNTIME_ERROR){
            if(output.toString().trim().equals(correctOut.trim())){
                setStatus(AC);
            }
            else{
                if(status != RUNTIME_ERROR){
                    setStatus(WA);
                }
            }
        }
    }

    /**
     * This method writes this 
     */
    public boolean writeToFile(String filename, boolean useOut){
        try{
            PrintWriter pw = new PrintWriter(filename);
            String toWrite = useOut ? output.toString() : err.toString();
            BufferedReader fReader = new BufferedReader(new StringReader(toWrite.toString()));
            String line = null;
            while((line = fReader.readLine()) != null){
                pw.println(line);
            }
            fReader.close();
            pw.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This method writes the Response to a JSON object
     * @param input The input of the program
     * @param correctOut The correct output of the response
     * @return a JSON object (stringified) representing the Response
     */
    public String writeJSON(String input, String correctOut){
        String out = StringEscapeUtils.escapeJava(output.toString());
        String inputParsed = StringEscapeUtils.escapeJava(input);
        String errParsed = StringEscapeUtils.escapeJava(err.toString());
        String correctOutParsed = null;
        if(correctOut != null){
            correctOutParsed = StringEscapeUtils.escapeJava(correctOut);
        }
        
        String json = "{\"status\": " + status + ",\n" +
            "\"output\": \"" + out + "\",\n" +
            "\"input\": \"" + inputParsed + "\",\n" +
            "\"error\": \"" + errParsed + "\"";
        if(correctOut != null){
            json += ",\n\"correctOut\": \"" + correctOutParsed + "\"";
        }
        
        json += "}";
        return json;
    }
}
