#!/bin/bash
# GitHub 提交脚本
# 在 Git Bash 或 WSL 中运行

cd "$(dirname "$0")"

# 初始化 Git 仓库（如果不存在）
if [ ! -d ".git" ]; then
    git init
fi

# 配置用户信息（如果未设置）
if [ -z "$(git config user.name)" ]; then
    git config user.name "ArrokothKing"
fi
if [ -z "$(git config user.email)" ]; then
    git config user.email "your-email@example.com"
fi

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit: ArroDashu 树木品种管理系统

包含内容：
- 后端：Spring Boot 3 + MyBatis Plus + JWT 认证
- 前端：Vue 3 + TypeScript + Element Plus
- 数据库：完整的表结构和初始化数据
- 功能：用户登录、品种 CRUD、分类管理
"

# 添加远程仓库（如果不存在）
if ! git remote | grep -q "origin"; then
    git remote add origin https://github.com/ArrokothKing/arrodashu.git
fi

# 推送到 GitHub
git branch -M main
git push -u origin main

echo "提交完成！"
