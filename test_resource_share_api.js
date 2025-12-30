const axios = require('axios');

// 配置基础URL和认证信息
const BASE_URL = 'http://localhost:8080';
const TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqejExIiwiaWF0IjoxNzUxNzA0NzY4LCJleHAiOjE3NTIzMDk1Njh9.ei2ZWiy6FYlf8GvnxIqWEd3ph4TWcOFuL3XcrVlryPU';

// 测试参数
const TEST_RESOURCE_ID = 13;
const TEST_TARGET_USERNAME = 'student1';

// 配置axios默认设置
axios.defaults.baseURL = BASE_URL;
axios.defaults.headers.common['Authorization'] = `Bearer ${TOKEN}`;
axios.defaults.headers.common['Content-Type'] = 'application/json';

async function testResourceShareAPI() {
    console.log('开始测试资源分享功能API...\n');

    try {
        // 1. 测试生成分享链接
        console.log('1. 测试生成分享链接');
        try {
            const generateResponse = await axios.post('/resource-share/generate', null, {
                params: { 
                    resourceId: TEST_RESOURCE_ID, 
                    expireMinutes: 1440 
                }
            });
            console.log('✅ 生成分享链接成功:', generateResponse.data);
        } catch (error) {
            console.log('❌ 生成分享链接失败:', error.response?.data || error.message);
        }

        // 2. 测试通过用户名分享资源
        console.log('\n2. 测试通过用户名分享资源');
        try {
            const shareResponse = await axios.post('/resource-share/share-by-username', null, {
                params: { 
                    resourceId: TEST_RESOURCE_ID, 
                    targetUsername: TEST_TARGET_USERNAME, 
                    permission: 'write' 
                }
            });
            console.log('✅ 分享资源成功:', shareResponse.data);
        } catch (error) {
            console.log('❌ 分享资源失败:', error.response?.data || error.message);
        }

        // 3. 测试查询资源的分享列表
        console.log('\n3. 测试查询资源的分享列表');
        try {
            const listResponse = await axios.get('/resource-share/list', {
                params: { resourceId: TEST_RESOURCE_ID }
            });
            console.log('✅ 查询分享列表成功:', listResponse.data);
        } catch (error) {
            console.log('❌ 查询分享列表失败:', error.response?.data || error.message);
        }

        // 4. 测试获取我分享的资源列表
        console.log('\n4. 测试获取我分享的资源列表');
        try {
            const sharedByMeResponse = await axios.get('/resource-share/shared-by-me');
            console.log('✅ 获取我分享的资源列表成功:', sharedByMeResponse.data);
        } catch (error) {
            console.log('❌ 获取我分享的资源列表失败:', error.response?.data || error.message);
        }

        // 5. 测试获取分享给我的资源列表
        console.log('\n5. 测试获取分享给我的资源列表');
        try {
            const sharedWithMeResponse = await axios.get('/resource-share/shared-with-me');
            console.log('✅ 获取分享给我的资源列表成功:', sharedWithMeResponse.data);
        } catch (error) {
            console.log('❌ 获取分享给我的资源列表失败:', error.response?.data || error.message);
        }

        // 6. 测试检查资源访问权限
        console.log('\n6. 测试检查资源访问权限');
        try {
            const checkAccessResponse = await axios.get('/resource-share/check-access', {
                params: { 
                    resourceId: TEST_RESOURCE_ID, 
                    permission: 'write' 
                }
            });
            console.log('✅ 检查访问权限成功:', checkAccessResponse.data);
        } catch (error) {
            console.log('❌ 检查访问权限失败:', error.response?.data || error.message);
        }

        // 7. 测试更新分享权限
        console.log('\n7. 测试更新分享权限');
        try {
            const updatePermissionResponse = await axios.put('/resource-share/update-permission/1', null, {
                params: { permission: 'admin' }
            });
            console.log('✅ 更新分享权限成功:', updatePermissionResponse.data);
        } catch (error) {
            console.log('❌ 更新分享权限失败:', error.response?.data || error.message);
        }

        // 8. 测试撤销分享
        console.log('\n8. 测试撤销分享');
        try {
            const revokeResponse = await axios.post('/resource-share/revoke/1');
            console.log('✅ 撤销分享成功:', revokeResponse.data);
        } catch (error) {
            console.log('❌ 撤销分享失败:', error.response?.data || error.message);
        }

        // 9. 测试取消分享
        console.log('\n9. 测试取消分享');
        try {
            const cancelResponse = await axios.delete('/resource-share/cancel', {
                params: { id: 1 }
            });
            console.log('✅ 取消分享成功:', cancelResponse.data);
        } catch (error) {
            console.log('❌ 取消分享失败:', error.response?.data || error.message);
        }

        console.log('\n🎉 所有测试完成！');

    } catch (error) {
        console.error('测试过程中发生错误:', error.message);
    }
}

// 运行测试
testResourceShareAPI(); 