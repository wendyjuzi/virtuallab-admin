const axios = require('axios');

// 配置
const BASE_URL = 'http://localhost:8080';
const ADMIN_TOKEN = 'your_admin_token_here'; // 请替换为实际的管理员token

// 请求头
const headers = {
    'Authorization': `Bearer ${ADMIN_TOKEN}`,
    'Content-Type': 'application/json'
};

// 测试函数
async function testAdminLikeFavoriteAPIs() {
    console.log('=== 管理员点赞收藏接口测试 ===\n');

    try {
        // 1. 获取所有用户点赞收藏统计
        console.log('1. 测试获取所有用户点赞收藏统计');
        const statsResponse = await axios.get(`${BASE_URL}/admin/like-favorite/stats`, { headers });
        console.log('响应:', JSON.stringify(statsResponse.data, null, 2));
        console.log('');

        // 2. 获取所有点赞记录（分页）
        console.log('2. 测试获取所有点赞记录（分页）');
        const likesResponse = await axios.get(`${BASE_URL}/admin/like-favorite/likes?page=1&size=10`, { headers });
        console.log('响应:', JSON.stringify(likesResponse.data, null, 2));
        console.log('');

        // 3. 获取所有收藏记录（分页）
        console.log('3. 测试获取所有收藏记录（分页）');
        const favoritesResponse = await axios.get(`${BASE_URL}/admin/like-favorite/favorites?page=1&size=10`, { headers });
        console.log('响应:', JSON.stringify(favoritesResponse.data, null, 2));
        console.log('');

        // 4. 获取资源统计
        console.log('4. 测试获取资源统计');
        const resourceStatsResponse = await axios.get(`${BASE_URL}/admin/like-favorite/resource-stats`, { headers });
        console.log('响应:', JSON.stringify(resourceStatsResponse.data, null, 2));
        console.log('');

        // 5. 获取指定资源统计
        console.log('5. 测试获取指定资源统计');
        const resourceDetailResponse = await axios.get(`${BASE_URL}/admin/like-favorite/resource/1/stats`, { headers });
        console.log('响应:', JSON.stringify(resourceDetailResponse.data, null, 2));
        console.log('');

        // 6. 获取指定用户统计
        console.log('6. 测试获取指定用户统计');
        const userDetailResponse = await axios.get(`${BASE_URL}/admin/like-favorite/user/1/stats`, { headers });
        console.log('响应:', JSON.stringify(userDetailResponse.data, null, 2));
        console.log('');

        // 7. 系统概览
        console.log('7. 测试系统概览');
        const overviewResponse = await axios.get(`${BASE_URL}/admin/like-favorite/overview`, { headers });
        console.log('响应:', JSON.stringify(overviewResponse.data, null, 2));
        console.log('');

        // 8. 测试删除点赞记录（需要实际的likeId）
        console.log('8. 测试删除点赞记录');
        try {
            const deleteLikeResponse = await axios.delete(`${BASE_URL}/admin/like-favorite/like/1`, { headers });
            console.log('响应:', JSON.stringify(deleteLikeResponse.data, null, 2));
        } catch (error) {
            console.log('删除点赞记录失败（可能记录不存在）:', error.response?.data || error.message);
        }
        console.log('');

        // 9. 测试删除收藏记录（需要实际的favoriteId）
        console.log('9. 测试删除收藏记录');
        try {
            const deleteFavoriteResponse = await axios.delete(`${BASE_URL}/admin/like-favorite/favorite/1`, { headers });
            console.log('响应:', JSON.stringify(deleteFavoriteResponse.data, null, 2));
        } catch (error) {
            console.log('删除收藏记录失败（可能记录不存在）:', error.response?.data || error.message);
        }
        console.log('');

        // 10. 测试批量删除点赞记录
        console.log('10. 测试批量删除点赞记录');
        try {
            const batchDeleteLikesResponse = await axios.delete(`${BASE_URL}/admin/like-favorite/likes/batch`, {
                headers,
                data: {
                    ids: [1, 2, 3],
                    reason: '测试批量删除'
                }
            });
            console.log('响应:', JSON.stringify(batchDeleteLikesResponse.data, null, 2));
        } catch (error) {
            console.log('批量删除点赞记录失败:', error.response?.data || error.message);
        }
        console.log('');

        // 11. 测试批量删除收藏记录
        console.log('11. 测试批量删除收藏记录');
        try {
            const batchDeleteFavoritesResponse = await axios.delete(`${BASE_URL}/admin/like-favorite/favorites/batch`, {
                headers,
                data: {
                    ids: [1, 2, 3],
                    reason: '测试批量删除'
                }
            });
            console.log('响应:', JSON.stringify(batchDeleteFavoritesResponse.data, null, 2));
        } catch (error) {
            console.log('批量删除收藏记录失败:', error.response?.data || error.message);
        }
        console.log('');

        // 12. 测试重置用户统计
        console.log('12. 测试重置用户统计');
        try {
            const resetStatsResponse = await axios.post(`${BASE_URL}/admin/like-favorite/user/1/reset-stats`, {}, { headers });
            console.log('响应:', JSON.stringify(resetStatsResponse.data, null, 2));
        } catch (error) {
            console.log('重置用户统计失败:', error.response?.data || error.message);
        }
        console.log('');

        // 13. 测试导出数据
        console.log('13. 测试导出数据');
        try {
            const exportResponse = await axios.get(`${BASE_URL}/admin/like-favorite/export?format=csv`, { 
                headers,
                responseType: 'arraybuffer'
            });
            console.log('导出成功，数据大小:', exportResponse.data.length, 'bytes');
        } catch (error) {
            console.log('导出数据失败:', error.response?.data || error.message);
        }
        console.log('');

        console.log('=== 所有测试完成 ===');

    } catch (error) {
        console.error('测试过程中发生错误:', error.response?.data || error.message);
    }
}

// 运行测试
testAdminLikeFavoriteAPIs(); 