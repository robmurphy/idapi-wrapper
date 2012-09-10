@echo off
set ANT_HOME=C:/Java/apache-ant-1.8.2
set JAVA_HOME=C:/Progra~2/Common~1/Actuate/11.0/JDK160
set PATH=%ANT_HOME%/bin;%PATH%
set IDAPI_LIB_DIR=C:/Progra~2/Actuate11SP3/iServer/servletcontainer/iportal/WEB-INF/lib

rem  You will need to build the BodWsApiV2.jar by running the script available in the Ant
rem  build found in the bod directory of this archive.  Before running the RunAntBuildBodWsApiClient.cmd
rem  command script, you will need to edit the WsdlLocation property of the buildBodWsApiClient.xml
rem  build file to point at your BIRT onDemand account.
set BOD_WSAPI_LIB=C:/Workspace/BodWsAPI/BodWsApiV2.jar


rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml Authentication
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml downloadFile
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml downloadUNC
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml copyFile
call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml uploadFile
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml volumeDownload
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml volumeMigrate
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml VolumeUpload

rem Test Upload to BIRT onDemand
rem call ant -lib idapi-wrapper.jar -lib %IDAPI_LIB_DIR% -lib %BOD_WSAPI_LIB% -buildfile build.xml BodUploadFile

pause