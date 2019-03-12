package com.bfchengnuo.sell.service;

import com.bfchengnuo.sell.po.SellerInfo;

/**
 * 卖家端-用户信息
 *
 * Created by 冰封承諾Andy on 2019/3/12.
 */
public interface SellerInfoService {
    SellerInfo findSellerInfoByOpenId(String openId);
}
