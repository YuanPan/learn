package com.wuhanfanlin.learn.shardingjdbc.demo1;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public final class PreciseModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {

    public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Long> preciseShardingValue) {
        String mod = preciseShardingValue.getValue() % 2 + "";
        for (String tableName : tableNames) {
            if (tableName.endsWith(mod)) {
                return tableName;
            }
        }

        throw new UnsupportedOperationException();
    }
}
