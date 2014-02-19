SET LIBDIR=.\lib\
SET JAVA_HOME=

SET JAVA_EXE="%JAVA_HOME%java"

SET CP="%LIBDIR%activation.jar"
SET CP=%CP%;"%LIBDIR%ant.jar"
SET CP=%CP%;"%LIBDIR%axis.jar"
SET CP=%CP%;"%LIBDIR%BodWsApiV2.jar"
SET CP=%CP%;"%LIBDIR%com.actuate.idapi.jar"
SET CP=%CP%;"%LIBDIR%com.actuate.idapi_internal.jar"
SET CP=%CP%;"%LIBDIR%commons-discovery-0.2.jar"
SET CP=%CP%;"%LIBDIR%commons-logging.jar"
SET CP=%CP%;"%LIBDIR%idapi-wrapper.jar"
SET CP=%CP%;"%LIBDIR%idapi-wrapper-samples.jar"
SET CP=%CP%;"%LIBDIR%jaxrpc.jar"
SET CP=%CP%;"%LIBDIR%log4j-1.2.8.jar"
SET CP=%CP%;"%LIBDIR%mail.jar"
SET CP=%CP%;"%LIBDIR%saaj.jar"
SET CP=%CP%;"%LIBDIR%wsdl4j-1.5.1.jar"
SET CP=%CP%;"%LIBDIR%xercesImpl.jar"

%JAVA_EXE% -classpath %CP% com.actuate.training.ContentLoader ContentLoader.properties