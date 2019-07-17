ji65
==========

ji65 is a Java-based interpreter for running 6502 assembly code. I share a love of developing
classic console games (the NES in particular) and for robust application testing, and became
frustrated over time with the lack of testability in my 6502 development. So I decided to write
this interpreter as a way to write and run test suites against my 6502 code.

Running It
----------

The app requires Gradle 4.10 and Java 11. Currently to run the app, you'll need to check it out of
Github, import the project via the Gradle file, and then run...? This is currently a work in progress.

Things To Do
------------

 * Add test-framework functionality into this, which has always been my over-arching goal.
 * There's a lot of features present in that similar-but-much-better tool ca65 that could be added here that I haven't personally used. I'll probably add these as I run into them.
 * I kind of want to turn this into a full-fledged NES emulator long-term, though maybe as a separate module. We'll see.
