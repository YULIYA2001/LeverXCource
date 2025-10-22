#!/bin/bash

PATH_TO_SRC=${1:-./}
OUTPUT_DIR=${2:-out}

# compile and run
echo Compile: $PATH_TO_SRC/src/com/.
javac -d $PATH_TO_SRC/$OUTPUT_DIR $PATH_TO_SRC/src/com/simple/logger/*.java $PATH_TO_SRC/src/com/test/*.java

echo Run: com.test.Test
java -cp $PATH_TO_SRC/$OUTPUT_DIR com.test.Test

#create executable jar
echo Create: "manifest.txt" file for executable jar
echo Main-Class: com.test.Test > $PATH_TO_SRC/manifest.txt

echo Create: executable "logger_exec.jar" file
jar cfm $PATH_TO_SRC/logger_exec.jar $PATH_TO_SRC/manifest.txt -C $PATH_TO_SRC/$OUTPUT_DIR .

echo Delete: "manifest.txt" file
rm $PATH_TO_SRC/manifest.txt

echo Run: executable "logger_exec.jar" file
java -jar $PATH_TO_SRC/logger_exec.jar

# create lib jar
echo Create: library "logger_lib.jar" file
jar cf $PATH_TO_SRC/logger_lib.jar -C $PATH_TO_SRC/$OUTPUT_DIR com/simple/logger
