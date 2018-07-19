package com.bfchengnuo.sell.service;

import com.bfchengnuo.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by 冰封承諾Andy on 2018/7/18.
 */
public interface OrderService {
    OrderDTO create(OrderDTO orderDTO);

    OrderDTO findOne(String orderId);

    Page<OrderDTO> findList(String buyerOpenId, Pageable pageable);

    OrderDTO cancel(OrderDTO orderDTO);

    OrderDTO finish(OrderDTO orderDTO);

    OrderDTO paid(OrderDTO orderDTO);

    /**
     * 判断传入的订单号与当前用户的 openid 是否相符，避免横向越权
     * 如果不符会抛出异常，相符返回查到的 OrderDTO
     * @param openid 用户的标识 openid
     * @param orderId 要查询的订单号
     * @return 相符返回查到的 OrderDTO
     */
    OrderDTO checkOrderOwner(String openid, String orderId);
}
