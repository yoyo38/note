@echo off
setx JAVA_HOME "D:\ProgramFiles\Java\jdk-11" /M
setx PATH "%JAVA_HOME%\bin;%PATH%" /M
echo 已切换到 JDK 11
java -version
pause