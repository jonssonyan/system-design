package com.jonssonyan.controller;

import com.jonssonyan.entity.TOrder;
import com.jonssonyan.service.TOrderService;
import com.jonssonyan.vo.Result;
import org.apache.shardingsphere.api.hint.HintManager;
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

    /**
     * 保存 主库
     *
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate() {
        for (int i = 0; i < 50; i++) {
            TOrder tOrder = new TOrder();
            tOrder.setUserId(1L);
            tOrderService.saveOrUpdate(tOrder);
        }
        return Result.success();
    }

    /**
     * 查询 从库
     *
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        List<TOrder> list = tOrderService.list();
        return Result.success(list);
    }

    /**
     * 强制路由主库 默认主库写，从库读
     *
     * @return
     */
    @GetMapping("/masterList")
    public Result masterList() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setMasterRouteOnly();
        List<TOrder> list = tOrderService.list();
        hintManager.close();
        return Result.success(list);
    }
}
