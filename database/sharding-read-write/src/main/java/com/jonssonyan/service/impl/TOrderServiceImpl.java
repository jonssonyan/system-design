package com.jonssonyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonssonyan.dao.TOrderDao;
import com.jonssonyan.entity.TOrder;
import com.jonssonyan.service.TOrderService;
import org.springframework.stereotype.Service;

@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderDao, TOrder> implements TOrderService {
}
