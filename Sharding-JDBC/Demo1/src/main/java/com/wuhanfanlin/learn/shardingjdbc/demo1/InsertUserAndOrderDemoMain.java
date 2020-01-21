package com.wuhanfanlin.learn.shardingjdbc.demo1;

import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class InsertUserAndOrderDemoMain {
    public static void main(String[] args) {

        try {
            DataSource dataSource = getShardingDataSource();
            Connection connection = dataSource.getConnection();
            for (int i = 0; i < 1000; i++) {

                String name = "user_" + (i + 1);
                int gender = i % 2 == 0 ? 0 : 1;

                PreparedStatement preparedStatement = connection.prepareStatement("insert into t_user(`id`, `name`, `gender`) values(?, ?, ?)");
                preparedStatement.setLong(1, i + 1L);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, gender);

                preparedStatement.execute();
            }


            for (int i = 0; i < 1000; i++) {

                String orderNO = "order_" + (i + 1);

                PreparedStatement preparedStatement = connection.prepareStatement("insert into t_order(`id`, `user_id`, `order_no`) values(?, ?, ?)");
                preparedStatement.setLong(1, i + 1L);
                preparedStatement.setLong(2, i + 1L);
                preparedStatement.setString(3, orderNO);

                preparedStatement.execute();
            }


            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        TableRuleConfiguration userTableRule = new TableRuleConfiguration("t_user", "ds0.t_user_${0..1}");
        TableRuleConfiguration orderTableRule = new TableRuleConfiguration("t_order", "ds0.t_order_${0..1}");

        userTableRule.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseRemainderShardingTableAlgorithm()));
        orderTableRule.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseRemainderShardingTableAlgorithm()));

        shardingRuleConfiguration.getTableRuleConfigs().add(userTableRule);
        shardingRuleConfiguration.getTableRuleConfigs().add(orderTableRule);

        shardingRuleConfiguration.getBindingTableGroups().add("t_user, t_order");

        // shardingRuleConfiguration.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseModuloShardingTableAlgorithm()));

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfiguration, new Properties());
    }


    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
        dataSourceMap.put("ds0", DataSourceUtil.createDataSource("ds0"));
        return dataSourceMap;
    }
}
