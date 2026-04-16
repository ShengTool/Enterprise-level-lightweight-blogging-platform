# 部署说明

## 1. 环境要求

### 1.1 前端环境
- Node.js 16+ 
- npm 7+ 或 pnpm 6+

### 1.2 后端环境
- Java 17+
- Maven 3.8+
- MySQL 8.0.26+

## 2. 数据库配置

1. 登录MySQL数据库
2. 创建数据库：
   ```sql
   CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 导入数据库表结构和初始数据：
   ```bash
   mysql -u root -p blog < backend/src/main/resources/schema.sql
   ```
4. 修改 `backend/src/main/resources/application.properties` 文件中的数据库连接配置：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/blog?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=123456
   ```

## 3. 后端部署

### 3.1 构建后端项目
```bash
cd backend
mvn clean package
```

### 3.2 运行后端服务
```bash
java -jar target/blog-backend-1.0-SNAPSHOT.jar
```

后端服务将在 `http://localhost:8080` 上运行。

## 4. 前端部署

### 4.1 安装依赖
```bash
cd frontend
npm install
# 或
pnpm install
```

### 4.2 构建前端项目
```bash
npm run build
# 或
pnpm run build
```

### 4.3 本地开发运行
```bash
npm run dev
# 或
pnpm run dev
```

前端服务将在 `http://localhost:5173` 上运行。

### 4.4 生产环境部署
将 `dist` 目录下的文件部署到任何静态文件服务器，如 Nginx、Apache 等。

## 5. 配置说明

### 5.1 前端配置
- 修改 `src/utils/axios.ts` 中的 `baseURL` 为后端服务的实际地址：
  ```typescript
  const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
    // ...
  })
  ```

### 5.2 后端配置
- 修改 `application.properties` 文件中的配置：
  - 服务器端口：`server.port=8080`
  - 数据库连接：`spring.datasource.*`
  - JWT密钥：`jwt.secret=your-secret-key`
  - JWT过期时间：`jwt.expiration=3600000`

## 6. 测试账号

### 6.1 管理员账号
- 邮箱：admin@example.com
- 密码：123456

### 6.2 测试用户账号
- 邮箱：user@example.com
- 密码：123456

## 7. 常见问题

### 7.1 数据库连接失败
- 检查数据库服务是否运行
- 检查数据库连接配置是否正确
- 检查数据库用户权限是否正确

### 7.2 前端无法访问后端API
- 检查后端服务是否运行
- 检查前端 `axios.ts` 中的 `baseURL` 配置是否正确
- 检查跨域配置是否正确

### 7.3 JWT认证失败
- 检查 `application.properties` 中的 `jwt.secret` 是否配置
- 检查token是否过期
- 检查请求头中的Authorization格式是否正确

## 8. 项目结构

### 8.1 前端结构
```
frontend/
├── src/
│   ├── components/          # 组件
│   ├── views/              # 页面
│   ├── stores/             # 状态管理
│   ├── router/             # 路由配置
│   ├── utils/              # 工具函数
│   └── assets/             # 静态资源
├── public/                 # 公共资源
├── index.html              # 入口HTML
├── vite.config.ts          # Vite配置
├── package.json            # 项目依赖
└── tailwind.config.js      # Tailwind配置
```

### 8.2 后端结构
```
backend/
├── src/main/java/com/example/blog/
│   ├── controller/         # 控制器
│   ├── service/            # 业务逻辑
│   ├── repository/         # 数据访问
│   ├── model/              # 数据模型
│   ├── config/             # 配置
│   └── security/           # 安全
├── src/main/resources/
│   ├── application.properties  # 应用配置
│   └── schema.sql         # 数据库脚本
├── pom.xml                # Maven配置
└── BlogApplication.java   # 应用入口
```