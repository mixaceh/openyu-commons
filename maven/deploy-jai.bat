@echo off

call mvn deploy:deploy-file -Dfile=../lib-jai/jai_core-1.1.3.jar -DgroupId=org.openyu.javax.media -DartifactId=jai_core -Dversion=1.1.3 -Dpackaging=jar -DgeneratePom=true -DrepositoryId=internal -Durl=http://127.0.0.1:8090/nexus/content/repositories/releases/
call mvn deploy:deploy-file -Dfile=../lib-jai/jai_codec-1.1.3.jar -DgroupId=org.openyu.javax.media -DartifactId=jai_codec -Dversion=1.1.3 -Dpackaging=jar -DgeneratePom=true -DrepositoryId=internal -Durl=http://127.0.0.1:8090/nexus/content/repositories/releases/
call mvn deploy:deploy-file -Dfile=../lib-jai/jai_imageio-1.1.jar -DgroupId=org.openyu.com.sun.media -DartifactId=jai_imageio -Dversion=1.1 -Dpackaging=jar -DgeneratePom=true -DrepositoryId=internal -Durl=http://127.0.0.1:8090/nexus/content/repositories/releases/