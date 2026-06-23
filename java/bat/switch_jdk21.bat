@echo off
setx JAVA_HOME "C:\ProgramFiles\Java\jdk-21" /M
setx PATH "%JAVA_HOME%\bin;%PATH%" /M
echo 已切换到 JDK 21
java -version
pause