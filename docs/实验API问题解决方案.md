# 实验API 404错误解决方案

## 问题描述
前端在访问 `/experiment/1` 接口时出现404错误，错误信息显示请求失败。

## 问题原因分析

### 1. 接口路径不匹配
- **后端接口路径**: `/experiment/project/{id}`
- **前端调用路径**: `/experiment/1`
- **问题**: 前端调用的路径缺少 `/project` 部分

### 2. 服务器端口配置
- **前端运行端口**: `localhost:3000`
- **后端运行端口**: `localhost:8080`
- **问题**: 前端需要正确配置后端API地址

### 3. 缺少API调用文件
- 前端缺少实验相关的API调用文件
- 需要创建统一的API管理文件

## 解决方案

### 1. 创建实验API文件
已创建 `src/main/resources/static/admin/js/api/experiment.js` 文件，包含所有实验相关的API调用方法。

### 2. 修正前端调用路径
前端应该调用正确的API路径：
```javascript
// 错误的方式
axios.get('/experiment/1')

// 正确的方式
axios.get('http://localhost:8080/experiment/project/1')
```

### 3. 创建测试页面
已创建 `src/main/resources/static/admin/test-experiment-api.html` 测试页面，用于验证API是否正常工作。

### 4. 数据库初始化
创建了数据库表结构和测试数据：
- `src/main/resources/sql/init_experiment_tables.sql` - 表结构
- `src/main/resources/sql/init_experiment_data.sql` - 测试数据

## 使用步骤

### 1. 初始化数据库
```sql
-- 执行表结构脚本
source src/main/resources/sql/init_experiment_tables.sql

-- 执行测试数据脚本
source src/main/resources/sql/init_experiment_data.sql
```

### 2. 测试API
访问测试页面：`http://localhost:8080/admin/test-experiment-api.html`

### 3. 在前端项目中使用
```javascript
// 导入API方法
import { getExperimentById, getExperimentList } from './api/experiment.js'

// 使用API
const experiment = await getExperimentById(1)
const list = await getExperimentList()
```

## API接口说明

### 实验项目相关接口
- `GET /experiment/project/list` - 获取所有实验项目
- `GET /experiment/project/{id}` - 获取指定实验项目详情
- `GET /experiment/project/search` - 搜索实验项目
- `POST /experiment/project` - 创建实验项目
- `PUT /experiment/project` - 更新实验项目
- `DELETE /experiment/project/{id}` - 删除实验项目
- `POST /experiment/project/publish` - 发布实验项目
- `GET /experiment/project/my-projects` - 获取我的实验项目

### 请求参数
- `id`: 实验项目ID (路径参数)
- `category`: 实验分类 (查询参数)
- `level`: 难度等级 (查询参数)
- `keyword`: 搜索关键词 (查询参数)

### 响应格式
```json
{
  "id": 1,
  "name": "Java基础语法实验",
  "category": "编程语言",
  "description": "通过实际编程练习掌握Java基础语法",
  "level": "初级",
  "imageUrl": "https://example.com/images/java-basic.jpg",
  "videoUrl": "https://example.com/videos/java-basic.mp4",
  "createdBy": "teacher1",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00",
  "auditStatus": "approved",
  "publishStatus": "published"
}
```

## 注意事项

1. **CORS配置**: 后端已配置允许 `localhost:3000` 的跨域请求
2. **认证**: 部分接口需要JWT认证，请确保请求头包含有效的Authorization
3. **数据库**: 确保MySQL服务正常运行，数据库连接配置正确
4. **端口**: 确保后端服务在8080端口正常运行

## 故障排除

### 1. 仍然出现404错误
- 检查后端服务是否正常运行
- 确认接口路径是否正确
- 查看后端日志是否有错误信息

### 2. CORS错误
- 确认前端地址是否在CORS配置中
- 检查请求头是否正确设置

### 3. 数据库连接错误
- 检查数据库服务是否启动
- 确认数据库连接配置是否正确
- 验证数据库表是否已创建 