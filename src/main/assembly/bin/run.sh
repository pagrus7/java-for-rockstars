#!/bin/sh

CP=$LIBS:$(echo bin lib/*.jar | sed 's/ /:/g')

java -cp $CP -Djava.library.path=lib -Xms256m -Xmx256m org.pagrus.sound.ConsoleMain

