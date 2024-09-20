package com.yindan.bookstore.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    // 数据库URL、用户名和密码
    private static final String URL = "jdbc:mysql://localhost:3306/electronicBookStore?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "123456root";

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // 加载并注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立连接
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("数据库连接成功！");

        } catch (ClassNotFoundException e) {
            System.err.println("找不到JDBC驱动程序。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库连接失败！");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
