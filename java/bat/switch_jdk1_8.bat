@echo off
setx JAVA_HOME "D:\ProgramFiles\Java\jdk1.8" /M
setx PATH "%JAVA_HOME%\bin;%PATH%" /M
echo 已切换到 JDK 1.8
java -version
pause