# External library usage
#### This program uses previous Simple Logger (_[see Simple Logger](https://github.com/YULIYA2001/LeverXCource/tree/homework1-main/Homework1)_).

### To start the program:
**1.** Install JDK and set up the Environment Variables. _How to do this [see here](https://www.geeksforgeeks.org/installation-guide/download-and-install-jdk-on-windows-mac-and-linux/)_.

**2.** Create a project directory and upload _src-_, _lib-_ directories and _magic.sh_ (for Linux) or _magic.bat_ (for Windows) to it.

**3.** <sup>_In terminal_</sup> Start script which:
+ compile and run program with external library;
+ create and run executable jar-file.

To run on __Windows__ from _anywhere_:
   ```
   <created_directory_path>\magic.bat <created_directory_path> [output_folder_name]
   ```
To run on __Windows__ directly from _created directory_:
   ```
   magic.bat [. output_folder_name]
   ```
To run on __Linux__ from _anywhere_:
   ```
   <created_directory_path>/magic.sh <created_directory_path> [output_folder_name]
   ```
To run on __Linux__ directly from _created directory_:
   ```
   ./magic.sh [. output_folder_name]
   ```

#### _Or do it manually by the following steps 4-6._

**4.** <sup>_In terminal_</sup> Compile program:
```
    javac -cp <created_directory_path>/lib/logger_lib.jar -d <output_directory_path> <created_directory_path>/src/com/subtask1/TestExternalLibrary.java
```

**5.** <sup>_In terminal_</sup> Run program:

To run on __Windows__:
```
java -cp <output_directory_path>;<created_directory_path>\lib\logger_lib.jar com.subtask1.TestExternalLibrary
```

To run on __Linux__:
```
java -cp <output_directory_path>:<created_directory_path>/lib/logger_lib.jar com.subtask1.TestExternalLibrary
``` 

**6.** <sup>_In terminal_</sup> To create executable JAR file (including test) run:
   ```
    # Create manifest (entry point)
    echo Main-Class: com.subtask1.TestExternalLibrary > <created_directory_path>/manifest.txt
    echo Class-Path: lib/logger_lib.jar >> <created_directory_path>/manifest.txt
    
    # Build JAR-file
    jar cfm <created_directory_path>/test_external_logger.jar <created_directory_path>/manifest.txt -C <output_directory_path> .
    
    # Run JAR-file
    java -jar <created_directory_path>/test_external_logger.jar
   ```
