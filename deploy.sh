#!/usr/bin/bash

if [ "$#" -ne 1 ]; then
    echo "version string missing"
    exit 1
fi

rm -rf ./deployment

echo "creating deployment dir"
mkdir -p ./deployment/scripts/

echo ""
echo "building java project"
mvn -q package

if [ $? -ne 0 ]; then
    echo "building the java project failed"
    exit 1
fi

echo ""
echo "copying files to deployment dir"
cp ./target/AssistantMirror-1.0-SNAPSHOT-jar-with-dependencies.jar ./deployment/AssistantMirror.jar
cp ./server/main.py ./deployment/
cp ./server/server.sh ./deployment/
cp ./server/update.py ./deployment/
cp -r ./scripts/* ./deployment/scripts/

echo ""
echo "writing release file"
echo "$1" > ./deployment/release

echo ""
echo "zipping release"
(cd deployment; zip -rq AssistantMirror.zip *)
mv deployment/AssistantMirror.zip .
rm -r ./deployment/*
mv ./AssistantMirror.zip ./deployment/

if [ $? -ne 0 ]; then
    echo "zipping failed"
    exit 1
fi

echo ""
echo "packaging done"
