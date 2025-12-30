// 邮箱验证码发送测试脚本
const axios = require('axios');

const BASE_URL = 'http://localhost:8080';
const TEST_EMAIL = '3278281361@qq.com';

// 测试配置
const config = {
  timeout: 10000,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  }
};

// 测试函数
async function testEmailCodeSending() {
  console.log('🚀 开始测试邮箱验证码发送功能...\n');
  
  const tests = [
    {
      name: '1. 测试通用邮箱验证码发送接口 (表单参数)',
      url: `${BASE_URL}/auth/email/send-code`,
      method: 'POST',
      data: `email=${TEST_EMAIL}&type=LOGIN`,
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    },
    {
      name: '2. 测试JSON格式邮箱验证码发送接口',
      url: `${BASE_URL}/auth/email/send-code-json`,
      method: 'POST',
      data: { email: TEST_EMAIL, type: 'LOGIN' },
      headers: { 'Content-Type': 'application/json' }
    },
    {
      name: '3. 测试灵活格式邮箱验证码发送接口',
      url: `${BASE_URL}/auth/email/send-flexible`,
      method: 'POST',
      data: `email=${TEST_EMAIL}&type=LOGIN`,
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    },
    {
      name: '4. 测试管理员专用邮箱验证码发送接口',
      url: `${BASE_URL}/auth/factor/email/send`,
      method: 'POST',
      data: `email=${TEST_EMAIL}&userType=admin`,
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    }
  ];

  for (const test of tests) {
    try {
      console.log(`📧 ${test.name}`);
      console.log(`   请求URL: ${test.url}`);
      console.log(`   请求方法: ${test.method}`);
      console.log(`   请求数据: ${JSON.stringify(test.data)}`);
      
      const response = await axios({
        method: test.method,
        url: test.url,
        data: test.data,
        headers: test.headers,
        timeout: 10000
      });
      
      console.log(`   ✅ 响应状态: ${response.status}`);
      console.log(`   📄 响应数据: ${JSON.stringify(response.data, null, 2)}`);
      
      if (response.data && response.data.success) {
        console.log(`   🎉 测试成功！验证码已发送到 ${TEST_EMAIL}\n`);
      } else {
        console.log(`   ⚠️ 测试完成，但响应显示失败\n`);
      }
      
    } catch (error) {
      console.log(`   ❌ 测试失败: ${error.message}`);
      if (error.response) {
        console.log(`   状态码: ${error.response.status}`);
        console.log(`   错误信息: ${JSON.stringify(error.response.data, null, 2)}`);
      }
      console.log('');
    }
  }
}

// 测试服务器健康状态
async function testServerHealth() {
  console.log('🏥 测试服务器健康状态...\n');
  
  try {
    const response = await axios.get(`${BASE_URL}/actuator/health`, { timeout: 5000 });
    console.log(`✅ 服务器健康状态: ${response.status}`);
    console.log(`📊 健康信息: ${JSON.stringify(response.data, null, 2)}\n`);
    return true;
  } catch (error) {
    console.log(`❌ 服务器健康检查失败: ${error.message}\n`);
    return false;
  }
}

// 主测试函数
async function runAllTests() {
  console.log('=' * 60);
  console.log('📧 邮箱验证码发送功能测试');
  console.log('=' * 60);
  console.log(`📧 测试邮箱: ${TEST_EMAIL}`);
  console.log(`🌐 服务器地址: ${BASE_URL}`);
  console.log(`⏰ 测试时间: ${new Date().toLocaleString()}\n`);
  
  // 首先测试服务器健康状态
  const serverHealthy = await testServerHealth();
  
  if (serverHealthy) {
    // 然后测试邮箱验证码发送功能
    await testEmailCodeSending();
  } else {
    console.log('❌ 服务器不健康，跳过邮箱验证码测试');
  }
  
  console.log('=' * 60);
  console.log('🏁 测试完成！');
  console.log('=' * 60);
}

// 运行测试
runAllTests().catch(console.error); 