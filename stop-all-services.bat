@echo off
chcp 65001 >nul
echo ========================================
echo   BBS 项目停止服务脚本
echo ========================================
echo.

echo 正在停止服务...
echo.

echo [1/4] 停止 bbs-local-auth...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":7014" ^| findstr "LISTENING"') do (
    echo   发现进程 PID: %%a
    taskkill /F /PID %%a >nul 2>&1
    echo   ✓ bbs-local-auth 已停止
    goto :next0
)
echo   ⚠ bbs-local-auth 未运行
:next0

echo.
echo [2/4] 停止 bbs-rest...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":7012" ^| findstr "LISTENING"') do (
    echo   发现进程 PID: %%a
    taskkill /F /PID %%a >nul 2>&1
    echo   ✓ bbs-rest 已停止
    goto :next1
)
echo   ⚠ bbs-rest 未运行
:next1

echo.
echo [3/4] 停止 bbs-article-service...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":7011" ^| findstr "LISTENING"') do (
    echo   发现进程 PID: %%a
    taskkill /F /PID %%a >nul 2>&1
    echo   ✓ bbs-article-service 已停止
    goto :next2
)
echo   ⚠ bbs-article-service 未运行
:next2

echo.
echo [4/4] 停止 bbs-user-service...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":7013" ^| findstr "LISTENING"') do (
    echo   发现进程 PID: %%a
    taskkill /F /PID %%a >nul 2>&1
    echo   ✓ bbs-user-service 已停止
    goto :next3
)
echo   ⚠ bbs-user-service 未运行
:next3

echo.
echo ========================================
echo   所有服务已停止
echo ========================================
echo.
pause
