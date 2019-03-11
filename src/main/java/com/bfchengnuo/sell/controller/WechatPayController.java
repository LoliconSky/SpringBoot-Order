package com.bfchengnuo.sell.controller;

import com.bfchengnuo.sell.dto.OrderDTO;
import com.bfchengnuo.sell.enums.ResultEnum;
import com.bfchengnuo.sell.exception.SellException;
import com.bfchengnuo.sell.service.OrderService;
import com.bfchengnuo.sell.service.WechatPayService;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/pay")
public class WechatPayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WechatPayService payService;

    /**
     * 发起支付
     * @param orderId
     * @param returnUrl 支付后跳转到的页面
     * @param map
     * @return
     */
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        //1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2. 发起支付，返回微信生产的预付款信息
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);
        // 使用 js 唤醒微信的支付
        return new ModelAndView("pay/create", map);
    }

    /**
     * 微信异步通知，由微信回调
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }
}