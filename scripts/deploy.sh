#!/bin/bash

# 脚本在任何命令失败时立即退出，并打印出所有执行的命令
set -ex

# 进入项目根目录
# 注意：此脚本假定它是在目标服务器的 /home/tgapk/Blog 目录中执行的
cd /home/tgapk/Blog

# 从远程仓库拉取最新的 main 分支代码
echo "Fetching latest code from origin/main..."
git fetch origin main
git reset --hard origin/main

# 使用 Docker Compose 拉取最新的镜像
docker compose pull

# 以后台模式启动或更新服务
docker compose up -d

# 清理不再使用的 Docker 资源（悬空镜像、停止的容器等）
docker system prune -f

echo "Deployment script completed successfully!"