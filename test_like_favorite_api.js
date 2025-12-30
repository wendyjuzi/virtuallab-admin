const axios = require('axios');

// 配置基础URL和认证信息
const BASE_URL = 'http://localhost:8080';
const TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqejExIiwiaWF0IjoxNzUxNzA0NzY4LCJleHAiOjE3NTIzMDk1Njh9.ei2ZWiy6FYlf8GvnxIqWEd3ph4TWcOFuL3XcrVlryPU';

// 测试用户ID和资源ID
const TEST_USER_ID = 1;
const TEST_RESOURCE_ID = 13;

// 配置axios默认设置
axios.defaults.baseURL = BASE_URL;
axios.defaults.headers.common['Authorization'] = `Bearer ${TOKEN}`;
axios.defaults.headers.common['Content-Type'] = 'application/json';

async function testLikeFavoriteAPI() {
    console.log('开始测试点赞收藏功能API...\n');

    try {
        // 1. 测试获取资源点赞数量
        console.log('1. 测试获取资源点赞数量');
        try {
            const likeCountResponse = await axios.get(`/like-favorite/resource/${TEST_RESOURCE_ID}/like-count`);
            console.log('✅ 获取点赞数量成功:', likeCountResponse.data);
        } catch (error) {
            console.log('❌ 获取点赞数量失败:', error.response?.data || error.message);
        }

        // 2. 测试获取资源收藏数量
        console.log('\n2. 测试获取资源收藏数量');
        try {
            const favoriteCountResponse = await axios.get(`/like-favorite/resource/${TEST_RESOURCE_ID}/favorite-count`);
            console.log('✅ 获取收藏数量成功:', favoriteCountResponse.data);
        } catch (error) {
            console.log('❌ 获取收藏数量失败:', error.response?.data || error.message);
        }

        // 3. 测试检查用户是否已点赞
        console.log('\n3. 测试检查用户是否已点赞');
        try {
            const isLikedResponse = await axios.get('/like-favorite/is-liked', {
                params: { userId: TEST_USER_ID, resourceId: TEST_RESOURCE_ID }
            });
            console.log('✅ 检查点赞状态成功:', isLikedResponse.data);
        } catch (error) {
            console.log('❌ 检查点赞状态失败:', error.response?.data || error.message);
        }

        // 4. 测试检查用户是否已收藏
        console.log('\n4. 测试检查用户是否已收藏');
        try {
            const isFavoritedResponse = await axios.get('/like-favorite/is-favorited', {
                params: { userId: TEST_USER_ID, resourceId: TEST_RESOURCE_ID }
            });
            console.log('✅ 检查收藏状态成功:', isFavoritedResponse.data);
        } catch (error) {
            console.log('❌ 检查收藏状态失败:', error.response?.data || error.message);
        }

        // 5. 测试获取点赞/收藏状态
        console.log('\n5. 测试获取点赞/收藏状态');
        try {
            const statusResponse = await axios.get(`/like-favorite/resource/${TEST_RESOURCE_ID}`, {
                params: { userId: TEST_USER_ID }
            });
            console.log('✅ 获取状态成功:', statusResponse.data);
        } catch (error) {
            console.log('❌ 获取状态失败:', error.response?.data || error.message);
        }

        // 6. 测试点赞功能
        console.log('\n6. 测试点赞功能');
        try {
            const likeResponse = await axios.post(`/like-favorite/like/${TEST_RESOURCE_ID}`, null, {
                params: { userId: TEST_USER_ID }
            });
            console.log('✅ 点赞成功:', likeResponse.data);
        } catch (error) {
            console.log('❌ 点赞失败:', error.response?.data || error.message);
        }

        // 7. 测试收藏功能
        console.log('\n7. 测试收藏功能');
        try {
            const favoriteResponse = await axios.post(`/like-favorite/favorite/${TEST_RESOURCE_ID}`, null, {
                params: { userId: TEST_USER_ID }
            });
            console.log('✅ 收藏成功:', favoriteResponse.data);
        } catch (error) {
            console.log('❌ 收藏失败:', error.response?.data || error.message);
        }

        // 8. 测试取消点赞功能
        console.log('\n8. 测试取消点赞功能');
        try {
            const unlikeResponse = await axios.post(`/like-favorite/unlike/${TEST_RESOURCE_ID}`, null, {
                params: { userId: TEST_USER_ID }
            });
            console.log('✅ 取消点赞成功:', unlikeResponse.data);
        } catch (error) {
            console.log('❌ 取消点赞失败:', error.response?.data || error.message);
        }

        // 9. 测试取消收藏功能
        console.log('\n9. 测试取消收藏功能');
        try {
            const unfavoriteResponse = await axios.post(`/like-favorite/unfavorite/${TEST_RESOURCE_ID}`, null, {
                params: { userId: TEST_USER_ID }
            });
            console.log('✅ 取消收藏成功:', unfavoriteResponse.data);
        } catch (error) {
            console.log('❌ 取消收藏失败:', error.response?.data || error.message);
        }

        // 10. 测试获取用户点赞列表
        console.log('\n10. 测试获取用户点赞列表');
        try {
            const userLikesResponse = await axios.get(`/like-favorite/user/${TEST_USER_ID}/likes`);
            console.log('✅ 获取用户点赞列表成功:', userLikesResponse.data);
        } catch (error) {
            console.log('❌ 获取用户点赞列表失败:', error.response?.data || error.message);
        }

        // 11. 测试获取用户点赞数量
        console.log('\n11. 测试获取用户点赞数量');
        try {
            const userLikeCountResponse = await axios.get(`/like-favorite/user/${TEST_USER_ID}/likes/count`);
            console.log('✅ 获取用户点赞数量成功:', userLikeCountResponse.data);
        } catch (error) {
            console.log('❌ 获取用户点赞数量失败:', error.response?.data || error.message);
        }

        // 12. 测试收藏服务的直接接口
        console.log('\n12. 测试收藏服务的直接接口');
        try {
            const addFavoriteResponse = await axios.post('/resource-favorite/add', {
                userId: TEST_USER_ID,
                resourceId: TEST_RESOURCE_ID
            });
            console.log('✅ 直接添加收藏成功:', addFavoriteResponse.data);
        } catch (error) {
            console.log('❌ 直接添加收藏失败:', error.response?.data || error.message);
        }

        console.log('\n🎉 所有测试完成！');

    } catch (error) {
        console.error('测试过程中发生错误:', error.message);
    }
}

// 运行测试
testLikeFavoriteAPI(); 