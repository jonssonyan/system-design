# Sharding Sub

数据分片

# Sharding-JDBC

Sharding-JDBC是ShardingSphere的第一个产品，也是ShardingSphere的前身。
它定位为轻量级Java框架，在Java的JDBC层提供的额外服务。它使用客户端直连数据库，以jar包形式提供服务，无需额外部署和依赖，可理解为增强版的JDBC驱动，完全兼容JDBC和各种ORM框架。

# 数据库

sub-table-ds0

- t_order_0
- t_order_1

sub-table-ds1

- t_order_0
- t_order_1

```sql
-- sub-table-ds0
create table t_order_0
(
    id      bigint           not null
        primary key,
    user_id bigint default 0 not null
);
create table t_order_1
(
    id      bigint           not null
        primary key,
    user_id bigint default 0 not null
);
-- sub-table-ds1
create table t_order_0
(
    id      bigint           not null
        primary key,
    user_id bigint default 0 not null
);
create table t_order_1
(
    id      bigint           not null
        primary key,
    user_id bigint default 0 not null
);
```

# 参考文档

1. https://shardingsphere.apache.org/document/current/cn/overview/
2. https://shardingsphere.apache.org/document/legacy/4.x/document/cn/overview/
3. [自定义分库/分片策略](https://www.cnblogs.com/chengxy-nds/p/13919981.html)