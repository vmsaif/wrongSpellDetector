## Spell Checker
This Java project implements a spell checker that prompts the user to add a dictionary file in .txt format, and then prompts for a file to be checked for spelling mistakes in .txt format. It tells in which line the error occurred, and if multiple errors are found, it writes the line numbers.

## Features
- Uses JFileChooser to allow the user to select the dictionary and file to be checked.
- Stores the dictionary words in an ArrayList for efficient searching.
- Implements logic to cross-check the spelling of the words in the file against the dictionary.

## How to Run
### Compile.
From the root directory of the repository, from terminal

    javac src\*.java

### Run the SpellChecker class.
    java -cp src\ CHECKER

In the project directory, there is a sample dictionary file `words.txt` and a sample document with spelling mistakes `GeorgeEliot_SilasMarner.txt` 
Follow the prompts in the user interface to select (in order)
1. dictionary file in .txt format
2. the file to be checked in .txt format
   
## What I Have Learned
- How to use JFileChooser to allow the user to select files.
- How to store data in an ArrayList and perform searches efficiently.
- How to implement logic to cross-check the spelling of words against a dictionary.

## Dependencies
Java 8 or later