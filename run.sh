#!/bin/sh
echo "Running script"

sudo mvn clean compile assembly:single

t=$(date +%Y%m%d%H%M%S)
logName="ex$t.log"
echo $logName

nohup java -Xmx10g -jar /home/uzicohen/Desktop/workspace/RIMPPDInference/target/RIMPPDInference-0.0.1-SNAPSHOT-jar-with-dependencies.jar > /home/uzicohen/Desktop/workspace/RIMPPDInference/src/main/java/resources/experiments/logs/$logName &


while true;
do
 tail -10 /home/uzicohen/Desktop/workspace/RIMPPDInference/src/main/java/resources/experiments/logs/$logName
 sleep  100 ;
done


