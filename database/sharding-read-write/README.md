# sharding read write

读写分离

# 数据库

read-write-master-ds

- t_order

read-write-slave-ds

- t_order

```sql
-- read-write-master-ds
create table t_order
(
    id      bigint           not null
        primary key,
    user_id bigint default 0 not null
);
-- read-write-slave-ds
create table t_order
(
    id      bigint           not null
        primary key,
    user_id bigint default 0 not null
);
```