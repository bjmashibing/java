@echo off 
@ECHO 启动 Oracle 11g 服务
net start "OracleOraDb11g_home1TNSListener"
net start "OracleServiceORCL"
@ECHO 启动完毕 按任意键继续
pause
exit