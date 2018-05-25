package search_engine_hw2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;


public class appendfile {
    public static void appendfile(String permFile, String tmpFile) throws IOException {
    // Appends file "tmpFile" to the end of "permFile"
    // Code adapted from:  https://www.daniweb.com/software-development/java/threads/44508/appending-two-java-text-files

        try {
	        File storageFolder = new File(permFile);
	        if (!storageFolder.exists()) storageFolder.createNewFile();
	       
	        // create a writer for permFile
	        BufferedWriter out = new BufferedWriter(new FileWriter(permFile, true));
	        // create a reader for tmpFile
	        BufferedReader in= new BufferedReader (new FileReader(tmpFile));
	        
	        String str;
	        while ((str = in.readLine()) != null) {
	            out.write(str);
	            out.newLine();
	            }
	        in.close();
	        out.close();
        } catch (IOException e) {
        }
    }
    
    public static void appendfile_with_text(String permFilename, String filetext) throws IOException {
        // Appends file "tmpFile" to the end of "permFile"
        // Code adapted from:  https://www.daniweb.com/software-development/java/threads/44508/appending-two-java-text-files

            try {
    	        File storageFolder = new File(permFilename);
    	        if (!storageFolder.exists()) storageFolder.createNewFile();
    	       
    	        // create a writer for permFile
    	        BufferedWriter out = new BufferedWriter(new FileWriter(permFilename, true));
    	        // create a reader for tmpFile
    	        BufferedReader in= new BufferedReader (new StringReader(filetext));
    	       
    	        String str;
    	        while ((str = in.readLine()) != null) {
    	            out.write(str);
    	            out.newLine();
    	            }
    	        in.close();
    	        out.close();
            } catch (IOException e) {
            }
        }
    public static void deletefile(String filename) {
	    try
	    {
	        Files.deleteIfExists(Paths.get(filename));
	    }
	    catch(NoSuchFileException e)
	    {
	        System.out.println("No such file/directory exists");
	    }
	    catch(DirectoryNotEmptyException e)
	    {
	        System.out.println("Directory is not empty.");
	    }
	    catch(IOException e)
	    {
	        System.out.println("Invalid permissions.");
	    }
    }
}
