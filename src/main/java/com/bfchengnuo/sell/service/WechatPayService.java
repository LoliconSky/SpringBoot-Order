package com.bfchengnuo.sell.service;

import com.bfchengnuo.sell.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

/**
 * Created by 冰封承諾Andy on 2019/3/11.
 */
public interface WechatPayService {
    /**
     * 创建支付请求
     *
     * @param orderDTO
     * @return
     */
    PayResponse create(OrderDTO orderDTO);

    /**
     * 确认支付
     *
     * @param notifyData
     * @return
     */
    PayResponse notify(String notifyData);

    /**
     * 发起退款
     *
     * @param orderDTO
     * @return
     */
    RefundResponse refund(OrderDTO orderDTO);
}
