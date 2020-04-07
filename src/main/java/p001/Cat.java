/**
 * @author  Eric Zair (zairea200@potsdam.edu)
 * @file  p001/Cat.java
 * @brief  Program that simulates the effect of the linux "cat" command line
 * @version  0.1
 * @date  01/28/2020
 *
 */


package p001;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Cat {


    public static void main(String[] args) {
        // Arguments were not given to the program, so there is no point in continuing.
        if (args.length == 0) {
            System.err.println("This Program requires AT LEAST one argument for a filename.");
            System.out.println("cat.java <name_of_file>");
            System.out.println("If using gradle: 'gradle run' --args <name_of_file(s)");
        }

        for (String filenameGivenToProgram : args) {
            System.out.println("Output of " + filenameGivenToProgram + ":");
            printFileContent(filenameGivenToProgram);
        }
    }


    /**
     * This methods takes the name of a file (as a string) and prints out all of the
     * content in the file directly to the terminal.
     *
     * @param  filename  This is the filename of the file that that will be printed to
     *                   the terminal.
     *
     * @exception  IOException  Caught in the event that the file the user is trying to read from does not exist.
     *
     */
    public static void printFileContent(String filename) {
        try {
            SansLittleUFilterReader commentRemoveReader = new SansLittleUFilterReader(new FileReader(filename));
            Scanner fileScanner = new Scanner(commentRemoveReader);

            while (fileScanner.hasNextLine())
                System.out.println(fileScanner.nextLine());

            fileScanner.close();
            commentRemoveReader.close();
        }
        catch (IOException e) {
            System.err.println("The file \"" + filename + "\" " + "does not exist. Skipping over it...\n");
        }
    }
}
