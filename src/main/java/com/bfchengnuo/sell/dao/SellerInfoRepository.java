package com.bfchengnuo.sell.dao;

import com.bfchengnuo.sell.po.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 冰封承諾Andy on 2019/3/12.
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openId);
}
