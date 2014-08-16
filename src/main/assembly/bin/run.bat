SETLOCAL enabledelayedexpansion

FOR %%F IN (lib/*.jar) DO (
  SET LIBS=!LIBS!;lib/%%F%
)

start java -cp bin;%LIBS% -verbose:gc -Djava.library.path=lib -Xms64m -Xmx64m -XX:+HeapDumpOnOutOfMemoryError org.pagrus.sound.ConsoleMain

wmic process where name="java.exe" CALL setpriority 256
