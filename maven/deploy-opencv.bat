@echo off

rem call mvn deploy:deploy-file -Dfile=../lib-opencv/opencv-248.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=2.4.8 -Dpackaging=jar -DrepositoryId=internal -Durl=http://127.0.0.1:8090/nexus/content/repositories/releases/
call mvn deploy:deploy-file -Dfile=../lib-opencv/opencv-2411.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=2.4.11 -Dpackaging=jar -DrepositoryId=internal -Durl=http://127.0.0.1:8090/nexus/content/repositories/releases/
