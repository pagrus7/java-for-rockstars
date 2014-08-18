SETLOCAL enabledelayedexpansion

FOR %%F IN (lib/*.jar) DO (
  SET LIBS=!LIBS!;lib/%%F%
)

start java -cp  bin;%LIBS% -verbose:gc -Djava.library.path=lib -Xms1G -Xmx1G -XX:+HeapDumpOnOutOfMemoryError org.pagrus.sound.GuiMain

wmic process where name="java.exe" CALL setpriority 256
