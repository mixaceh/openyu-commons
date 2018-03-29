@echo off

call mvn deploy:deploy-file -Dfile=../lib-jmagic/jmagick-6.3.9-Q8.jar -DgroupId=jmagick -DartifactId=jmagick -Dversion=6.3.9-Q8 -Dpackaging=jar -DrepositoryId=internal -Durl=http://127.0.0.1:8090/nexus/content/repositories/releases/
