# Message Sender 
### _Lambda expressions and anonymous classes instead of inner, nested, anonymous, local classes_

#### To start using script (_[see shared README](https://github.com/YULIYA2001/LeverXCource/tree/homework1-main/Homework1_Subtasks)_).
### To start the program manually:

**1.** <sup>_In terminal_</sup> Compile program:
```
    javac -cp <created_directory_path>/lib/logger_lib.jar -d <output_directory_path> <created_directory_path>/Subtask3/src/com/subtask3/*.java
```

**2.** <sup>_In terminal_</sup> Run program:

To run on __Windows__:
```
java -cp <output_directory_path>;<created_directory_path>\lib\logger_lib.jar com.subtask3.TestLambdaAndAnonymous
```

To run on __Linux__:
```
java -cp <output_directory_path>:<created_directory_path>/lib/logger_lib.jar com.subtask3.TestLambdaAndAnonymous
``` 

**3.** <sup>_In terminal_</sup> To create executable JAR file (including test) run:
   ```
    # Create manifest (entry point)
    echo Main-Class: com.subtask3.TestLambdaAndAnonymous > <created_directory_path>/Subtask3/manifest.txt
    echo Class-Path: ../lib/logger_lib.jar >> <created_directory_path>/Subtask3/manifest.txt
    
    # Build JAR-file
    jar cfm <created_directory_path>/Subtask3/message_sender.jar <created_directory_path>/Subtask3/manifest.txt -C <output_directory_path> .
    
    # Run JAR-file
    java -jar <created_directory_path>/Subtask3/message_sender.jar
   ```

## After compile: 
**_Separate .class file for anonymous class and interfaces, but nothing for lambda-expressions_**

![compiled_classes.png](compiled_classes.png)
