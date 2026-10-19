@echo off
setlocal

REM Try to use globally installed Maven first
where mvn >nul 2>&1
if %ERRORLEVEL% equ 0 (
    mvn %*
    exit /b %ERRORLEVEL%
)

REM Fall back to Maven wrapper distribution
set "MAVEN_VERSION=3.9.16"

if not exist "%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%" (
    echo Maven %MAVEN_VERSION% not found in wrapper distribution.
    echo Please install Maven globally from https://maven.apache.org/download.cgi
    exit /b 1
)

for /d %%D in ("%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%\*") do (
    if exist "%%D\bin\mvn.cmd" (
        call "%%D\bin\mvn.cmd" %*
        exit /b %ERRORLEVEL%
    )
)

echo Maven %MAVEN_VERSION% not found. Please install Maven.
exit /b 1