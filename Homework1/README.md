# Simple Logger
#### This is a simple logger program. It consists of two packages: the logger itself (ConsoleLogger) and a simple test.

### To start the program:
**1.** Install JDK and set up the Environment Variables. _How to do this [see here](https://www.geeksforgeeks.org/installation-guide/download-and-install-jdk-on-windows-mac-and-linux/)_.

**2.** Create a project directory and upload _src_-directory and _magic.sh_ (for Linux) or _magic.bat_ (for Windows) to it.

**3.** <sup>_In terminal_</sup> Start script which:
   + compile and run program; 
   + create and run executable jar-file; 
   + create a jar-library.
   
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
   magic.sh [. output_folder_name]
   ```
   
   #### _Or do it manually by the following steps 4-6._

**4.** <sup>_In terminal_</sup> Compile the library including test:
   ```
   javac -d <output_directory_path> <created_directory_path>/src/com/simple/logger/*.java <created_directory_path>/src/com/test/*.java
   ```

**5.** <sup>_In terminal_</sup> Run program test: 
   ```
   java -cp <output_directory_path> com.test.Test
   ```
   <sup>*___-cp__ - classpath to compiled .class files_</sup>

**6.** <sup>_In terminal_</sup> To create executable JAR file (including test) run:
   ```
   # Create manifest (entry point)
   echo Main-Class: com.test.Test > <created_directory_path>/manifest.txt
   
   # Build JAR-file
   jar cfm <created_directory_path>/logger_exec.jar <created_directory_path>/manifest.txt -C <output_directory_path> .
   
   # Run JAR-file
   java -jar <created_directory_path>/logger_exec.jar
   ```
<sup>*_**cfm** - (**c**)create new jar (**f**)file with name (**m**)use manifest_</sup>   
<sup> *_**-C** - put all from <output_directory_path> to jar_</sup>

**7.** <sup>_In terminal_</sup> To create JAR library file (without test) run:
   ```
   jar cf <created_directory_path>/logger_lib.jar -C <output_directory_path> com/simple/logger
   ```
