@echo off

REM Batch file for executing the RunWeka launcher class.
REM   RunWeka.bat <command>
REM Run with option "-h" to see available commands
REM 
REM Notes: 
REM - If you're getting an OutOfMemory Exception, increase the value of
REM   "maxheap" in the RunWeka.ini file.
REM - If you need more jars available in Weka, either include them in your
REM   %CLASSPATH% environment variable or add them to the "cp" placeholder
REM   in the RunWeka.ini file.
REM
REM Author:  FracPete (fracpete at waikato dot ac dot nz)
REM Version: $Revision: 1.6 $


set actual_dir=%cd%
echo %actual_dir%

IF NOT EXIST %actual_dir%/GenericObjectEditor.props java -cp "weka.jar;genlib.jar" weka.gui.GenericPropertiesCreator %actual_dir%\GenericPropertiesCreator.props %actual_dir%/GenericObjectEditor.props

java -cp "weka.jar;GenLib.jar" weka.gui.GUIChooser

