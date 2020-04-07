---
author:
- 'Brian C. Ladd'
date: Spring 2019
email: 'laddbc@potsdam.edu'
startup: showeverything
subtitle: CIS 443 Programming Languages
title: Making a Sans Comment Filter
---

**Formatting Note** In this document, unless otherwise specified, text
within equal signs, `text` is to be taken as literal text. If you
translate the document with PanDoc, these become typewriter font. The
equal signs are quotes for the literal, not part of the literal itself.
So, for example, `;` is a literal semicolon.

Overview
========

Students will use `git` (to retrieve the course source code repository
and turn in the assignment). They will write a simple program that takes
a file name on the command-line and copies the content of that file to
standard output. After committing that program to version control, they
will implement a comment-stripper (in the form of a `FilterReader`
object) that strips Java style comments from the input stream it
processes. Then the filter will be added to the Java `cat` program.

Getting Started
===============

Use `git` to fetch the course source code from <http://cs-devel.potsdam.edu/>
-----------------------------------------------------------------------------

Look at the code that introduces Gradle in the repository
---------------------------------------------------------

### Make sure you can run the Java "ls" program with command-line parameters from within Gradle.

The "Real" Work
===============

Make a **separate** folder.
---------------------------

### Make sure the **separate** folder is NOT under the folder where you cloned the course repository.

1.  Placing it under the course repo will only lead to pain.

### In the new folder, initialize the git repository

1.  You can see [file:GettingStarted.org](GettingStarted.org) for some
    guidance. Google is also your friend here.

### **Copy** the `.gitignore` from the root of the course source code repository.

1.  Your projects should **always** include the `.gitignore` where you
    exclude kibble

### Try out `gradle init`

1.  Include the `--type java-application` switch

2.  Reset the name and test framework as described in the source code

User Interface
==============

Initially, you will write a driver program, called `Cat.java` in this document.
-------------------------------------------------------------------------------

When run, `Cat` will check the command-line for arguments.
----------------------------------------------------------

### If there are no arguments, terminate with an appropriate error message.

### For each argument, treat it as a file name and open it for input

1.  Use the `FileReader` class for reading the file.

    1.  For future convenience, store the reader in a variable of the
        superclass `Reader` type.

2.  Read each line from the file and echo it to the screen.

Extending `FilterReader`
========================

Read the documentation on the `FilterReader` class in the Java standard library.
--------------------------------------------------------------------------------

### A `FilterReader` is a `Readable` object that, itself, contains a `Readable` object.

1.  **Software Engineering Note** The Facade Pattern.

### The **FilterReader** extending class can modify the input from the wrapped readable object.

### Since the `Cat` program you were wrote uses a `FileReader` to read the input file (which implements the `Readable` interface), it could be "wrapped" in a filtering class that changed what `Cat` read.

1.  Imagine you hated lower-case 'u' characters. You hate them so much
    you want your `Cat` program to replace them with 'x' characters.

    1.  No, you want to be able to get rid of 'u' in ANY file you read.

    2.  So you build a `SansLittleUFilterReader` that extends
        `FilterReader`. In the single character `read` method you look
        at the next character from the wrapped `Reader`.

        1.  If it is not the dreaded 'u', return it.

        2.  If it **is** a 'u', return an 'x' and throw that 'u' away.

### Notice that the `SansLittleUFilterReader` `extends` the `FilterReader` class.

1.  This means it needs a `Reader` passed to its constructor.

2.  That `Reader` is stored in the `FilterReader`'s `in` field.

3.  `in` is `protected`, so subclasses (say `SansLittleUFilterReader`)
    can see it.

`SansCommentFilterReader`
=========================

### **NOTE** Our filter reader will **not** implement `mark`.

### To **be** a `Reader` the `FilterReader` child must implement *both* `read` methods and the `skip` method.

1.  You have a choice on implementing `read()` in terms of
    `read(char[], int, int)` or *vice versa*.

2.  In any case, make sure to implement one in terms of the other so
    that the working code is only written once and all the tests run
    through a single path doing the real work.

    1.  **Software Engineering Note** The *Do not Repeat Yourself* (DRY)
        Principle.

Read the documentation on the `PushbackFilterReader` class, too.
----------------------------------------------------------------

The *point* of a `FilterReader` is to wrap around a `Reader` and modify the input from the wrapped reader before it goes to the caller.
---------------------------------------------------------------------------------------------------------------------------------------

The `SansCommentFilterReader` is more complex.
----------------------------------------------

### End-of-line comments: a semicolon, `;` marks the beginning of a comment that ends at the end of the line.

1.  Replace EOLN comments with one EOLN character.

### Delimited comments: a hash-pipe, `#|`, and a pipe-hash, `|#`, mark the beginning and ending of a comment.

1.  Replace with a single space *if* the comment contains zero EOLN
    characters; otherwise replace it with the number of EOLN characters
    within the comment.

### The example below has the line number prepended to each line in the before (with comment) and after (sans comment) version of the code.

``` {.scheme}
---------- Before ----------
 1: (set a (+ 1 5)) ; Scheme uses prefix notation
 2: (set b (* (+ a 1) (- a 1))) ; b is a^2 - 1
 3: #| Isn't this fun? Seeing Scheme programs.
 4:
 5: And yet we know nothing about Scheme. Except
 6: what comments look like. |#
 7: (set c (+ (* a a)#| the square|#(* b b)#| the OTHER square|#))
 8:
---------- After ----------
 1: (set a (+ 1 5))
 2: (set b (* (+ a 1) (- a 1)))
 3:
 4:
 5:
 6:
 7: (set c (+ (* a a) (* b b) ))
 8:
```

1.  The rules for delimited comments mean that the line numbers of all
    non-comment code remain the same.

    The line numbers of all non-comment bits of the program are
    **unchanged**.

### As mentioned above, you need to implement `read()`, `read(char[], int, int)`, and `skip(long)` so that any arbitrary `Reader` client gets the properly stripped results.

Modify `Cat`
============

When `SansCommentFilterReader` works, modify the construction of the `Reader` in `Cat` so that it wraps a comment stripper around the `FileReader`.
---------------------------------------------------------------------------------------------------------------------------------------------------

That line should be the only change needed.
-------------------------------------------

Test it to make sure it compiles and runs
-----------------------------------------

Deliverables/Submission Method
==============================

Submission is using Git version control and cs-devel.
-----------------------------------------------------

The project is to be a Gradle project that can be built and run within the tool.
--------------------------------------------------------------------------------

The project must include a `README` file explaining the problem, the solution, and how it is run.
-------------------------------------------------------------------------------------------------

### You must document how you **tested** your program. Input, expected output, what the test proves when it passes.

The base directory of the project is to be a `git` repository
-------------------------------------------------------------

### Make sure it has an appropriate Java/Gradle (and your editor) `.gitignore` file.

Log in to `GitTea` at <https://cs-devel.potsdam.edu>
----------------------------------------------------

### Create a new, empty repository on `cs-devel`.

1.  The name **must** begin with `p001`. (note the lowercase of the
    'p'.)

2.  The name of the repository of *each* program you turn in will begin
    with `p###` where "\#\#\#" is the number of the assignment.

### After the `GitTea` will guide you to connect your *local* repository (where your solution lives) to the *remote* repository.

1.  Notice that the instructions differ on when you create the new
    repository on `cs-devel`.
