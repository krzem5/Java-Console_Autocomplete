@echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac -encoding utf8 -cp com/krzem/console_autocomplete/modules/jna-5.5.0.jar;com/krzem/console_autocomplete/modules/jna-platform-5.5.0.jar; com/krzem/console_autocomplete/Main.java&&java -Dfile.encoding=UTF8 -cp com/krzem/console_autocomplete/modules/jna-5.5.0.jar;com/krzem/console_autocomplete/modules/jna-platform-5.5.0.jar; com/krzem/console_autocomplete/Main
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"