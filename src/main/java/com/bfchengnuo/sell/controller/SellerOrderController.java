package com.bfchengnuo.sell.controller;

import com.bfchengnuo.sell.dto.OrderDTO;
import com.bfchengnuo.sell.enums.ResultEnum;
import com.bfchengnuo.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 卖家端-订单管理
 *
 * Created by 冰封承諾Andy on 2019/3/11.
 */
@Controller
@Slf4j
@RequestMapping("/seller/order")
public class SellerOrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 查询订单列表
     */
    @GetMapping("list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                     ModelAndView modelAndView) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<OrderDTO> pageOrder = orderService.findList(pageRequest);
        modelAndView.setViewName("order/list");
        modelAndView.addObject("pageOrder", pageOrder);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("size", size);
        return modelAndView;
    }

    /**
     * 取消订单
     * 安全情况下，这里应该是 post
     *
     * @param orderId
     * @return
     */
    @GetMapping("cancel")
    public String cancel(@RequestParam("orderId") String orderId, Map<String, Object> map) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);

        } catch (Exception e) {
            log.error("【卖家端取消订单】发生异常 {}", e);

            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return "common/error";
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return "common/success";
    }

    /**
     * 查询订单详情
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("detail")
    public String detail(@RequestParam("orderId") String orderId, Map<String, Object> map) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);

            map.put("orderDTO", orderDTO);
            return "order/detail";
        } catch (Exception e) {
            log.error("【卖家端查看订单详情】发生异常 {}", e);

            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return "common/error";
        }
    }

    /**
     * 完成订单
     * TODO 待优化：记录页数，当前页
     *
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("finish")
    public String finish(@RequestParam("orderId") String orderId, Map<String, Object> map) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        } catch (Exception e) {
            log.error("【卖家端完成订单】发生异常 {}", e);

            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return "common/error";
        }

        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return "common/success";
    }
}
