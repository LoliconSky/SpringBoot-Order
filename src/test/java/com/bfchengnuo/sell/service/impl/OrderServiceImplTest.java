package com.bfchengnuo.sell.service.impl;

import com.bfchengnuo.sell.dto.OrderDTO;
import com.bfchengnuo.sell.enums.OrderStatusEnum;
import com.bfchengnuo.sell.enums.PayStatusEnum;
import com.bfchengnuo.sell.po.OrderDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冰封承諾Andy on 2018/7/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "1101110";
    private final String ORDER_ID = "1531904255690412284";

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("喵帕斯");
        orderDTO.setBuyerAddress("乡下");
        orderDTO.setBuyerPhone("123456789012");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123456");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("789654");
        o2.setProductQuantity(2);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单】result={}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void createMore() {
        for (int i = 0; i < 10; i++) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setBuyerName("一条萤" + i);
            orderDTO.setBuyerAddress("乡下");
            orderDTO.setBuyerPhone("123456789012");
            orderDTO.setBuyerOpenid(BUYER_OPENID);

            //购物车
            List<OrderDetail> orderDetailList = new ArrayList<>();
            OrderDetail o1 = new OrderDetail();
            o1.setProductId("123456");
            o1.setProductQuantity(1);

            OrderDetail o2 = new OrderDetail();
            o2.setProductId("789654");
            o2.setProductQuantity(2);

            orderDetailList.add(o1);
            orderDetailList.add(o2);

            orderDTO.setOrderDetailList(orderDetailList);

            OrderDTO result = orderService.create(orderDTO);
            log.info("【创建订单】result={}", result);
        }
    }

    @Test
    public void findOne() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        System.out.println(orderDTO);
        Assert.assertNotNull(orderDTO);
    }

    @Test
    public void findList() {
        Page<OrderDTO> page = orderService.findList(BUYER_OPENID, PageRequest.of(0, 2));
        System.out.println(page.getTotalElements());
        Assert.assertNotEquals(0, page.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO dto = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), dto.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO dto = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), dto.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO dto = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), dto.getPayStatus());
    }
}