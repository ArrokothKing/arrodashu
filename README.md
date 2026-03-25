# 大树（ArroDashu）项目启动指南

## 环境要求

- **Java**: JDK 17 或更高版本
- **Node.js**: 18.x 或更高版本
- **MySQL**: 8.0
- **Redis**: 7.x

---

## 1. 数据库准备

### 1.1 安装 MySQL 和 Redis

**方式一：使用 Docker（推荐）**
```bash
cd arrodashu
docker-compose up -d
```

**方式二：本地安装**
- 安装 MySQL 8.0，创建数据库 `arrodashu`
- 安装 Redis 7.x

### 1.2 初始化数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source arrodashu-server/src/main/resources/db/init.sql
```

或直接使用 MySQL 客户端导入 `init.sql` 文件。

---

## 2. 启动后端

### 2.1 使用 IntelliJ IDEA（推荐）

1. 打开 `arrodashu-server` 文件夹
2. 等待 Maven 依赖下载完成
3. 找到 `ArrodashuServerApplication.java`
4. 点击运行按钮

### 2.2 使用命令行

```bash
cd arrodashu-server

# 编译
mvn clean compile

# 运行
mvn spring-boot:run
```

后端服务将启动在 **http://localhost:8080**

---

## 3. 启动前端

### 3.1 安装依赖

```bash
cd arrodashu-web

# 使用 npm
npm install

# 或使用 yarn
yarn install

# 或使用 pnpm
pnpm install
```

### 3.2 启动开发服务器

```bash
# 使用 npm
npm run dev

# 或使用 yarn
yarn dev

# 或使用 pnpm
pnpm dev
```

前端服务将启动在 **http://localhost:3000**

---

## 4. 访问系统

- **前端页面**: http://localhost:3000
- **后端 API**: http://localhost:8080

### 测试账号
- 用户名: `admin`
- 密码: `admin123`

---

## 5. 常见问题

### Q: 后端启动失败，提示数据库连接错误
A: 检查 MySQL 是否已启动，以及 `application.yml` 中的数据库配置是否正确。

### Q: 前端 npm install 失败
A: 
1. 检查 Node.js 版本是否 >= 18
2. 尝试使用管理员权限运行
3. 或更换 npm 镜像源：`npm config set registry https://registry.npmmirror.com`

### Q: 登录时提示 401
A: 检查后端服务是否正常启动，以及前端代理配置是否正确。

---

## 6. 项目结构

```
arrodashu/
├── arrodashu-server/     # 后端 Spring Boot 项目
│   ├── src/main/java/    # Java 源代码
│   ├── src/main/resources/
│   │   ├── db/init.sql   # 数据库初始化脚本
│   │   └── application.yml  # 配置文件
│   └── pom.xml           # Maven 配置
├── arrodashu-web/        # 前端 Vue 3 项目
│   ├── src/              # 源代码
│   ├── package.json      # npm 配置
│   └── vite.config.ts    # Vite 配置
└── docker-compose.yml    # Docker 开发环境
```

---

## 7. API 接口文档

### 认证接口
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/auth/info` - 获取用户信息
- `POST /api/auth/logout` - 登出

### 品种接口
- `GET /api/species` - 获取品种列表
- `GET /api/species/{id}` - 获取品种详情
- `POST /api/species` - 添加品种
- `PUT /api/species/{id}` - 更新品种
- `DELETE /api/species/{id}` - 删除品种

### 分类接口
- `GET /api/category/families` - 获取科列表
- `GET /api/category/genera` - 获取属列表
- `GET /api/category/family/{familyId}/genera` - 根据科获取属

---

**祝使用愉快！**
# arrodashu
