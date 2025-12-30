// 收藏功能测试脚本
const axios = require('axios');

const BASE_URL = 'http://localhost:8080'; // 根据你的实际端口调整

// 测试数据
const testUserId = 1;
const testResourceId = 1;

async function testFavoriteFunctionality() {
    console.log('开始测试收藏功能...\n');

    try {
        // 1. 测试添加收藏
        console.log('1. 测试添加收藏');
        const addFavoriteResponse = await axios.post(`${BASE_URL}/resource-favorite/add`, {
            userId: testUserId,
            resourceId: testResourceId
        });
        console.log('添加收藏结果:', addFavoriteResponse.data);
        console.log('');

        // 2. 测试查询收藏状态
        console.log('2. 测试查询收藏状态');
        const isFavoritedResponse = await axios.get(`${BASE_URL}/resource-favorite/is-favorited?userId=${testUserId}&resourceId=${testResourceId}`);
        console.log('是否已收藏:', isFavoritedResponse.data);
        console.log('');

        // 3. 测试获取资源收藏数量
        console.log('3. 测试获取资源收藏数量');
        const favoriteCountResponse = await axios.get(`${BASE_URL}/resource-favorite/resource/${testResourceId}/favorite-count`);
        console.log('资源收藏数量:', favoriteCountResponse.data);
        console.log('');

        // 4. 测试查询用户收藏列表
        console.log('4. 测试查询用户收藏列表');
        const userFavoritesResponse = await axios.get(`${BASE_URL}/resource-favorite/list/${testUserId}?offset=0&size=10`);
        console.log('用户收藏列表:', userFavoritesResponse.data);
        console.log('');

        // 5. 测试统计用户收藏数量
        console.log('5. 测试统计用户收藏数量');
        const userFavoriteCountResponse = await axios.get(`${BASE_URL}/resource-favorite/count/${testUserId}`);
        console.log('用户收藏数量:', userFavoriteCountResponse.data);
        console.log('');

        // 6. 测试查询用户资源收藏
        console.log('6. 测试查询用户资源收藏');
        const userResourceFavoriteResponse = await axios.get(`${BASE_URL}/resource-favorite/getByUserAndResource?userId=${testUserId}&resourceId=${testResourceId}`);
        console.log('用户资源收藏:', userResourceFavoriteResponse.data);
        console.log('');

        // 7. 测试移除收藏
        console.log('7. 测试移除收藏');
        if (userResourceFavoriteResponse.data.data && userResourceFavoriteResponse.data.data.id) {
            const removeFavoriteResponse = await axios.delete(`${BASE_URL}/resource-favorite/remove/${userResourceFavoriteResponse.data.data.id}`);
            console.log('移除收藏结果:', removeFavoriteResponse.data);
            console.log('');
        }

        // 8. 最终查询收藏状态
        console.log('8. 最终查询收藏状态');
        const finalFavoritedResponse = await axios.get(`${BASE_URL}/resource-favorite/is-favorited?userId=${testUserId}&resourceId=${testResourceId}`);
        console.log('最终收藏状态:', finalFavoritedResponse.data);
        console.log('');

        console.log('收藏功能测试完成！');

    } catch (error) {
        console.error('测试失败:', error.response ? error.response.data : error.message);
    }
}

// 运行测试
testFavoriteFunctionality(); 