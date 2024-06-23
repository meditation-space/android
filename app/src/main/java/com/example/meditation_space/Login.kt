package com.example.meditation_space

import java.sql.Connection
import java.sql.DriverManager

/**
 * 此模块是通过JDBC直接连接数据库（已无用）
 * 主要原因一开始没和后端沟通好，没有设计好对应的API文档，且后端由于实习原因，进度较慢，为了方便测试，我就先用JDBC测试数据库数据了。
 * */
class Login {
    fun getConnection(): Connection? {
        Class.forName("com.mysql.jdbc.Driver")

        // 连接 MySQL 数据库
        val conn =
            DriverManager.getConnection(
                "jdbc:mysql://10.61.19.94:3306/android",
                "root",
                "123456"
            )
        return conn
    }
}