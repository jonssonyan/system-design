package com.jonssonyan.controller;

import com.jonssonyan.entity.TOrder;
import com.jonssonyan.service.TOrderService;
import com.jonssonyan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tOrder")
public class TOrderController {
    @Autowired
    private TOrderService tOrderService;

    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate() {
        for (int i = 0; i < 50; i++) {
            TOrder tOrder1 = new TOrder();
            tOrder1.setUserId(1L);
            tOrderService.saveOrUpdate(tOrder1);
            TOrder tOrder2 = new TOrder();
            tOrder2.setUserId(2L);
            tOrderService.saveOrUpdate(tOrder2);
        }
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        List<TOrder> list = tOrderService.list();
        return Result.success(list);
    }
}
