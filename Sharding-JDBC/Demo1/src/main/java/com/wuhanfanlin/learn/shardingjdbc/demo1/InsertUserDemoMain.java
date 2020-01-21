package com.wuhanfanlin.learn.shardingjdbc.demo1;

import com.p6spy.engine.spy.P6DataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class InsertUserDemoMain {

    private static final Logger LOG = LoggerFactory.getLogger(InsertUserDemoMain.class);

    public static void main(String[] args) {

        try {
            DataSource dataSource = getShardingDataSource();
            Connection connection = dataSource.getConnection();

//            for (int i = 0; i < 1000; i++) {
//
//                String name = "user_" + (i + 1);
//                int gender = i % 2 == 0 ? 0 : 1;
//
//                PreparedStatement preparedStatement = connection.prepareStatement("insert into t_user(`id`, `name`, `gender`) values(?, ?, ?)");
//                preparedStatement.setLong(1, i + 1L);
//                preparedStatement.setString(2, name);
//                preparedStatement.setInt(3, gender);
//
//                preparedStatement.execute();
//            }

            PreparedStatement queryStatement = connection.prepareStatement("select * from t_user where id = ?");
            long[] queryIds = new long[]{1, 600, 800, 900, 1000};
            for (long queryId : queryIds) {
                queryStatement.setLong(1, queryId);
                ResultSet resultSet1 = queryStatement.executeQuery();
                if (resultSet1.next()) {
                    long id = resultSet1.getLong("id");
                    String name = resultSet1.getString("name");
                    int gender = resultSet1.getInt("gender");
                    LOG.info(String.format("id = %d, name = %s, gender = %d", id, name, gender));
                }
            }

            connection.close();

            System.exit(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        TableRuleConfiguration userTableRule = new TableRuleConfiguration("t_user", "ds0.t_user_${0..1}");
        // userTableRule.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseModuloShardingTableAlgorithm()));
        userTableRule.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseRemainderShardingTableAlgorithm()));

        shardingRuleConfiguration.getTableRuleConfigs().add(userTableRule);

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfiguration, new Properties());
    }


    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
        dataSourceMap.put("ds0", DataSourceUtil.createDataSource("ds0"));
        return dataSourceMap;
    }
}
