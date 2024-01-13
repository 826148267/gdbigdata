package edu.jnu.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月17日 15时58分
 * @功能描述: TODO
 */
public class MyTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> preciseShardingValue) {
        /**
         * tableNames 对应分片库中所有分片表的集合
         * shardingValue 为分片属性，其中 logicTableName 为逻辑表，columnName 分片健（字段），value 为从 SQL 中解析出的分片健的值
         */
        for (String tableName : tableNames) {
            long timestamp = preciseShardingValue.getValue() >> 22;
            /**
             * 取模算法，分片健 % 表数量
             */
            String value = String.valueOf(timestamp % tableNames.size());
            if (tableName.endsWith(value)) {
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}
