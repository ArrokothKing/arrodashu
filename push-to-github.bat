:: GitHub 提交脚本 (Windows CMD)
:: 双击运行或在 CMD 中执行

cd /d "%~dp0"

:: 初始化 Git 仓库（如果不存在）
if not exist ".git" (
    git init
)

:: 配置用户信息（如果未设置）
git config user.name "ArrokothKing"

:: 添加所有文件
git add .

:: 提交
git commit -m "Initial commit: ArroDashu 树木品种管理系统"

:: 添加远程仓库（如果不存在）
git remote get-url origin >nul 2>&1
if errorlevel 1 (
    git remote add origin https://github.com/ArrokothKing/arrodashu.git
)

:: 推送到 GitHub
git branch -M main
git push -u origin main

echo 提交完成！
pause
