package com.jonssonyan.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 自定义标准分表策略
 */
public class MyShardingAlgorithmById implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        for (String name : collection) {
            /**
             * 取模算法，分片健 % 表数量
             */
            String value = String.valueOf(preciseShardingValue.getValue() % collection.size());
            if (name.endsWith(value)) {
                return name;
            }
        }
        throw new IllegalArgumentException();
    }
}
