import java.util.*;
/**
 * Enumeration class Language - This enum represents a given programming 
 * language and its given command line arguments to compile/run a program written in said language.
 * 
 * Each language parameters are as follows:
 * First Parameter: A String[] indicating how to compile (if possible) a file in this language
 * Second Parameter: A String[] indicating how to run a file in this language
 * 
 * Note: For running locally, you may need to change the compile and run parameters.
 * If you have any issues running code locally, try running the code in your own command
 * line to ensure that these are correct (you may need to update your environment variables).
 * Also note that the ProcessBuilder class is a bit strange and will not update the current
 * directory of the automated virtual command line (even after the dir() method), so that
 * is another issue that you may encounter when running locally.
 * 
 * @author Fun
 * @version Fun
 */
public enum Language
{

    JAVA (new String[]{"javac", "Solution.java"}, new String[]{"java", "Solution"}),
    
    PYTHON (null, new String[]{"python", "Solution.py"}),
    
    C (new String[]{"gcc", "Solution.c", "-o", "Solution.o"}, new String[]{"./Solution.o"}), // Using gcc
    
    CPP (new String[]{"g++", "Solution.cpp", "-o", "Solution.o"}, new String[]{"./Solution.o"}), // Using g++
    
    CSHARP (new String[]{"csc", "Solution.cs"}, new String[]{"..\\Solution"}), // Using .NET
    
    JAVASCRIPT(null, new String[]{"node", "Solution.js"}); // Using Node.js

    String[] compileCmd;
    String[] runCmd;
    /**
     * Simple constructor for a Language
     */
    Language(String[] c, String[] r){
        compileCmd = c;
        runCmd = r;
    }

    /**
     * This method merely returns whether this langauage is a compilable language
     */
    boolean isCompilable(){
        return compileCmd != null;
    }

    /**
     * This method retrieves a requested command with a custom name (other than say, Solution)
     * @param name The name of the actual program
     * @param isCompileCmd Whether you would like to retrieve the compileCmd, runCmd otherwise
     * 
     * @return A String[] representing the correct command
     */
    String[] getCmd(String name, boolean isCompileCmd){
        String[] cur = isCompileCmd ? compileCmd : runCmd; 
        if(cur == null){
            return null;
        }
        String[] copy = new String[cur.length];
        System.arraycopy(cur, 0, copy, 0, copy.length);
        for(int i = 0; i < copy.length; i++){
            copy[i] = copy[i].replace("Solution", name);
        }
        return copy;
    }

    /**
     * This method gets the language enum value from a given string
     * 
     * @param lang The String value of the language (i.e. "Java")
     * @return The Language enum value for said string
     */
    public static Language getLang(String lang){
        lang = lang.toUpperCase();
        // Cases where 'lang' includes non-alphabetical characters
        if(lang.equals("C++")){
            lang = "CPP";
        }
        else if(lang.equals("C#")){
            lang = "CSHARP";
        }
        Language language = valueOf(lang);
        return language;
    }
}
