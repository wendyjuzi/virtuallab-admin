const axios = require('axios');

// 配置
const BASE_URL = 'http://localhost:8080';
const ADMIN_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqeiIsImlhdCI6MTc1MTcxNDQxMiwiZXhwIjoxNzUyMzE5MjEyfQ.bdTItQQNLSh8ac7vDzSunRm9Z7351W9IdCJbyu5kSOg';

// 请求配置
const config = {
    headers: {
        'Authorization': `Bearer ${ADMIN_TOKEN}`,
        'Content-Type': 'application/json'
    }
};

async function testAdminAPIs() {
    console.log('开始测试修改后的管理员点赞收藏接口...\n');

    try {
        // 1. 测试获取统计数据
        console.log('1. 测试获取统计数据...');
        const statsResponse = await axios.get(`${BASE_URL}/admin/like-favorite/stats`, config);
        console.log('统计数据响应:', JSON.stringify(statsResponse.data, null, 2));
        console.log('');

        // 2. 测试获取点赞记录
        console.log('2. 测试获取点赞记录...');
        const likesResponse = await axios.get(`${BASE_URL}/admin/like-favorite/likes?page=1&size=10`, config);
        console.log('点赞记录响应:', JSON.stringify(likesResponse.data, null, 2));
        console.log('');

        // 3. 测试获取收藏记录
        console.log('3. 测试获取收藏记录...');
        const favoritesResponse = await axios.get(`${BASE_URL}/admin/like-favorite/favorites?page=1&size=10`, config);
        console.log('收藏记录响应:', JSON.stringify(favoritesResponse.data, null, 2));
        console.log('');

        // 4. 测试获取资源统计
        console.log('4. 测试获取资源统计...');
        const resourceStatsResponse = await axios.get(`${BASE_URL}/admin/like-favorite/resource-stats`, config);
        console.log('资源统计响应:', JSON.stringify(resourceStatsResponse.data, null, 2));
        console.log('');

        // 5. 测试系统概览
        console.log('5. 测试系统概览...');
        const overviewResponse = await axios.get(`${BASE_URL}/admin/like-favorite/overview`, config);
        console.log('系统概览响应:', JSON.stringify(overviewResponse.data, null, 2));
        console.log('');

        console.log('✅ 所有管理员接口测试完成！');

    } catch (error) {
        console.error('❌ 测试失败:', error.response ? error.response.data : error.message);
        console.error('状态码:', error.response ? error.response.status : 'N/A');
        console.error('请求URL:', error.config ? error.config.url : 'N/A');
    }
}

// 测试普通用户接口
async function testUserAPIs() {
    console.log('\n开始测试普通用户点赞收藏接口...\n');

    try {
        // 1. 测试获取实验项目点赞收藏状态
        console.log('1. 测试获取实验项目点赞收藏状态...');
        const statusResponse = await axios.get(`${BASE_URL}/like-favorite/resource/1`, config);
        console.log('状态响应:', JSON.stringify(statusResponse.data, null, 2));
        console.log('');

        // 2. 测试点赞实验项目
        console.log('2. 测试点赞实验项目...');
        const likeResponse = await axios.post(`${BASE_URL}/like-favorite/like/1`, {}, config);
        console.log('点赞响应:', JSON.stringify(likeResponse.data, null, 2));
        console.log('');

        // 3. 测试收藏实验项目
        console.log('3. 测试收藏实验项目...');
        const favoriteResponse = await axios.post(`${BASE_URL}/like-favorite/favorite/1`, {}, config);
        console.log('收藏响应:', JSON.stringify(favoriteResponse.data, null, 2));
        console.log('');

        console.log('✅ 所有用户接口测试完成！');

    } catch (error) {
        console.error('❌ 用户接口测试失败:', error.response ? error.response.data : error.message);
        console.error('状态码:', error.response ? error.response.status : 'N/A');
        console.error('请求URL:', error.config ? error.config.url : 'N/A');
    }
}

// 运行测试
async function runTests() {
    await testAdminAPIs();
    await testUserAPIs();
}

runTests(); 