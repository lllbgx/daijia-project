@echo off
echo 正在清理微信开发者工具缓存...
echo.

REM 查找并删除可能的缓存文件
if exist "%LOCALAPPDATA%\微信开发者工具\*" (
    echo 发现微信开发者工具目录，正在清理...
    REM 这里只是示例，实际清理需要谨慎
    echo 请手动清理：退出微信开发者工具，删除 %LOCALAPPDATA%\微信开发者工具\Cache 目录
) else (
    echo 未找到微信开发者工具缓存目录
)

echo.
echo 清理完成，请重新打开微信开发者工具
echo 如果问题仍然存在，请尝试：
echo 1. 完全退出微信开发者工具
echo 2. 删除项目目录下的 .git 目录（如果有）
echo 3. 重新导入项目
pause