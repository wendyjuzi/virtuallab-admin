# API接口使用指南

## 邮箱验证码发送接口

### 问题描述

前端请求 `http://localhost:3001/api/auth/factor/email/send` 返回403 Forbidden错误。

### 解决方案

#### 1. 正确的接口路径

**推荐使用**: `/api/auth/email/send-code`

**请求方式**: POST

**请求参数**:
- `email`: 邮箱地址（必填）
- `type`: 验证码类型（可选，默认为LOGIN）

**支持的验证码类型**:
- `REGISTER` - 注册验证码
- `LOGIN` - 登录验证码  
- `RESET_PASSWORD` - 重置密码验证码

#### 2. 请求示例

```bash
# 发送登录验证码
curl -X POST "http://localhost:8080/api/auth/email/send-code" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=test@example.com&type=LOGIN"

# 发送注册验证码
curl -X POST "http://localhost:8080/api/auth/email/send-code" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=test@example.com&type=REGISTER"
```

#### 3. 响应示例

```json
{
  "code": 200,
  "message": "操作成功",
  "data": true
}
```

### 其他可用的邮箱接口

#### 1. 邮箱验证码登录

**接口**: `POST /api/auth/email/login`

**参数**:
- `email`: 邮箱地址
- `code`: 验证码

#### 2. 验证邮箱验证码

**接口**: `POST /api/auth/email/verify`

**参数**:
- `email`: 邮箱地址
- `code`: 验证码
- `type`: 验证码类型

#### 3. 检查邮箱是否已验证

**接口**: `GET /api/auth/email/check`

**参数**:
- `email`: 邮箱地址

### 前端代码示例

#### JavaScript/Axios

```javascript
// 发送邮箱验证码
async function sendEmailCode(email, type = 'LOGIN') {
  try {
    const response = await axios.post('/api/auth/email/send-code', {
      email: email,
      type: type
    }, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });
    
    if (response.data.code === 200) {
      console.log('验证码发送成功');
      return true;
    } else {
      console.error('验证码发送失败:', response.data.message);
      return false;
    }
  } catch (error) {
    console.error('请求失败:', error);
    return false;
  }
}

// 使用示例
sendEmailCode('test@example.com', 'LOGIN');
```

#### Fetch API

```javascript
// 发送邮箱验证码
async function sendEmailCode(email, type = 'LOGIN') {
  try {
    const formData = new FormData();
    formData.append('email', email);
    formData.append('type', type);
    
    const response = await fetch('/api/auth/email/send-code', {
      method: 'POST',
      body: formData
    });
    
    const data = await response.json();
    
    if (data.code === 200) {
      console.log('验证码发送成功');
      return true;
    } else {
      console.error('验证码发送失败:', data.message);
      return false;
    }
  } catch (error) {
    console.error('请求失败:', error);
    return false;
  }
}
```

### 注意事项

1. **端口配置**: 确保前端请求的端口与后端服务端口一致
2. **CORS配置**: 后端已配置CORS，支持跨域请求
3. **邮箱配置**: 确保邮箱服务配置正确（参考QQ邮箱配置指南）
4. **验证码有效期**: 验证码有效期为10分钟
5. **请求频率**: 建议限制验证码发送频率，防止滥用

### 错误处理

#### 常见错误码

- `200`: 操作成功
- `400`: 参数错误
- `403`: 权限不足（已解决）
- `500`: 服务器内部错误

#### 错误响应示例

```json
{
  "code": 400,
  "message": "邮箱地址不能为空",
  "data": null
}
```

### 调试建议

1. **检查网络请求**: 使用浏览器开发者工具查看网络请求
2. **查看后端日志**: 检查Spring Boot应用日志
3. **验证邮箱配置**: 确认邮箱服务配置正确
4. **测试接口**: 使用Postman等工具测试接口

### 相关文件

- `src/main/java/com/edu/virtuallab/auth/controller/EmailVerificationController.java`
- `src/main/java/com/edu/virtuallab/auth/controller/AuthFactorController.java`
- `src/main/java/com/edu/virtuallab/auth/service/EmailVerificationService.java`
- `docs/QQ邮箱配置指南.md` 