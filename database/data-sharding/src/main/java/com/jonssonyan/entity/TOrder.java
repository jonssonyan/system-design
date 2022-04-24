package com.jonssonyan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order")
public class TOrder {
    @TableId
    private Long id;
    private Long userId;
}
