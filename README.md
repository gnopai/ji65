ji65
==========

ji65 is a Java-based interpreter for running 6502 assembly code. I share a love of developing
classic console games (the NES in particular) and for robust application testing, and became
frustrated over time with the lack of testability in my 6502 development. So I decided to write
this interpreter as a way to write and run test suites against my 6502 code.

Running It
----------

The app currently runs on Java 12 and uses gradle-wrapper for building and whatnot. It might work on 10 or 11, but no guarantees.
It builds a single jar that can be imported into any 6502 project and easily run from something like a Makefile.

To build:

`$ ./gradlew clean jar`    

To run:

```
$ java -jar build/libs/ji65-*.jar -h
Usage: ji65 [-hpV] [-C=CONFIG_FILE] FILE
Assemble and test 6502 code
      FILE          the file to assemble and run tests for
  -C=CONFIG_FILE    the program config file to use
  -h, --help        Show this help message and exit.
  -p                display passing tests and assertions
  -V, --version     Print version information and exit.
```

Things To Do
------------

 * There's a lot of features present in that similar-but-much-better tool ca65 that could be added here that I haven't personally used. I'll probably add these as I run into them.
 * I kind of want to turn this into a full-fledged NES emulator long-term, though maybe as a separate module. We'll see.
