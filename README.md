# LiteBlog

用Trae CN的全新Solo模式Vibe Coding出来的一个简洁优雅的个人博客系统，基于 Vue 3 + Spring Boot 构建。

## ✨ 功能特性

- 📝 **文章管理** - 支持 Markdown 编辑、草稿保存、标签分类
- 💬 **评论系统** - 支持评论回复、删除管理
- 👤 **用户系统** - 注册登录、个人中心、头像设置
- 🔐 **权限控制** - 管理员后台、路由守卫
- 🎨 **响应式设计** - 适配桌面端和移动端
- 🚀 **性能优化** - Vite 构建、代码分割、懒加载

## 🛠 技术栈

### 前端
| 技术 | 说明 |
|------|------|
| Vue 3 | 渐进式 JavaScript 框架 |
| TypeScript | 类型安全 |
| Vite | 下一代前端构建工具 |
| Pinia | 状态管理 |
| Vue Router | 路由管理 |
| Element Plus | UI 组件库 |
| Tailwind CSS | 原子化 CSS 框架 |
| Axios | HTTP 请求库 |
| Marked | Markdown 解析器 |

### 后端
| 技术 | 说明 |
|------|------|
| Spring Boot 3.2 | Java Web 框架 |
| Spring Data JPA | ORM 框架 |
| Spring Security | 安全框架 |
| MySQL | 关系型数据库 |
| JWT | 身份认证 |
| Redis | 缓存 & 限流 |

## 📸 项目截图

> 可在此处添加项目截图

## 🚀 快速开始

### 环境要求

- Node.js 18+
- Java 17+
- MySQL 8.0+
- Redis (可选，用于限流)

### 1. 克隆项目

```bash
git clone https://github.com/your-username/LiteBlog.git
cd LiteBlog
```

### 2. 配置数据库

```sql
CREATE DATABASE liteblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置后端

修改 `backend/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/liteblog
spring.datasource.username=root
spring.datasource.password=your_password

jwt.secret=your-secret-key
```

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端运行在 http://localhost:8080

### 5. 启动前端

```bash
# 回到项目根目录
cd ..
npm install
npm run dev
```

前端运行在 http://localhost:5173

### 6. 访问应用

打开浏览器访问 http://localhost:5173

**默认管理员账号：**
- 邮箱：`admin@example.com`
- 密码：`admin123`

## 📁 项目结构

```
LiteBlog/
├── src/                        # 前端源码
│   ├── views/                  # 页面组件
│   │   ├── Home.vue           # 首页
│   │   ├── ArticleDetail.vue  # 文章详情
│   │   ├── Profile.vue        # 个人中心
│   │   ├── Admin.vue          # 管理后台
│   │   └── ...
│   ├── components/             # 公共组件
│   ├── stores/                 # Pinia 状态管理
│   │   ├── user.ts            # 用户状态
│   │   ├── article.ts         # 文章状态
│   │   └── comment.ts         # 评论状态
│   ├── router/                 # 路由配置
│   ├── utils/                  # 工具函数
│   └── style.css              # 全局样式
├── backend/                    # 后端源码
│   └── src/main/java/com/example/blog/
│       ├── controller/         # 控制器
│       ├── service/            # 业务逻辑
│       ├── repository/         # 数据访问
│       ├── model/              # 实体模型
│       ├── config/             # 配置类
│       └── security/           # 安全配置
├── public/                     # 静态资源
├── API.md                      # API 文档
├── DEPLOY.md                   # 部署文档
├── docker-compose.yml          # Docker 编排
└── nginx.conf                  # Nginx 配置
```

## 📖 文档

- [API 接口文档](./API.md)
- [部署说明](./DEPLOY.md)

## 🔧 配置说明

### 前端环境变量

创建 `.env` 文件：

```env
VITE_API_BASE_URL=http://localhost:8080
```

### 后端配置

主要配置项：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 服务端口 | 8080 |
| `spring.datasource.url` | 数据库连接 | - |
| `jwt.secret` | JWT 密钥 | - |
| `jwt.expiration` | Token 过期时间 | 86400000 |

## 🐳 Docker 部署

```bash
# 构建并启动
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## 📝 开发命令

```bash
# 前端开发
npm run dev          # 启动开发服务器
npm run build        # 构建生产版本
npm run preview      # 预览构建结果
npm run lint         # 代码检查

# 后端开发
cd backend
mvn spring-boot:run       # 启动开发服务器
mvn clean package         # 打包
mvn test                  # 运行测试
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 🙏 致谢

- [Vue.js](https://vuejs.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Element Plus](https://element-plus.org/)
- [Tailwind CSS](https://tailwindcss.com/)
