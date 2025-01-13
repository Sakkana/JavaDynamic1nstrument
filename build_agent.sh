#!/bin/zsh

mvn package
echo "package Java Agent succeed! 🎉"

cd UserApp/HwlloRowld

./pkg.sh
echo "compile user app HelloWorld succeed! 🎉"

echo "running user app HelloWorld with agent JavaAg3nt-1.0-SNAPSHOT.jar..."
java -javaagent:../../target/JavaAg3nt-1.0-SNAPSHOT.jar -cp target/HwlloRowld-1.0-SNAPSHOT.jar com.diy.HelloWorld.HelloWorld

if [ $? -eq 0 ];  # 中括号前后这里必须要空格
then
  echo "===== task succeed! 🎉 ====="
else
  echo "===== task error. 🤮 ====="
fi

cd ../../