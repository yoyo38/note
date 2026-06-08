#!/bin/bash
DATE=$(date +%Y%m%d%H%M%S)
DIR=/usr/local/jenkins/hebs/hebs-iot
JARFILE=hebs-iot.jar
if [ ! -d $DIR/backup ];then
   mkdir -p $DIR/backup
fi
cd $DIR

cp -f $JARFILE backup/$JARFILE$DATE
cd backup/
ls -lt|awk 'NR>5{print $NF}'|xargs rm -rf
cd $DIR
echo "=== stoping  hebs-iot... ==="
docker stop  hebs-iot
echo "=== deleting  hebs-iot container ==="
docker rm  hebs-iot
echo "=== deleting  hebs-iot image ==="
docker rmi  hebs-iot
echo "=== building  hebs-iot image ==="
docker build -t  hebs-iot .
echo "=== run hebs-iot ==="
docker run --net=host --name hebs-iot --restart always -itd hebs-iot:latest -p 8088:8088 -p 2404:2404
