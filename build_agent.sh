#!/bin/zsh

mvn package
echo "package Java Agent succeed! 🎉"

cd UserApp/HwlloRowld

./pkg.sh
echo "complile user app HelloWorld succeed! 🎉"

echo "running user app HelloWorld with agent JavaAg3nt-1.0-SNAPSHOT.jar..."
java -javaagent:../../target/JavaAg3nt-1.0-SNAPSHOT.jar -cp target/HwlloRowld-1.0-SNAPSHOT.jar com.diy.HelloWorld.HelloWorld

echo "===== task succeed! 🎉 ====="
cd ../../