# API文档

## 1. 用户认证API

### 1.1 注册
- **路径**: `/api/auth/register`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "username": "用户名",
    "email": "邮箱",
    "password": "密码"
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "username": "用户名",
    "email": "邮箱",
    "avatar": "头像URL",
    "is_admin": false
  }
  ```

### 1.2 登录
- **路径**: `/api/auth/login`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "email": "邮箱",
    "password": "密码"
  }
  ```
- **响应**:
  ```json
  {
    "token": "JWT令牌",
    "user": {
      "id": 1,
      "username": "用户名",
      "email": "邮箱",
      "avatar": "头像URL",
      "is_admin": false
    }
  }
  ```

### 1.3 获取个人信息
- **路径**: `/api/auth/profile`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
  ```json
  {
    "id": 1,
    "username": "用户名",
    "email": "邮箱",
    "avatar": "头像URL",
    "is_admin": false
  }
  ```

### 1.4 更新个人信息
- **路径**: `/api/auth/profile`
- **方法**: `PUT`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "username": "新用户名",
    "avatar": "新头像URL"
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "username": "新用户名",
    "email": "邮箱",
    "avatar": "新头像URL",
    "is_admin": false
  }
  ```

## 2. 文章API

### 2.1 获取文章列表
- **路径**: `/api/articles`
- **方法**: `GET`
- **查询参数**:
  - `page`: 页码 (默认: 1)
  - `limit`: 每页数量 (默认: 10)
  - `tag`: 标签名称 (可选)
- **响应**:
  ```json
  {
    "data": [
      {
        "id": 1,
        "title": "文章标题",
        "summary": "文章摘要",
        "status": "PUBLISHED",
        "view_count": 100,
        "created_at": "2024-01-01T00:00:00",
        "updated_at": "2024-01-01T00:00:00",
        "user": {
          "username": "用户名",
          "avatar": "头像URL"
        },
        "tags": [
          {
            "id": 1,
            "name": "标签名称",
            "color": "#3B82F6"
          }
        ]
      }
    ],
    "total": 10
  }
  ```

### 2.2 获取文章详情
- **路径**: `/api/articles/{id}`
- **方法**: `GET`
- **响应**:
  ```json
  {
    "id": 1,
    "title": "文章标题",
    "content": "文章内容",
    "summary": "文章摘要",
    "status": "PUBLISHED",
    "view_count": 100,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00",
    "user": {
      "username": "用户名",
      "avatar": "头像URL"
    },
    "tags": [
      {
        "id": 1,
        "name": "标签名称",
        "color": "#3B82F6"
      }
    ]
  }
  ```

### 2.3 创建文章
- **路径**: `/api/articles`
- **方法**: `POST`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "title": "文章标题",
    "content": "文章内容",
    "summary": "文章摘要",
    "status": "PUBLISHED",
    "tags": [
      {
        "id": 1,
        "name": "标签名称"
      }
    ]
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "title": "文章标题",
    "content": "文章内容",
    "summary": "文章摘要",
    "status": "PUBLISHED",
    "user_id": 1,
    "view_count": 0,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00",
    "tags": [
      {
        "id": 1,
        "name": "标签名称",
        "color": "#3B82F6"
      }
    ]
  }
  ```

### 2.4 更新文章
- **路径**: `/api/articles/{id}`
- **方法**: `PUT`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "title": "新文章标题",
    "content": "新文章内容",
    "summary": "新文章摘要",
    "status": "PUBLISHED",
    "tags": [
      {
        "id": 1,
        "name": "标签名称"
      }
    ]
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "title": "新文章标题",
    "content": "新文章内容",
    "summary": "新文章摘要",
    "status": "PUBLISHED",
    "user_id": 1,
    "view_count": 100,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-02T00:00:00",
    "tags": [
      {
        "id": 1,
        "name": "标签名称",
        "color": "#3B82F6"
      }
    ]
  }
  ```

### 2.5 删除文章
- **路径**: `/api/articles/{id}`
- **方法**: `DELETE`
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
  ```json
  "Article deleted"
  ```

### 2.6 保存草稿
- **路径**: `/api/articles/{id}/draft`
- **方法**: `PUT`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "title": "草稿标题",
    "content": "草稿内容",
    "summary": "草稿摘要",
    "status": "DRAFT",
    "tags": [
      {
        "id": 1,
        "name": "标签名称"
      }
    ]
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "title": "草稿标题",
    "content": "草稿内容",
    "summary": "草稿摘要",
    "status": "DRAFT",
    "user_id": 1,
    "view_count": 0,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-02T00:00:00",
    "tags": [
      {
        "id": 1,
        "name": "标签名称",
        "color": "#3B82F6"
      }
    ]
  }
  ```

## 3. 标签API

### 3.1 获取标签列表
- **路径**: `/api/tags`
- **方法**: `GET`
- **响应**:
  ```json
  [
    {
      "id": 1,
      "name": "标签名称",
      "color": "#3B82F6",
      "count": 10,
      "created_at": "2024-01-01T00:00:00",
      "updated_at": "2024-01-01T00:00:00"
    }
  ]
  ```

### 3.2 创建标签
- **路径**: `/api/tags`
- **方法**: `POST`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "name": "标签名称",
    "color": "#3B82F6"
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "name": "标签名称",
    "color": "#3B82F6",
    "count": 0,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00"
  }
  ```

### 3.3 更新标签
- **路径**: `/api/tags/{id}`
- **方法**: `PUT`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "name": "新标签名称",
    "color": "#10B981"
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "name": "新标签名称",
    "color": "#10B981",
    "count": 10,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-02T00:00:00"
  }
  ```

### 3.4 删除标签
- **路径**: `/api/tags/{id}`
- **方法**: `DELETE`
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
  ```json
  "Tag deleted"
  ```

## 4. 评论API

### 4.1 获取评论列表
- **路径**: `/api/comments`
- **方法**: `GET`
- **查询参数**:
  - `articleId`: 文章ID
- **响应**:
  ```json
  [
    {
      "id": 1,
      "content": "评论内容",
      "article_id": 1,
      "user_id": 1,
      "parent_id": null,
      "created_at": "2024-01-01T00:00:00",
      "updated_at": "2024-01-01T00:00:00",
      "user": {
        "username": "用户名",
        "avatar": "头像URL"
      }
    }
  ]
  ```

### 4.2 创建评论
- **路径**: `/api/comments`
- **方法**: `POST`
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
  ```json
  {
    "content": "评论内容",
    "article_id": 1,
    "parent_id": null
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "content": "评论内容",
    "article_id": 1,
    "user_id": 1,
    "parent_id": null,
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00",
    "user": {
      "username": "用户名",
      "avatar": "头像URL"
    }
  }
  ```

### 4.3 删除评论
- **路径**: `/api/comments/{id}`
- **方法**: `DELETE`
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
  ```json
  "Comment deleted"
  ```