// 资源分享功能测试脚本
const axios = require('axios');

const BASE_URL = 'http://localhost:8080'; // 根据你的实际端口调整

// 测试数据
const testResourceId = 1;
const testTargetUsername = 'student1';
const testPermission = 'read';

async function testResourceShareFunctionality() {
    console.log('开始测试资源分享功能...\n');

    try {
        // 1. 测试通过用户名分享资源
        console.log('1. 测试通过用户名分享资源');
        const shareResponse = await axios.post(`${BASE_URL}/resource-share/share-by-username`, null, {
            params: {
                resourceId: testResourceId,
                targetUsername: testTargetUsername,
                permission: testPermission
            }
        });
        console.log('分享结果:', shareResponse.data);
        console.log('');

        // 2. 测试查询某资源的所有分享
        console.log('2. 测试查询某资源的所有分享');
        const sharesResponse = await axios.get(`${BASE_URL}/resource-share/list?resourceId=${testResourceId}`);
        console.log('资源分享列表:', sharesResponse.data);
        console.log('');

        // 3. 测试获取用户分享给其他人的资源列表
        console.log('3. 测试获取用户分享给其他人的资源列表');
        const sharedByMeResponse = await axios.get(`${BASE_URL}/resource-share/shared-by-me`);
        console.log('我分享的资源列表:', sharedByMeResponse.data);
        console.log('');

        // 4. 测试获取分享给用户的资源列表
        console.log('4. 测试获取分享给用户的资源列表');
        const sharedWithMeResponse = await axios.get(`${BASE_URL}/resource-share/shared-with-me`);
        console.log('分享给我的资源列表:', sharedWithMeResponse.data);
        console.log('');

        // 5. 测试检查用户是否有资源的访问权限
        console.log('5. 测试检查用户是否有资源的访问权限');
        const checkAccessResponse = await axios.get(`${BASE_URL}/resource-share/check-access?resourceId=${testResourceId}&permission=${testPermission}`);
        console.log('访问权限检查结果:', checkAccessResponse.data);
        console.log('');

        // 6. 测试生成分享链接
        console.log('6. 测试生成分享链接');
        const generateLinkResponse = await axios.post(`${BASE_URL}/resource-share/generate`, null, {
            params: {
                resourceId: testResourceId,
                expireMinutes: 60
            }
        });
        console.log('生成分享链接结果:', generateLinkResponse.data);
        console.log('');

        // 7. 测试更新分享权限
        console.log('7. 测试更新分享权限');
        if (sharesResponse.data.data && sharesResponse.data.data.length > 0) {
            const shareId = sharesResponse.data.data[0].id;
            const updatePermissionResponse = await axios.put(`${BASE_URL}/resource-share/update-permission/${shareId}?permission=write`);
            console.log('更新权限结果:', updatePermissionResponse.data);
            console.log('');
        }

        // 8. 测试撤销分享
        console.log('8. 测试撤销分享');
        if (sharesResponse.data.data && sharesResponse.data.data.length > 0) {
            const shareId = sharesResponse.data.data[0].id;
            const revokeResponse = await axios.post(`${BASE_URL}/resource-share/revoke/${shareId}`);
            console.log('撤销分享结果:', revokeResponse.data);
            console.log('');
        }

        // 9. 测试取消分享
        console.log('9. 测试取消分享');
        if (sharesResponse.data.data && sharesResponse.data.data.length > 0) {
            const shareId = sharesResponse.data.data[0].id;
            const cancelResponse = await axios.delete(`${BASE_URL}/resource-share/cancel?id=${shareId}`);
            console.log('取消分享结果:', cancelResponse.data);
            console.log('');
        }

        console.log('资源分享功能测试完成！');

    } catch (error) {
        console.error('测试失败:', error.response ? error.response.data : error.message);
    }
}

// 运行测试
testResourceShareFunctionality(); 