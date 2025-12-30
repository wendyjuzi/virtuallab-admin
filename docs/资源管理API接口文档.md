# 资源管理API接口文档

## 概述

资源管理模块提供了完整的资源CRUD操作、资源共享、复制和交互功能，支持资源的分类管理、状态控制和统计分析。

## 基础信息

- **基础URL**: `http://localhost:8080/resource`
- **数据格式**: JSON
- **字符编码**: UTF-8

## 数据模型

### Resource 资源对象

```json
{
  "id": 1,
  "name": "Java编程基础教程",
  "type": "document",
  "category": "teaching",
  "url": "https://example.com/resources/java-basic-tutorial.pdf",
  "status": "active",
  "description": "Java编程语言基础教程，包含语法、面向对象编程等核心概念",
  "fileSize": 2048576,
  "fileType": "application/pdf",
  "uploader": "teacher1",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### ResourceShare 资源共享对象

```json
{
  "id": 1,
  "resourceId": 1,
  "sharedBy": "teacher1",
  "sharedWith": "student1",
  "permission": "read",
  "status": "active",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00",
  "expiresAt": "2024-12-31T23:59:59"
}
```

### ResourceCopy 资源复制对象

```json
{
  "sourceResourceId": 1,
  "newName": "Java教程副本",
  "newDescription": "Java教程的副本",
  "targetCategory": "teaching",
  "copiedBy": "teacher2",
  "targetUrl": "https://example.com/resources/java-tutorial-copy.pdf"
}
```

### ResourceInteraction 资源交互对象

```json
{
  "id": 1,
  "resourceId": 1,
  "userId": "student1",
  "interactionType": "rate",
  "content": "很好的教程",
  "rating": 5,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

## API接口

### 基础CRUD操作

#### 1. 创建资源

**接口地址**: `POST /resource`

**请求参数**:
```json
{
  "name": "新资源名称",
  "type": "document",
  "category": "teaching",
  "url": "https://example.com/resource.pdf",
  "description": "资源描述",
  "fileSize": 1048576,
  "fileType": "application/pdf",
  "uploader": "teacher1"
}
```

**响应示例**:
```json
1
```

#### 2. 更新资源

**接口地址**: `PUT /resource`

**请求参数**:
```json
{
  "id": 1,
  "name": "更新后的资源名称",
  "type": "document",
  "category": "teaching",
  "url": "https://example.com/updated-resource.pdf",
  "description": "更新后的描述"
}
```

**响应示例**:
```json
1
```

#### 3. 删除资源

**接口地址**: `DELETE /resource/{id}`

**路径参数**:
- `id`: 资源ID

**响应示例**:
```json
1
```

#### 4. 获取资源详情

**接口地址**: `GET /resource/{id}`

**路径参数**:
- `id`: 资源ID

**响应示例**:
```json
{
  "id": 1,
  "name": "Java编程基础教程",
  "type": "document",
  "category": "teaching",
  "url": "https://example.com/resources/java-basic-tutorial.pdf",
  "status": "active",
  "description": "Java编程语言基础教程",
  "fileSize": 2048576,
  "fileType": "application/pdf",
  "uploader": "teacher1",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

#### 5. 获取资源列表

**接口地址**: `GET /resource/list`

**响应示例**:
```json
[
  {
    "id": 1,
    "name": "Java编程基础教程",
    "type": "document",
    "category": "teaching",
    "url": "https://example.com/resources/java-basic-tutorial.pdf",
    "status": "active",
    "description": "Java编程语言基础教程",
    "fileSize": 2048576,
    "fileType": "application/pdf",
    "uploader": "teacher1",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

### 资源共享功能

#### 6. 设置资源共享权限

**接口地址**: `POST /resource/share`

**请求参数**:
```json
{
  "resourceId": 1,
  "sharedBy": "teacher1",
  "sharedWith": "student1",
  "permission": "read",
  "status": "active",
  "expiresAt": "2024-12-31T23:59:59"
}
```

**响应示例**:
```json
1
```

### 资源复制功能

#### 7. 复制资源

**接口地址**: `POST /resource/copy`

**请求参数**:
```json
{
  "sourceResourceId": 1,
  "newName": "Java教程副本",
  "newDescription": "Java教程的副本",
  "targetCategory": "teaching",
  "copiedBy": "teacher2",
  "targetUrl": "https://example.com/resources/java-tutorial-copy.pdf"
}
```

**响应示例**:
```json
{
  "id": 2,
  "name": "Java教程副本",
  "type": "document",
  "category": "teaching",
  "url": "https://example.com/resources/java-tutorial-copy.pdf",
  "status": "active",
  "description": "Java教程的副本",
  "fileSize": 2048576,
  "fileType": "application/pdf",
  "uploader": "teacher2",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 资源交互功能

#### 8. 添加资源交互

**接口地址**: `POST /resource/interaction`

**请求参数**:
```json
{
  "resourceId": 1,
  "userId": "student1",
  "interactionType": "rate",
  "content": "很好的教程",
  "rating": 5
}
```

**响应示例**:
```json
1
```

#### 9. 获取资源交互记录

**接口地址**: `GET /resource/interaction/{resourceId}`

**路径参数**:
- `resourceId`: 资源ID

**响应示例**:
```json
[
  {
    "id": 1,
    "resourceId": 1,
    "userId": "student1",
    "interactionType": "rate",
    "content": "很好的教程",
    "rating": 5,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  },
  {
    "id": 2,
    "resourceId": 1,
    "userId": "student2",
    "interactionType": "comment",
    "content": "内容很详细",
    "rating": null,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

## 枚举值说明

### 资源类型 (type)
- `document` - 文档
- `video` - 视频
- `image` - 图片
- `audio` - 音频
- `other` - 其他

### 资源分类 (category)
- `teaching` - 教学
- `experiment` - 实验
- `reference` - 参考资料
- `tool` - 工具

### 资源状态 (status)
- `active` - 激活
- `inactive` - 停用
- `deleted` - 已删除

### 分享权限 (permission)
- `read` - 只读权限
- `write` - 读写权限
- `admin` - 管理权限

### 分享状态 (status)
- `active` - 激活
- `inactive` - 停用
- `expired` - 已过期

### 交互类型 (interactionType)
- `view` - 查看
- `download` - 下载
- `like` - 点赞
- `comment` - 评论
- `rate` - 评分

## 业务逻辑说明

### 资源共享逻辑

1. **权限检查**: 只有资源所有者或具有管理权限的用户可以分享资源
2. **重复分享**: 如果已经存在相同的分享记录，会更新现有记录
3. **权限继承**: 高级权限包含低级权限（admin > write > read）
4. **过期处理**: 支持设置分享过期时间

### 资源复制逻辑

1. **源资源验证**: 复制前会检查源资源是否存在
2. **自动命名**: 如果没有指定新名称，会自动添加"(副本)"后缀
3. **属性继承**: 复制时会继承源资源的基本属性
4. **权限重置**: 新资源的所有权归复制者所有

### 资源交互逻辑

1. **交互验证**: 交互前会检查资源是否存在
2. **重复处理**: 对于点赞和评分，同一用户只能有一条记录
3. **评分计算**: 支持计算资源的平均评分
4. **统计功能**: 支持统计各种交互类型的数量

## 使用示例

### JavaScript (Axios)

```javascript
// 创建资源
const createResource = async (resourceData) => {
  try {
    const response = await axios.post('http://localhost:8080/resource', resourceData);
    console.log('资源创建成功:', response.data);
  } catch (error) {
    console.error('资源创建失败:', error);
  }
};

// 分享资源
const shareResource = async (shareData) => {
  try {
    const response = await axios.post('http://localhost:8080/resource/share', shareData);
    console.log('资源共享成功:', response.data);
  } catch (error) {
    console.error('资源共享失败:', error);
  }
};

// 复制资源
const copyResource = async (copyData) => {
  try {
    const response = await axios.post('http://localhost:8080/resource/copy', copyData);
    console.log('资源复制成功:', response.data);
  } catch (error) {
    console.error('资源复制失败:', error);
  }
};

// 添加交互
const addInteraction = async (interactionData) => {
  try {
    const response = await axios.post('http://localhost:8080/resource/interaction', interactionData);
    console.log('交互添加成功:', response.data);
  } catch (error) {
    console.error('交互添加失败:', error);
  }
};
```

### cURL

```bash
# 创建资源
curl -X POST http://localhost:8080/resource \
  -H "Content-Type: application/json" \
  -d '{
    "name": "新资源",
    "type": "document",
    "category": "teaching",
    "url": "https://example.com/resource.pdf",
    "description": "资源描述"
  }'

# 分享资源
curl -X POST http://localhost:8080/resource/share \
  -H "Content-Type: application/json" \
  -d '{
    "resourceId": 1,
    "sharedBy": "teacher1",
    "sharedWith": "student1",
    "permission": "read"
  }'

# 复制资源
curl -X POST http://localhost:8080/resource/copy \
  -H "Content-Type: application/json" \
  -d '{
    "sourceResourceId": 1,
    "newName": "资源副本",
    "copiedBy": "teacher2"
  }'

# 添加交互
curl -X POST http://localhost:8080/resource/interaction \
  -H "Content-Type: application/json" \
  -d '{
    "resourceId": 1,
    "userId": "student1",
    "interactionType": "rate",
    "rating": 5
  }'
```

## 错误处理

当接口调用失败时，会抛出相应的异常：

- **资源不存在**: 当访问不存在的资源时
- **权限不足**: 当用户没有相应权限时
- **参数错误**: 当请求参数格式不正确时
- **业务逻辑错误**: 当操作违反业务规则时

## 注意事项

1. **权限控制**: 资源共享功能包含完整的权限控制逻辑
2. **数据一致性**: 所有操作都会进行数据验证
3. **性能优化**: 建议对大量数据的查询添加分页功能
4. **安全性**: 建议在生产环境中添加更严格的权限验证
5. **扩展性**: 代码结构支持后续功能扩展 