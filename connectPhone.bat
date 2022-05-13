@echo off
chcp 65001
start bin/adb.exe kill-server
echo "按回车继续,请记住出现在Connected to后面的ip地址"
pause
start bin/scrcpy.exe --tcpip
exit