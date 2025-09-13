# 多阶段构建 Dockerfile
# 第一阶段：构建 Vue 应用
FROM node:18-alpine AS build-stage

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 package-lock.json
COPY package*.json ./

# 安装依赖
RUN npm ci --only=production

# 复制源代码
COPY . .

# 构建应用
RUN npm run build

# 第二阶段：nginx 服务器
FROM nginx:alpine AS production-stage

# 删除默认的 nginx 配置文件
RUN rm /etc/nginx/conf.d/default.conf

# 复制自定义的 nginx 配置文件
COPY ../nginx/default.conf /etc/nginx/conf.d/

# 从构建阶段复制构建产物到 nginx 的 html 目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 暴露端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]