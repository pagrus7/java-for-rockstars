#!/bin/sh

CP=$LIBS:$(echo bin lib/*.jar | sed 's/ /:/g')

java -cp $CP -verbose:gc -Djava.library.path=lib -Xms256m -Xmx256m org.pagrus.sound.GuiMain

