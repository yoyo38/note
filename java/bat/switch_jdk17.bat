@echo off
setx JAVA_HOME "C:\ProgramFiles\Java\jdk-17" /M
setx PATH "%JAVA_HOME%\bin;%PATH%" /M
echo 已切换到 JDK 17
java -version
pause