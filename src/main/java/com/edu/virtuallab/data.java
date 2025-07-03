package com.edu.virtuallab;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
public class data {



        private static final String URL = "jdbc:mysql://:3306/virtuallab?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "123456";

        // 批量插入大小
        private static final int BATCH_SIZE = 100;
        // 总记录数
        private static final int TOTAL_RECORDS = 500;

        public static void main(String[] args) {
            // 操作类型列表
            List<String> operations = Arrays.asList("新增", "修改", "删除", "查询", "导出", "导入", "登录", "登出", "授权", "审核");
            // 功能模块列表
            List<String> modules = Arrays.asList("用户管理", "权限管理", "日志管理", "系统设置", "数据统计", "文件管理", "角色管理", "部门管理");
            // 请求方法列表
            List<String> requestMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH");

            // 加载数据库驱动
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("找不到MySQL JDBC驱动");
                e.printStackTrace();
                return;
            }

            // 生成并插入数据
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                conn.setAutoCommit(false);

                String sql = "INSERT INTO operation_log (" +
                        "user_id, username, operation, module, description, request_method, " +
                        "request_url, request_params, response_result, ip_address, user_agent, " +
                        "execution_time, status, error_message, create_time, permission_code" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    Random random = new Random();
                    int batchCount = 0;

                    for (int i = 0; i < TOTAL_RECORDS; i++) {
                        // 生成随机用户ID
                        long userId = random.nextInt(100) + 1;
                        // 生成随机用户名
                        String username = generateRandomUsername();
                        // 随机选择操作类型
                        String operation = operations.get(random.nextInt(operations.size()));
                        // 随机选择功能模块
                        String module = modules.get(random.nextInt(modules.size()));
                        // 生成操作描述
                        String description = module + operation + "操作，用户" + username;
                        // 随机选择请求方法
                        String requestMethod = requestMethods.get(random.nextInt(requestMethods.size()));
                        // 生成请求URL
                        String requestUrl = "/api/" + module.toLowerCase().replace("管理", "") + "/" + operation.toLowerCase();
                        // 生成请求参数
                        String requestParams = generateRequestParams(random);
                        // 随机生成状态（90%成功，10%失败）
                        int status = random.nextInt(10) < 9 ? 1 : 0;
                        // 生成错误信息（仅失败时）
                        String errorMessage = status == 0 ? generateRandomErrorMessage(random) : null;
                        // 生成执行时间
                        long executionTime = random.nextInt(4990) + 10;
                        // 生成IP地址
                        String ipAddress = generateRandomIp(random);
                        // 生成用户代理
                        String userAgent = generateRandomUserAgent();
                        // 生成创建时间（最近30天内）
                        Timestamp createTime = generateRandomTimestamp(random);
                        // 生成权限代码
                        String permissionCode = module.substring(0, 2).toUpperCase() + "_" +
                                operation.substring(0, 1).toUpperCase() +
                                (random.nextInt(900) + 100);

                        // 生成响应结果
                        String responseResult = status == 1 ?
                                generateSuccessResponse(random) :
                                generateErrorResponse(errorMessage);

                        // 设置SQL参数
                        pstmt.setLong(1, userId);
                        pstmt.setString(2, username);
                        pstmt.setString(3, operation);
                        pstmt.setString(4, module);
                        pstmt.setString(5, description);
                        pstmt.setString(6, requestMethod);
                        pstmt.setString(7, requestUrl);
                        pstmt.setString(8, requestParams);
                        pstmt.setString(9, responseResult);
                        pstmt.setString(10, ipAddress);
                        pstmt.setString(11, userAgent);
                        pstmt.setLong(12, executionTime);
                        pstmt.setInt(13, status);
                        pstmt.setString(14, errorMessage);
                        pstmt.setTimestamp(15, createTime);
                        pstmt.setString(16, permissionCode);

                        pstmt.addBatch();
                        batchCount++;

                        // 批量执行
                        if (batchCount % BATCH_SIZE == 0) {
                            pstmt.executeBatch();
                            conn.commit();
                            System.out.println("已插入批次 " + (batchCount / BATCH_SIZE) + "/" + (TOTAL_RECORDS / BATCH_SIZE));
                        }
                    }

                    // 执行剩余批次
                    if (batchCount % BATCH_SIZE != 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        System.out.println("已插入批次 " + ((batchCount / BATCH_SIZE) + 1) + "/" + (TOTAL_RECORDS / BATCH_SIZE));
                    }
                }
            } catch (SQLException e) {
                System.err.println("数据库操作错误");
                e.printStackTrace();
            }

            System.out.println("已成功生成并插入 " + TOTAL_RECORDS + " 条测试数据");
        }

        // 生成随机用户名
        private static String generateRandomUsername() {
            String[] surnames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "陈", "杨"};
            String[] names = {"伟", "芳", "娜", "秀英", "敏", "静", "强", "磊", "军", "洋"};
            Random random = new Random();
            return surnames[random.nextInt(surnames.length)] + names[random.nextInt(names.length)];
        }

        // 生成随机请求参数
        private static String generateRequestParams(Random random) {
            return String.format("{\"id\":%d,\"name\":\"%s\",\"page\":%d,\"size\":20}",
                    random.nextInt(9000) + 1000,
                    generateRandomUsername(),
                    random.nextInt(10) + 1);
        }

        // 生成成功响应
        private static String generateSuccessResponse(Random random) {
            return String.format("{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":%d}}",
                    random.nextInt(9000) + 1000);
        }

        // 生成错误响应
        private static String generateErrorResponse(String errorMessage) {
            return String.format("{\"code\":500,\"message\":\"操作失败\",\"error\":\"%s\"}",
                    errorMessage.replace("\"", "\\\""));
        }

        // 生成随机错误信息
        private static String generateRandomErrorMessage(Random random) {
            String[] errorMessages = {
                    "系统繁忙，请稍后再试",
                    "数据库连接失败",
                    "权限不足",
                    "参数错误",
                    "数据不存在",
                    "数据已存在",
                    "网络异常",
                    "服务器内部错误",
                    "超时，请重试",
                    "文件上传失败"
            };
            return errorMessages[random.nextInt(errorMessages.length)];
        }

        // 生成随机IP地址
        private static String generateRandomIp(Random random) {
            return String.format("%d.%d.%d.%d",
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256));
        }

        // 生成随机用户代理
        private static String generateRandomUserAgent() {
            String[] userAgents = {
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36 Edg/91.0.864.71"
            };
            Random random = new Random();
            return userAgents[random.nextInt(userAgents.length)];
        }

        // 生成随机时间戳（最近30天内）
        private static Timestamp generateRandomTimestamp(Random random) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime thirtyDaysAgo = now.minusDays(30);

            long startEpoch = thirtyDaysAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endEpoch = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            long randomEpoch = startEpoch + (long) (random.nextDouble() * (endEpoch - startEpoch));
            return new Timestamp(randomEpoch);
        }

}
