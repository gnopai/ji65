ji65
==========

ji65 is a Java-based interpreter for running 6502 assembly code. I share a love of developing
classic console games (the NES in particular) and for robust application testing, and became
frustrated over time with the lack of testability in my 6502 development. So I decided to write
this interpreter as a way to write and run test suites against my 6502 code.

Running It
----------

The app currently runs on Java 12 and uses gradle-wrapper for building and whatnot. It might work on 10 or 11, but no guarantees.
Currently to run the it, you'll need to check it out of Github, import the project via the Gradle file, and then run...?
This is still kind of a work in progress, but nearly to the point of actually being useful.

Things To Do
------------

 * Add test-framework functionality into this, which has always been my over-arching goal.
 * There's a lot of features present in that similar-but-much-better tool ca65 that could be added here that I haven't personally used. I'll probably add these as I run into them.
 * I kind of want to turn this into a full-fledged NES emulator long-term, though maybe as a separate module. We'll see.
