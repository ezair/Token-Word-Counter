/**
 * @author  Eric Zair (zairea200@potsdam.edu)
 * @file  p001/SansLittleUFilterReader.java
 * @brief  This module contains the SansLittleUFilterReader class, used to filter out scheme
 *         comments from whatever the data the reader given to it contains.
 * @version  0.1
 * @date 0 1/28/2020
 *
 */

package p001;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;


/**
 * This class is used specifically to filter out scheme comments from a Reader
 * object. It works with scanners and the Readable interface.
 *
 * @extends FilterReader
 *
 */
public class SansLittleUFilterReader extends FilterReader {


    /**
     * Holds the most recent value read from our reader.
     *
     */
    private int currentValueReadFromReader;

    /**
     * CONSTANT Value that signifies we have reached the end of the reader.
     *
     */
    private final int END_OF_READER = -1;

    /**
     * This field tells us whether or not we are in a multi-line-comment when we are calling a read() method.
     *
     */
    private boolean inMultiLineComment;

    /**
     * Lets us know if the value that we just read out of the read has not yet been printed out.
     * This is very useful for catching edge cases with '#' and '|' being read.
     *
     */
    private boolean currentValueReadFromReaderHasBeenReturnedAlready = true;


    /**
     * The passed in reader will be set to the reader that is used inside of this class, 'this.in'.
     *
     * @DEFAULT_CONSTRUCTOR  SansLittleUFilterReader
     *
     * @param  reader  Contains the data that the user passed into this reader.
     *                 This will serve as the reader that we wrap our SansLittleUFilterReader around.
     *
     */
    public SansLittleUFilterReader(Reader reader) {
        super(reader);
        this.in = reader;
    }


    // PRIVATE HELPER METHODS__________________________________________________________________________________________


    /**
     * Gets the character that is directly after a single line comment, or -1.
     *
     * @return  Character following a single line line comment. This is either a '\n' or END_OF_READER.
     *
     * @throws IOException  This is required to be thrown, but will not cause an issue.
     *
     */
    private int getCharacterAfterSingleLineComment() throws IOException {
        while (currentValueReadFromReader != '\n' && currentValueReadFromReader != END_OF_READER)
            currentValueReadFromReader = in.read();
        return currentValueReadFromReader;
    }


    /**
     * Parse through reader and get the next valid character in a multi-line-comment.
     *
     * @return  The next valid character in a multi-line-comment.
     *          ' ' if at end of multi-line-comment.
     *          -1 if at end of reader.
     *
     * @throws  IOException  required to throw, but will not not occur in this method.
     *
     */
    private int parseAndHandleCharactersInAMultiLineComment() throws IOException {
        while (inMultiLineComment) {
            // '|#' is the symbol for the end of a multi-line-comment.
            if (currentValueReadFromReader == '|') {
                currentValueReadFromReader = in.read();

                if (currentValueReadFromReader == '#') {
                    inMultiLineComment = false;
                    return ' ';
                }
            }

            // Yes we are in a comment, but we still care about properly spacing our file.
            if (currentValueReadFromReader == END_OF_READER || currentValueReadFromReader == '\n')
                return currentValueReadFromReader;
            currentValueReadFromReader = in.read();
        }

        // This case will not be hit in our public read() method.
        return END_OF_READER;
    }


    // PUBLIC INTERFACE________________________________________________________________________________________________


