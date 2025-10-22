@echo off

set PATH_TO_SRC=%1
if "%PATH_TO_SRC%"=="" set PATH_TO_SRC=.
set OUTPUT_DIR=%2
if "%OUTPUT_DIR%"=="" set OUTPUT_DIR=out

rem compile and run
echo Compile: %PATH_TO_SRC%\src\com\...
javac -cp %PATH_TO_SRC%\lib\logger_lib.jar -d %PATH_TO_SRC%\%OUTPUT_DIR% %PATH_TO_SRC%\src\com\subtask1\TestExternalLibrary.java

echo Run: com.subtask1.TestExternalLibrary
java -cp %PATH_TO_SRC%\%OUTPUT_DIR%;%PATH_TO_SRC%\lib\logger_lib.jar com.subtask1.TestExternalLibrary

rem create executable jar
echo Create: "manifest.txt" file for executable jar
echo Main-Class: com.subtask1.TestExternalLibrary > %PATH_TO_SRC%\manifest.txt
echo Class-Path: lib\logger_lib.jar >> %PATH_TO_SRC%\manifest.txt

echo Create: executable "test_external_logger.jar" file
jar cfm %PATH_TO_SRC%\test_external_logger.jar %PATH_TO_SRC%\manifest.txt -C %PATH_TO_SRC%\%OUTPUT_DIR% .

echo Delete: "manifest.txt" file
del %PATH_TO_SRC%\manifest.txt

echo Run: executable "test_external_logger.jar" file
java -jar %PATH_TO_SRC%\test_external_logger.jar
