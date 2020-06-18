#!/bin/sh
while ! nc -z test.c8wvkz3hrsg0.us-east-1.rds.amazonaws.com 3306 ; do
echo "Waiting for MySQL server to be available"
sleep 2
done
exec java -jar -Dspring.datasource.url=jdbc:mysql://test.c8wvkz3hrsg0.us-east-1.rds.amazonaws.com/test Practica1-CI-1.0.0.jar