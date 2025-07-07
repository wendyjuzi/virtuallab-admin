package com.edu.virtuallab.admin.service;

import java.io.IOException;

public interface DatabaseBackupService {
    /**
     * 备份数据库并生成zip包，返回zip文件绝对路径
     */
    String backupAndZip() throws IOException;
} 