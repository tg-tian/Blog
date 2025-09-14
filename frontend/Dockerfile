FROM node:20-alpine AS build-stage

# 1. 设置工作目录
WORKDIR /app

# 2. 复制依赖清单
COPY frontend/package*.json ./

# 3. 安装依赖
# 使用 npm ci 以确保根据 lock 文件精确安装，速度更快且更可靠
RUN npm ci

# 4. 复制配置文件和静态入口
# 这些文件不经常变动，单独复制以利用缓存
COPY frontend/vite.config.js ./
COPY frontend/tailwind.config.js ./
COPY frontend/postcss.config.js ./
COPY frontend/index.html ./

# 5. 复制源代码和公共资源
# 源代码变动最频繁，放在后面，这样代码变更不会导致前面的层缓存失效
COPY frontend/src/ ./src
COPY frontend/public/ ./public

# 6. 设置环境变量为生产环境
ENV NODE_ENV=production

# 7. 构建应用
RUN npm run build


FROM nginx:1.25-alpine AS production-stage

# 1. 复制自定义的 Nginx 配置文件
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

# 2. 从构建阶段复制构建产物
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 3. 暴露 80 端口
EXPOSE 80

# 4. 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]