@echo off
set ANT_HOME=C:/Java/ant
set JAVA_HOME=C:/Java/jdk1.7
set PATH=%ANT_HOME%/bin;%PATH%
set IDAPI_LIB_DIR=C:/Actuate/idapiLibs-iHub3

call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml Authentication
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml downloadFile
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml downloadUNC
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml copyFile
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml uploadFile
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml volumeDownload
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml volumeMigrate
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib -buildfile build.xml VolumeUpload

pause