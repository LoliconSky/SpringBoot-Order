package com.bfchengnuo.sell.controller;

import com.bfchengnuo.sell.converter.OrderForm2OrderDTOConverter;
import com.bfchengnuo.sell.dto.OrderDTO;
import com.bfchengnuo.sell.enums.ResultEnum;
import com.bfchengnuo.sell.exception.SellException;
import com.bfchengnuo.sell.form.OrderForm;
import com.bfchengnuo.sell.service.OrderService;
import com.bfchengnuo.sell.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 买家订单接口
 * Created by 冰封承諾Andy on 2018/7/19.
 */
@RestController
@RequestMapping("buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param order
     * @param bindingResult
     * @return
     */
    @PostMapping("create")
    public ResultVo<Map<String, String>> create(@Valid OrderForm order,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】 参数不正确，order={}", order);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(order);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】 购物车为空，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        orderDTO = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        return ResultVo.createBySuccess(map);
    }

    /**
     * 订单列表
     * @param openid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("list")
    public ResultVo<List<OrderDTO>> list(String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【订单列表】 openid 为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDTO> result = orderService.findList(openid, pageRequest);
        return ResultVo.createBySuccess(result.getContent());
    }

    /**
     * 查询订单详情
     * @param openid
     * @param orderId
     * @return
     */
    @GetMapping("detail")
    public ResultVo<OrderDTO> detail(String openid, String orderId) {
        OrderDTO orderDTO = orderService.checkOrderOwner(openid, orderId);
        return ResultVo.createBySuccess(orderDTO);
    }

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    @PostMapping("cancel")
    public ResultVo cancel(String openid, String orderId) {
        OrderDTO orderDTO = orderService.checkOrderOwner(openid, orderId);
        if (StringUtils.isEmpty(orderDTO.getOrderId())) {
            log.error("【取消订单】 订单不存在，订单号：{}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        orderService.cancel(orderDTO);
        return ResultVo.createBySuccess();
    }
}
