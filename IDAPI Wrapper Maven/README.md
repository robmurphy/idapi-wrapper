Package the wrapper, install the IDAPI depedency, install the IDAPI Wrapper
mvn package
mvn install:install-file -Dfile=target\com.actuate.idapi.jar -DgroupId=com.actuate -DartifactId=idapi -Dversion=3.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=target\idapi-wrapper-3.0.1.jar -DgroupId=com.actuate.aces -DartifactId=idapi-wrapper -Dversion=3.0.1 -Dpackaging=jar