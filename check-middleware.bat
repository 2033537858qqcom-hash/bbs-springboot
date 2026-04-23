@echo off
chcp 65001 >nul
echo ========================================
echo   BBS 项目中间件状态检查
echo ========================================
echo.

echo [1/4] 检查 Nacos...
curl -s -o nul -w "HTTP状态码: %%{http_code}" http://127.0.0.1:8848/nacos
if %errorlevel% equ 0 (
    echo ✓
    echo   访问地址: http://127.0.0.1:8848/nacos
    echo   用户名: nacos
    echo   密码: nacos
) else (
    echo ✗ Nacos 未启动
    echo   启动命令: cd \path\to\nacos\bin ^&^& startup.cmd -m standalone
)

echo.
echo [2/4] 检查 MySQL...
mysql -h 127.0.0.1 -P 3306 -u root -e "SELECT 'MySQL is running' AS status;" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MySQL 正在运行 (127.0.0.1:3306)
    mysql -h 127.0.0.1 -P 3306 -u root -e "SHOW DATABASES;" 2>nul | findstr "open_bbs"
    if %errorlevel% equ 0 (
        echo   ✓ 数据库 open_bbs 存在
    ) else (
        echo   ⚠ 数据库 open_bbs 不存在
        echo   创建命令: mysql -h 127.0.0.1 -P 3306 -u root -p -e "CREATE DATABASE open_bbs CHARACTER SET utf8mb4;"
    )
) else (
    echo ✗ MySQL 未启动或连接失败
    echo   请检查 MySQL 服务是否启动
)

echo.
echo [3/4] 检查 Redis...
redis-cli -h 127.0.0.1 -p 6379 ping >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Redis 正在运行 (127.0.0.1:6379)
    for /f "delims=" %%i in ('redis-cli -h 127.0.0.1 -p 6379 ping') do set REDIS_RESULT=%%i
    if "%REDIS_RESULT%"=="PONG" (
        echo   ✓ Redis 响应正常
    )
) else (
    echo ✗ Redis 未启动或连接失败
    echo   请检查 Redis 服务是否启动
)

echo.
echo [4/4] 检查 MongoDB...
mongosh --quiet --eval "db.runCommand({ping:1}).ok" mongodb://127.0.0.1:27017/open_bbs >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MongoDB 正在运行 (127.0.0.1:27017)
    echo   数据库: open_bbs
) else (
    echo ✗ MongoDB 未启动或连接失败
    echo   请检查 MongoDB 服务是否启动
)

echo.
echo ========================================
echo   服务端口占用情况
echo ========================================
echo.
echo BBS 服务端口：
netstat -ano | findstr ":7011 :7012 :7013" | findstr "LISTENING"
if %errorlevel% neq 0 (
    echo   暂无服务运行
)

echo.
echo 中间件端口：
netstat -ano | findstr ":8848 :3306 :6379 :27017" | findstr "LISTENING"

echo.
echo ========================================
echo   快速命令
echo ========================================
echo.
echo 启动所有服务: start-all-services.bat
echo 停止所有服务: stop-all-services.bat
echo 查看 Nacos:   http://127.0.0.1:8848/nacos
echo.
pause
