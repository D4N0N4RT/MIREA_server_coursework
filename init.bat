@echo off

echo Building jar
call mvn clean package -DskipTests
echo ...

echo Copy jar to deploy folder
copy target/server_coursework-0.1.jar deploy/app
echo ...

pause