    /**
     * Reads the next character in the reader.
     *
     * @brief Super implementation of read() in filter reader class.
     *
     * @return  number of characters read, or -1 if the end of the stream has been reached.
     *
     * @throws IOException  This issue will not occur in this method, but is required to throw.
     *
     */
    @Override
    public int read() throws IOException {
        if (currentValueReadFromReaderHasBeenReturnedAlready)
            currentValueReadFromReader = in.read();
        else
            // currentValueReadFromReader has a value in it that has not been processed yet.
            currentValueReadFromReaderHasBeenReturnedAlready = true;

        // '|' might be the next thing that appears, but we don't know, so we store this for now.
        if (currentValueReadFromReader == '#' && !inMultiLineComment) {
            currentValueReadFromReader = in.read();

            if (currentValueReadFromReader == '|') {
                inMultiLineComment = true;
                currentValueReadFromReader = '\0';
            }
            else {
                // Edge Case:  We saw a '#' symbol, but did not read it out since it might have been a bar.
                currentValueReadFromReaderHasBeenReturnedAlready = false;
                return '#';
            }
        }

        // Anything that has to do with parsing through a multi-line-comment is done in the returned method :)
        if (inMultiLineComment)
            return parseAndHandleCharactersInAMultiLineComment();

        // ';' is a single line comment in scheme. Need to read until end of line.
        if (currentValueReadFromReader == ';' && !inMultiLineComment) {
            return getCharacterAfterSingleLineComment();
        }

        // Valid regular character is returned.
        return currentValueReadFromReader;
    }


    /**
     * Reads characters into an array. The reader then moves up by the amount of characters that were read.
     * If we hit the end of the reader, we stop incrementing the characters that follow.
     *
     * @brief Super implementation of read(char[], int, int) in the FilterReader class.
     *
     * @param  buffer  This is the array that we will put all of the characters that were read into.
     * @param  startingIndexToReadTo  This is the index that we will being to read chars into.
     * @param  endingIndexToReadTo  This is the last index that will be reading chars into.
     *
     * @return  number of characters read; -1 if empty.
     *
     * @throws  IOException  This issue will not occur in this method, but is required to throw.
     *
     */
    @Override
    public int read(char[] buffer, int startingIndexToReadTo, int endingIndexToReadTo) throws IOException {
        int valueReadInFromReader = read();
        if (valueReadInFromReader == END_OF_READER)
            return END_OF_READER;

        // Had to pull a fencepost above, so we need to account for that (cleanup work).
        buffer[startingIndexToReadTo] = (char) valueReadInFromReader;
        int countOfReads = 1;

        for (int i = startingIndexToReadTo + 1; i < endingIndexToReadTo; i++) {
            valueReadInFromReader = read();
            if (valueReadInFromReader == END_OF_READER)
                return countOfReads;

            buffer[i] = (char) valueReadInFromReader;
            countOfReads++;
        }
        return countOfReads;
    }


    /**
     * @brief  Super implementation of the read(CharBuffer) method in the extended FilterReader class.
     *         We need to override this so that our reader will work with scanners and such.
     *
     * @param  targetBuffer  The buffer that we are going to fill with 0 to length amount of chars from our reader.
     *
     * @return  The number of characters added to the buffer, or -1 if this source of characters is at its end.
     *
     * @throws  IOException  This issue will not occur in this method, but is required to throw.
     *
     */
    @Override
    public int read(CharBuffer targetBuffer) throws IOException {
        char[] cbuffer = new char[targetBuffer.remaining()];
        int n = read(cbuffer, 0, cbuffer.length);
        if (n > 0)
            targetBuffer.put(cbuffer, 0, n);
        return n;
    }


    /**
     * Skips a given amount of characters in our reader.
     *
     * @param  amountOfCharactersToSkip  amount of characters to skip.
     *
     * @return  Number of characters that were skipped in the reader.
     *
     * @throws IOException  Required to throw, but no issue will occur here.
     */
    @Override
    public long skip(long amountOfCharactersToSkip) throws IOException {
        if (amountOfCharactersToSkip < 0)
            throw new IllegalArgumentException("N cannot be negative.");

        int numberOfSkips = 0;
        for (numberOfSkips = 0; numberOfSkips < amountOfCharactersToSkip; numberOfSkips++)
            if (read() != END_OF_READER)
                numberOfSkips++;
        return numberOfSkips;
    }
}
