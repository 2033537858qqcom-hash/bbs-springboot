@echo off
chcp 65001 >nul
echo ========================================
echo   BBS 项目本地启动脚本
echo ========================================
echo.

echo [1/4] 检查中间件状态...
echo.

echo 检查 Nacos...
curl -s http://127.0.0.1:8848/nacos >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Nacos 正在运行
) else (
    echo ✗ Nacos 未启动，请先启动 Nacos
    echo   启动命令: startup.cmd -m standalone
    pause
    exit /b 1
)

echo 检查 MySQL...
mysql -h 127.0.0.1 -P 3306 -u root -e "SELECT 1;" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MySQL 正在运行
) else (
    echo ⚠ MySQL 连接失败，请检查是否启动
)

echo 检查 Redis...
redis-cli -h 127.0.0.1 -p 6379 ping >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Redis 正在运行
) else (
    echo ⚠ Redis 连接失败，请检查是否启动
)

echo 检查 MongoDB...
mongosh --eval "db.runCommand({ping:1})" mongodb://127.0.0.1:27017/open_bbs >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MongoDB 正在运行
) else (
    echo ⚠ MongoDB 连接失败，请检查是否启动
)

echo.
echo [2/4] 编译项目...
echo.

cd /d "%~dp0"
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo ✗ 编译失败
    pause
    exit /b 1
)

echo ✓ 编译成功
echo.

echo [3/4] 准备启动服务...
echo.
echo 将启动以下服务：
echo   - bbs-local-auth       (端口 7014) [本地认证服务]
echo   - bbs-user-service     (端口 7013)
echo   - bbs-article-service  (端口 7011)
echo   - bbs-rest             (端口 7012)
echo.
echo 提示：每个服务将在新的命令行窗口中启动
echo.
pause

echo [4/4] 启动服务...
echo.

echo 启动 bbs-local-auth...
start "BBS Local Auth" cmd /k "cd bbs-local-auth && mvn spring-boot:run"
timeout /t 3 >nul

echo 启动 bbs-user-service...
start "BBS User Service" cmd /k "cd bbs-user\bbs-user-service && mvn spring-boot:run"
timeout /t 3 >nul

echo 启动 bbs-article-service...
start "BBS Article Service" cmd /k "cd bbs-article\bbs-article-service && mvn spring-boot:run"
timeout /t 3 >nul

echo 启动 bbs-rest...
start "BBS Rest API" cmd /k "cd bbs-rest && mvn spring-boot:run"
timeout /t 3 >nul

echo.
echo ========================================
echo   服务启动中，请稍候...
echo ========================================
echo.
echo 访问地址：
echo   - Nacos 控制台: http://127.0.0.1:8848/nacos
echo   - bbs-rest Swagger: http://localhost:7012/api/doc.html
echo   - bbs-article Swagger: http://localhost:7011/doc.html
echo   - bbs-user Swagger: http://localhost:7013/doc.html
echo.
echo 提示：请查看各服务窗口中的启动日志
echo.
pause
