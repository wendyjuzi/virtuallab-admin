# QQ邮箱配置指南

## 配置说明

本项目使用QQ邮箱作为邮件服务器，用于发送验证码等功能。

## 配置步骤

### 1. 获取QQ邮箱授权码

1. 登录QQ邮箱网页版
2. 点击"设置" -> "账户"
3. 找到"POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务"
4. 开启"POP3/SMTP服务"
5. 按照提示获取授权码（不是QQ密码）

### 2. 配置文件设置

在 `application-dev.properties` 中配置以下参数：

```properties
# 邮件服务器配置
spring.mail.host=smtp.qq.com
spring.mail.port=465
spring.mail.username=你的QQ邮箱@qq.com
spring.mail.password=你的授权码
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=false
spring.mail.properties.mail.smtp.starttls.required=false
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
spring.mail.properties.mail.smtp.socketFactory.port=465
spring.mail.properties.mail.smtp.socketFactory.fallback=false
# 发件人配置
spring.mail.from=你的QQ邮箱@qq.com
```

### 3. 端口说明

- **465端口**：SSL加密连接，推荐使用
- **587端口**：STARTTLS加密连接，可能在某些网络环境下不稳定

### 4. 常见问题

#### 连接失败
- 检查网络连接
- 确认授权码是否正确
- 检查防火墙设置

#### SSL错误
- 使用465端口而不是587端口
- 确保SSL配置正确

#### 授权失败
- 确认已开启SMTP服务
- 重新获取授权码

## 测试邮件功能

启动应用后，可以通过以下API测试邮件功能：

```
POST /api/auth/sendEmailCode
Content-Type: application/json

{
  "email": "测试邮箱@qq.com",
  "type": "LOGIN"
}
```

## 安全注意事项

1. **不要将授权码提交到版本控制系统**
2. **在生产环境中使用环境变量存储敏感信息**
3. **定期更换授权码**
4. **限制邮件发送频率，防止被识别为垃圾邮件**

## 生产环境配置

在生产环境中，建议：

1. 使用环境变量存储邮箱配置
2. 配置邮件发送频率限制
3. 添加邮件发送日志
4. 配置邮件发送失败重试机制 