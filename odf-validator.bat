@REM
@REM This file is part of veraPDF Installer, a module of the veraPDF project.
@REM Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
@REM All rights reserved.
@REM
@REM veraPDF Installer is free software: you can redistribute it and/or modify
@REM it under the terms of either:
@REM
@REM The GNU General public license GPLv3+.
@REM You should have received a copy of the GNU General Public License
@REM along with veraPDF Installer as the LICENSE.GPL file in the root of the source
@REM tree.  If not, see http://www.gnu.org/licenses/ or
@REM https://www.gnu.org/licenses/gpl-3.0.en.html.
@REM
@REM The Mozilla Public License MPLv2+.
@REM You should have received a copy of the Mozilla Public License along with
@REM veraPDF Installer as the LICENSE.MPL file in the root of the source tree.
@REM If a copy of the MPL was not distributed with this file, you can obtain one at
@REM http://mozilla.org/MPL/2.0/.
@REM

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\..
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0
if %BASEDIR:~-1%==\ set BASEDIR=%BASEDIR:~0,-1%

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\bin

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\*

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

"%JAVACMD%" -Dfile.encoding=UTF8 -XX:+IgnoreUnrecognizedVMOptions -Dapp.name="ODF Validator" -jar target/odf-validator-0.1.0-SNAPSHOT-jar-with-dependencies.jar %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
