package com.bfchengnuo.sell.service.impl;

import com.bfchengnuo.sell.converter.OrderMaster2OrderDTOConverter;
import com.bfchengnuo.sell.dao.OrderDetailRepository;
import com.bfchengnuo.sell.dao.OrderMasterRepository;
import com.bfchengnuo.sell.dao.ProductInfoRepository;
import com.bfchengnuo.sell.dto.CartDTO;
import com.bfchengnuo.sell.dto.OrderDTO;
import com.bfchengnuo.sell.enums.OrderStatusEnum;
import com.bfchengnuo.sell.enums.PayStatusEnum;
import com.bfchengnuo.sell.enums.ResultEnum;
import com.bfchengnuo.sell.exception.SellException;
import com.bfchengnuo.sell.po.OrderDetail;
import com.bfchengnuo.sell.po.OrderMaster;
import com.bfchengnuo.sell.po.ProductInfo;
import com.bfchengnuo.sell.service.OrderService;
import com.bfchengnuo.sell.service.ProductService;
import com.bfchengnuo.sell.service.WechatPayService;
import com.bfchengnuo.sell.utils.KeyUtil;
import com.bfchengnuo.sell.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 处理订单的业务逻辑，包含主订单信息和订单详情，一起处理
 * Created by 冰封承諾Andy on 2018/7/18.
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private WechatPayService payService;
    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        String orderId = KeyUtil.genUniqueKey();
        // 保存订单项的信息
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            // 查询数量、价格等信息; getOne 方法即使不存在也返回一个引用，而不是 null
            Optional<ProductInfo> optional = productInfoRepository.findById(orderDetail.getProductId());
            if (!optional.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = optional.get();

            // 计算订单的总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity().toString()))
                    .add(orderAmount);

            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);
        }
        // 保存主订单
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        // 属性拷贝如果是 null 也会进行拷贝
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        // 扣除库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(orderDetail -> new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity()))
                .collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        // 使用 websocket 推送消息
        try {
            webSocket.sendMessage("收到新的订单：".concat(orderId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderDTO;
    }

    /**
     * 查询单个订单信息，包含有商品的详细信息
     * @param orderId 订单 id
     * @return 前台所需要的 orderVo
     */
    @Override
    public OrderDTO findOne(String orderId) {
        Optional<OrderMaster> optional = orderMasterRepository.findById(orderId);
        if (!optional.isPresent()) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        OrderMaster orderMaster = optional.get();
        List<OrderDetail> detailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(detailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(detailList);
        return orderDTO;
    }

    /**
     * 根据微信的 openId 查询订单列表（分页）
     * 注：不包含商品的详细信息
     * @param buyerOpenId 用户 id
     * @param pageable 分页信息
     * @return 前台需要的 list
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);
        List<OrderDTO> list = OrderMaster2OrderDTOConverter.convert(page.getContent());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findAll(pageable);
        List<OrderDTO> list = OrderMaster2OrderDTOConverter.convert(page.getContent());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        // 判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】 订单状态不正确，orderId={}， status={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 修改订单状态
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        try {
            orderMasterRepository.save(orderMaster);
        } catch (Exception e) {
            log.error("【取消订单】 更新订单失败，orderId={}， status={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        // 返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】 订单中无商品信息，orderId={}， status={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> list = orderDTO.getOrderDetailList().stream()
                .map(orderDetail -> new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(list);
        // 如果已支付，则退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            // 退款逻辑, 大概还需要校验是否退款成功
            payService.refund(orderDTO);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        // 判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】 订单状态不正确，orderId={}， status={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        try {
            orderMasterRepository.save(orderMaster);
        } catch (Exception e) {
            log.error("【完结订单】 更新订单失败，orderId={}， status={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        // 判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【支付订单】 订单状态不正确，orderId={}， status={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【支付订单】 支付状态不正确，orderId={}， payStatus={}", orderDTO.getOrderId(), orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        // 修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        try {
            orderMasterRepository.save(orderMaster);
        } catch (Exception e) {
            log.error("【支付订单】 更新订单失败，orderId={}， payStatus={}", orderDTO.getOrderId(), orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    /**
     * 判断传入的订单号与当前用户的 openid 是否相符，避免横向越权
     * 如果不符会抛出异常，相符返回查到的 OrderDTO
     * findOne 改用两个条件大概也可以
     *
     * @param openid 用户的标识 openid
     * @param orderId 要查询的订单号
     * @return 相符返回查到的 OrderDTO
     */
    @Override
    public OrderDTO checkOrderOwner(String openid, String orderId) {
        OrderDTO orderDTO = this.findOne(orderId);
        if (!orderDTO.getBuyerOpenid().equals(openid)) {
            log.error("【查询订单详情】 订单 openid 不相符，订单openid={}, 传入的openid={}", orderDTO.getBuyerOpenid(), openid);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
