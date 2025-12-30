// 点赞功能测试脚本
const axios = require('axios');

const BASE_URL = 'http://localhost:8080'; // 根据你的实际端口调整

// 测试数据
const testUserId = 1;
const testResourceId = 1;

async function testLikeFunctionality() {
    console.log('开始测试点赞功能...\n');

    try {
        // 1. 测试查询点赞状态
        console.log('1. 测试查询点赞状态');
        const statusResponse = await axios.get(`${BASE_URL}/like-favorite/experiment/${testResourceId}?userId=${testUserId}`);
        console.log('点赞状态:', statusResponse.data);
        console.log('');

        // 2. 测试点赞
        console.log('2. 测试点赞');
        const likeResponse = await axios.post(`${BASE_URL}/like-favorite/like/${testResourceId}?userId=${testUserId}`);
        console.log('点赞结果:', likeResponse.data);
        console.log('');

        // 3. 再次查询点赞状态
        console.log('3. 再次查询点赞状态');
        const statusResponse2 = await axios.get(`${BASE_URL}/like-favorite/experiment/${testResourceId}?userId=${testUserId}`);
        console.log('点赞状态:', statusResponse2.data);
        console.log('');

        // 4. 测试查询是否已点赞
        console.log('4. 测试查询是否已点赞');
        const isLikedResponse = await axios.get(`${BASE_URL}/like-favorite/is-liked?userId=${testUserId}&resourceId=${testResourceId}`);
        console.log('是否已点赞:', isLikedResponse.data);
        console.log('');

        // 5. 测试获取资源点赞数量
        console.log('5. 测试获取资源点赞数量');
        const likeCountResponse = await axios.get(`${BASE_URL}/like-favorite/resource/${testResourceId}/like-count`);
        console.log('资源点赞数量:', likeCountResponse.data);
        console.log('');

        // 6. 测试取消点赞
        console.log('6. 测试取消点赞');
        const unlikeResponse = await axios.post(`${BASE_URL}/like-favorite/unlike/${testResourceId}?userId=${testUserId}`);
        console.log('取消点赞结果:', unlikeResponse.data);
        console.log('');

        // 7. 最终查询点赞状态
        console.log('7. 最终查询点赞状态');
        const finalStatusResponse = await axios.get(`${BASE_URL}/like-favorite/experiment/${testResourceId}?userId=${testUserId}`);
        console.log('最终点赞状态:', finalStatusResponse.data);
        console.log('');

        console.log('点赞功能测试完成！');

    } catch (error) {
        console.error('测试失败:', error.response ? error.response.data : error.message);
    }
}

// 运行测试
testLikeFunctionality(); 