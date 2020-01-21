package com.wuhanfanlin.learn.shardingjdbc.demo1;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 加入说800行记录分一个表
 */
public class PreciseRemainderShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> preciseShardingValue) {
        long remainder = preciseShardingValue.getValue() / 800;
        return preciseShardingValue.getLogicTableName() + "_" + remainder;
    }
}
