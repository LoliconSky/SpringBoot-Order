package com.bfchengnuo.sell.dao;

import com.bfchengnuo.sell.po.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 主订单信息
 * Created by 冰封承諾Andy on 2018/7/17.
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    /**
     * 按照买家的 openId 来查询订单
     * @param openId 买家微信的 openId
     * @param pageable 分页条件
     * @return 分页数据
     */
    Page<OrderMaster> findByBuyerOpenid(String openId,Pageable pageable);
}
