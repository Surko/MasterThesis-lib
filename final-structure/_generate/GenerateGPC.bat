@echo off

REM Batch file to pull the GenericPropertiesCreator from weka.jar.
REM No additional parameters needed.
REM   GenerateGPC.bat
REM Run with option "-h" to see available commands
REM 
REM Author:  LukasSurin 

set actual_dir=%cd%/..
echo %actual_dir% 

jar xf %actual_dir%/weka.jar weka/gui/GenericPropertiesCreator.props

move "weka\gui\GenericPropertiesCreator.props" %actual_dir%

rmdir /S /Q weka

REM java -cp "%actual_dir%/weka.jar;%actual_dir%/genlib.jar" weka.gui.GenericPropertiesCreator %actual_dir%\GenericPropertiesCreator.props %actual_dir%/GenericObjectEditor.props
