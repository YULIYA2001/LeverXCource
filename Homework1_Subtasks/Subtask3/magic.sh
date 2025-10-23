#!/bin/bash

PATH_TO_SRC=${1:-.}
OUTPUT_DIR=${2:-out}

# compile and run
echo Compile: $PATH_TO_SRC/src/com/...
javac -cp $PATH_TO_SRC/../lib/logger_lib.jar -d $PATH_TO_SRC/$OUTPUT_DIR $PATH_TO_SRC/src/com/subtask3/*.java

echo -e "Run: com.subtask3.TestLambdaAndAnonymous\n"
java -cp "$PATH_TO_SRC/$OUTPUT_DIR:$PATH_TO_SRC/../lib/logger_lib.jar" com.subtask3.TestLambdaAndAnonymous

# create executable jar
echo -e "\nCreate: "manifest.txt" file for executable jar"
echo Main-Class: com.subtask3.TestLambdaAndAnonymous > $PATH_TO_SRC/manifest.txt
echo Class-Path: ../lib/logger_lib.jar >> $PATH_TO_SRC/manifest.txt

echo Create: executable "message_sender.jar" file
jar cfm $PATH_TO_SRC/message_sender.jar $PATH_TO_SRC/manifest.txt -C $PATH_TO_SRC/$OUTPUT_DIR .

echo Delete: "manifest.txt" file
rm $PATH_TO_SRC/manifest.txt

echo -e "Run: executable "message_sender.jar" file\n"
java -jar $PATH_TO_SRC/message_sender.jar
