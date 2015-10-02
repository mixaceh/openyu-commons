@echo off
set _CLASSPATH=.

rem for %%i in (*.jar) do call classpath.bat %%i
rem for %%i in (..\*.jar) do call classpath.bat %%i
rem for %%i in (..\lib\*.jar) do call classpath.bat %%i
rem #bug The input line is too long.

set _CLASSPATH=.;..\*;..\lib\*;..\..\*;..\..\lib\*;..\..\..\*;..\..\..\lib\*;..\..\..\lib-provided\*

rem @echo Using classpath:%_CLASSPATH%

java -Xmn96m -Xms192m -Xmx192m -Xss256k -XX:PermSize=128m -XX:MaxPermSize=128m -cp %_CLASSPATH% -XX:+UseParNewGC -Djava.net.preferIPv4Stack=true -verbose:gc org.openyu.commons.core.App %1 %2 %3 %4 %5 %6 %7 %8 %9

set _CLASSPATH=.