# Blog

**个人表达 + 轻互动**，尝试在项目中进行开发一个简单的博客网站，感受软件开发的全流程



**第一，技术调研与技术选型的探索性。**
 我在调研市面上主流的个人博客后发现，大多数都是基于现成的博客框架（如 WordPress、Hexo）直接搭建的，虽然省事，但可控性和可定制化能力较弱。
 我希望基于 **Vue + Spring Boot** 自行搭建，从架构设计、数据库建模到前后端分离的 API 交互都由自己完成，并尽可能使用新的技术栈，比如 Vite、Pinia、JWT、Docker 部署等，锻炼全栈开发能力。

**第二，功能定位更简洁易用。**
 很多现成的博客功能非常冗余，包含大量不必要的插件与模块，导致性能下降、维护成本高。我的设计目标是：

- **核心功能聚焦**：文章发布、评论互动、标签分类、后台管理
- **UI 简洁**：响应式布局，PC 和移动端都能快速加载
- **个人定制化**：方便调整布局、配色、模块结构，满足我个人写作与展示的需求

**第三，工程化实践与上线能力。**
 相比写一些只在本地运行的课程小项目，这个博客会覆盖一个完整的上线流程，包括：

- 从零开始的前后端架构搭建
- 数据库设计与 API 开发
- 前后端联调与状态管理
- Docker 容器化部署 + Nginx 反向代理 + HTTPS 配置
- 线上环境性能优化与安全加固
   这样可以让我实实在在地体验一个**可落地、可上线的工程项目**的全流程。



### **阶段 1：项目初始化（1-2天）**

- **后端**
  1. 创建 Spring Boot 项目（Spring Initializr）
  2. 配置 MySQL、JPA/MyBatis、Swagger、Lombok
  3. 创建基础包结构（controller、service、entity、repository、config）
  4. 写一个测试 API（`GET /ping` 返回 "pong"）
- **前端**
  1. 使用 Vue CLI / Vite 创建项目
  2. 配置路由（Vue Router）
  3. 配置 Axios（全局请求拦截）
  4. 做一个测试页面调用 `/ping`

------

### **阶段 2：用户系统（3-5天）**

- **后端**
  1. 设计用户表 `user`（id, username, password, role）
  2. 实现注册 `POST /api/auth/register`
  3. 实现登录 `POST /api/auth/login`（JWT）
  4. 实现获取当前用户 `GET /api/auth/me`
- **前端**
  1. 登录页 + 注册页
  2. 全局状态管理（Vuex/Pinia）
  3. 登录后保存 Token 到 localStorage
  4. 退出登录逻辑

------

### **阶段 3：文章 CRUD（5-7天）**

- **后端**
  1. 设计文章表 `article`（id, title, content, author_id, create_time, update_time）
  2. 实现：
     - 获取文章列表 `GET /api/articles`
     - 获取文章详情 `GET /api/articles/{id}`
     - 新建文章 `POST /api/articles`
     - 更新文章 `PUT /api/articles/{id}`
     - 删除文章 `DELETE /api/articles/{id}`
- **前端**
  1. 文章列表页（分页）
  2. 文章详情页
  3. 文章编辑页（Markdown 编辑器）
  4. 文章管理页（后台）

------

### **阶段 4：评论系统（3-4天）**

- **后端**
  1. 设计评论表 `comment`（id, article_id, user_id, content, create_time）
  2. 实现：
     - 获取评论列表 `GET /api/articles/{id}/comments`
     - 新增评论 `POST /api/comments`
     - 删除评论 `DELETE /api/comments/{id}`
- **前端**
  1. 评论列表组件
  2. 评论输入框组件
  3. 删除评论功能（管理员）

------

### **阶段 5：标签与分类（2-3天）**

- **后端**
  1. 设计 `tag`、`category`、`article_tag` 表
  2. 实现文章分类与标签 API
- **前端**
  1. 标签云组件
  2. 分类筛选组件

------

### **阶段 6：后台管理系统（5-7天）**

- **后端**
  1. 管理员权限校验（Spring Security）
  2. 用户管理 API
  3. 文章管理 API
  4. 评论管理 API
- **前端**
  1. 管理员后台布局
  2. 用户管理页面
  3. 文章管理页面
  4. 评论管理页面

------

### **阶段 7：优化与部署（持续迭代）**

- **优化**
  - 缓存热门文章（Redis）
  - 添加文章浏览量
  - 接入 SEO（生成 Sitemap）
- **部署**
  1. 写 Dockerfile（前后端分离）
  2. 配置 Nginx
  3. 配置 HTTPS
  4. 推到云服务器（阿里云/Tencent Cloud）



```less
[ 顶部导航栏 ]  首页 | 博客 | 项目 | 关于我

[ HERO 区 ]
  -----------------------------------------------------
  | 头像 | 我是 XXX   | [一句话介绍: 热爱技术, 分享生活] |
  |      | Java / Vue / MySQL / Docker / K8s           |
  -----------------------------------------------------

[ 内容区 ]
  最新文章     精选技术     随笔生活
  [卡片1]     [卡片1]     [卡片1]
  [卡片2]     [卡片2]     [卡片2]

[ 项目展示区 ]
  GitHub 项目卡片 + 技术栈展示条

[ 页脚 ]
  邮箱 | GitHub | 微信二维码 | 备案号

```





# 文章接口开发

## 文章相关数据表设计

### **1.文章表 `article`**

存放文章的主体信息

```sql
CREATE TABLE article (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    cover_url VARCHAR(500),
    content LONGTEXT NOT NULL,
    publish_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    views BIGINT DEFAULT 0,
    likes BIGINT DEFAULT 0,
    comments BIGINT DEFAULT 0
);
```

**说明：**

- `content` 可以直接存长文本，也可以考虑存到 `article_content` 表分离（如果文章内容很大且访问频率低）。
- `views/likes/comments` 这种统计字段可能会被缓存处理，不一定实时更新数据库。

### **2.标签表`tag`**

因为一个文章可能有多个标签，所以是多对多关系。

**标签表 `tag`**

```
CREATE TABLE tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);
```

**文章-标签关系表 `article_tag`**

```
CREATE TABLE article_tag (
    article_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY(article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES article(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);
```

### **3. 评论表 `comment`**

存储评论，支持多级评论（可通过 `parent_id` 实现）

```
CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    parent_id BIGINT DEFAULT NULL,
    FOREIGN KEY (article_id) REFERENCES article(id)
);
```

- 如果文章访问量很大，可以把 `views/likes/comments` 这些字段放缓存里（Redis）做异步更新，避免写热点。
- 文章 `content` 很大且访问频率低时，可以拆到单独表 `article_content`。

## 文章内容渲染如何保证结构不丢失

