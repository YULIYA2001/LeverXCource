# External library usage
#### To start using script (_[see shared README](https://github.com/YULIYA2001/LeverXCource/tree/homework1-main/Homework1_Subtasks)_).

### To start the program manually:

**1.** <sup>_In terminal_</sup> Compile program:
```
    javac -cp <created_directory_path>/lib/logger_lib.jar -d <output_directory_path> <created_directory_path>/Subtask1/src/com/subtask1/TestExternalLibrary.java
```

**2.** <sup>_In terminal_</sup> Run program:

To run on __Windows__:
```
java -cp <output_directory_path>;<created_directory_path>\lib\logger_lib.jar com.subtask1.TestExternalLibrary
```

To run on __Linux__:
```
java -cp <output_directory_path>:<created_directory_path>/lib/logger_lib.jar com.subtask1.TestExternalLibrary
``` 

**3.** <sup>_In terminal_</sup> To create executable JAR file (including test) run:
   ```
    # Create manifest (entry point)
    echo Main-Class: com.subtask1.TestExternalLibrary > <created_directory_path>/Subtask1/manifest.txt
    echo Class-Path: ../lib/logger_lib.jar >> <created_directory_path>/Subtask1/manifest.txt
    
    # Build JAR-file
    jar cfm <created_directory_path>/Subtask1/test_external_logger.jar <created_directory_path>/Subtask1/manifest.txt -C <output_directory_path> .
    
    # Run JAR-file
    java -jar <created_directory_path>/Subtask1/test_external_logger.jar
   ```
