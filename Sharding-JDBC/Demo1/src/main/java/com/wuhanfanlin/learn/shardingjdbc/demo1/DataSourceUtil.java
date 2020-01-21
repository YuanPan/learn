package com.wuhanfanlin.learn.shardingjdbc.demo1;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class DataSourceUtil {
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1234";

    public static DataSource createDataSource(final String databaseName) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        dataSource.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?serverTimezone=%s&useUnicode=true&characterEncoding=UTF-8", HOST, PORT, databaseName, "GMT%2B8"));
        dataSource.setUsername(USER_NAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
}
