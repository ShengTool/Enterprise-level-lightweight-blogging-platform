package com.example.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BackupService {
    @Autowired
    private DataSource dataSource;

    public String backupDatabase() throws Exception {
        // 创建备份文件路径
        String backupDir = "backup";
        File dir = new File(backupDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 生成备份文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = "backup-" + sdf.format(new Date()) + ".sql";
        String backupPath = backupDir + File.separator + fileName;

        // 获取数据库连接
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             FileWriter fw = new FileWriter(backupPath);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // 备份用户表
            backupTable(statement, bw, "user");

            // 备份标签表
            backupTable(statement, bw, "tag");

            // 备份文章表
            backupTable(statement, bw, "article");

            // 备份评论表
            backupTable(statement, bw, "comment");

            // 备份文章标签关联表
            backupTable(statement, bw, "article_tags");
        }

        return backupPath;
    }

    private void backupTable(Statement statement, BufferedWriter bw, String tableName) throws Exception {
        // 获取表结构
        ResultSet rs = statement.executeQuery("SHOW CREATE TABLE " + tableName);
        if (rs.next()) {
            bw.write(rs.getString(2) + ";\n\n");
        }
        rs.close();

        // 获取表数据
        rs = statement.executeQuery("SELECT * FROM " + tableName);
        int columnCount = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            bw.write("INSERT INTO " + tableName + " VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                Object value = rs.getObject(i);
                if (value == null) {
                    bw.write("NULL");
                } else if (value instanceof String) {
                    bw.write("'" + value.toString().replace("'", "''") + "'");
                } else {
                    bw.write(value.toString());
                }
                if (i < columnCount) {
                    bw.write(", ");
                }
            }
            bw.write(");\n");
        }
        rs.close();
        bw.write("\n");
    }

    public void restoreDatabase(String backupPath) throws Exception {
        // 获取数据库连接
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             FileReader fr = new FileReader(backupPath);
             BufferedReader br = new BufferedReader(fr)) {

            // 禁用外键约束
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            // 读取备份文件并执行SQL语句
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                if (line.endsWith(";")) {
                    statement.execute(sb.toString());
                    sb.setLength(0);
                }
            }

            // 启用外键约束
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }
    
    public int clearCache() throws IOException {
        int count = 0;
        
        // 清理上传目录的临时文件
        File uploadDir = new File("uploads");
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            File[] files = uploadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 删除7天前的临时文件
                    long sevenDaysAgo = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);
                    if (file.lastModified() < sevenDaysAgo) {
                        if (file.delete()) {
                            count++;
                        }
                    }
                }
            }
        }
        
        // 清理备份目录的临时文件
        File backupDir = new File("backup");
        if (backupDir.exists() && backupDir.isDirectory()) {
            File[] files = backupDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 删除30天前的备份文件
                    long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
                    if (file.lastModified() < thirtyDaysAgo) {
                        if (file.delete()) {
                            count++;
                        }
                    }
                }
            }
        }
        
        return count;
    }
}