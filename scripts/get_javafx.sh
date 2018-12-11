#!/bin/bash

URL=http://gluonhq.com/download/javafx-embedded-sdk
ZIP=javafx-embedded-sdk
DIR=armv6hf-sdk

if [ $(whoami) != "root" ]; then
	echo "error: please run as root"
	exit 0
fi

wget $URL
unzip $ZIP

(cd ${DIR}/rt/lib/    && cp javafx.platform.properties javafx.properties jfxswt.jar /usr/lib/jvm/java-8-openjdk/jre/lib/)
(cd ${DIR}/rt/lib/arm && cp * /usr/lib/jvm/java-8-openjdk/jre/lib/arm/)
(cd ${DIR}/rt/lib/ext && cp jfxrt.jar /usr/lib/jvm/java-8-openjdk/jre/lib/ext/)
