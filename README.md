# ID Block Information

* @author Eric Zair
* @email zairea200@potsdam.edu

## Description

The Goal of this project is to do two main things:

### (1) Simulate the Linux command line program called `cat`

The normal linux cat program takes command line arguments which are the names of some given files. The cat program simply takes the name of those files and prints out the file content to the terminal.

However, the version that I have does the same thing except it removes all scheme comments from the output that is sent to the terminal.

### (2) Create a class `SansLittleUFilterReader` that extends Java's `FilterReader` class

A filter reader is essentially a type of abstract `Reader` that is also implements the `Readable` interface in java.

The class that I will be making will extend `FilterReader` and take a `Reader` as an argument to its constructor. Instead I will then override the each and everyone one of the `read()` methods (and the `skip()` method) to make it read any data from the passed in `Reader`, but also ignore any scheme comments that are contained in the given `Reader`. (Examples wil be show in the testing section).

## How To Compile && run

### Building/Compiling the program

To build/compile the program, simply run the following command:

`gradle clean build`

This will build the program for you (assuming that you have gradle installed on your computer).

### Running the program

After building the program, simply run it by doing the following:

`gradle run --args <file_to_pass_in_to_program>`

Note that if you do not run this with the file name, you will receive the following message from the program:

```Ignore
This Program requires AT LEAST one argument for a filename.
cat.java <name_of_file>
If using gradle: 'gradle run' --args <name_of_file(s)
```

### Build & Run all in one

Alternatively you can build and run the program in one step by doing the following:

`gradle clean build run --args <file_to_pass_in_to_program>`

## Testing

In order to test the program, I have build a few text files to test with. These test files are located in the root project directory's `tests/` folder. `test1.txt` is a more intense test that covers edge cases. `test2.txt` is a more basic test that proves the functionality in a simple and easy to understand example.

Below I will show an example of the Cat program, followed by my implementation of the Cat program.

### Output of the Linux Cat program

```Ignore
This is a test...#

    And this is the second ; line of the test.
Still doing testing and stuffs #;

Woot woot
    I'm here now :)

    #|This is should be deleted|# but not this; this can.

        Still though: #| here to | right about here |# should be taken out.

                Finally, #| from here #| to al the way over here | was |# taken out ;
#   :)
```

### Output of my Cat program

```Ignore
Task :run
Output of tests/test1.txt:

     This is a test...#

    And this is the second
Still doing testing and stuffs #

Woot woot
    I'm here now :)

      but not this

        Still though:   should be taken out.

                Finally,   taken out
#   :)

BUILD SUCCESSFUL in 1s
9 actionable tasks: 9 executed

```

NOTE: My program intentionally adds the line that says "Output of tests/test1.txt" for the sake of when multiple files are passed into it.

ALSO The very bottom line shown is a result of gradle itself :)

## Doxygen & Documentation

Comments in this program are done in a format that supports using doxygen to generate .html documentation.

### Generating Doxygen Documentation

To Generate doxygen documentation make sure that doxygen is properly installed on your system.

If it is not installed on your system and you are running linux, run the following command:

`sudo apt-get install doxygen`

This will install doxygen on your computer.

### The generate_open_docs.py script

To do a quickly generate and open documentation, simply run the command `./generate_open_docs.py` and the documentation will be built and then opened up in your web browser.

NOTE: If you already have a web browser open, then the documentation will open in that window.

Now that doxygen is installed on your computer, you can run the following command to generate the documentation:

`cd doc; doxygen Doxyfile`

Once executed, the documentation will be generated successfully.

To Easily view it, inside of the `doc/` folder look for `html/index.html` and open it.
