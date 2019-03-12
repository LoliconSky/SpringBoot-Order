package com.bfchengnuo.sell.service.impl;

import com.bfchengnuo.sell.dao.SellerInfoRepository;
import com.bfchengnuo.sell.po.SellerInfo;
import com.bfchengnuo.sell.service.SellerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 卖家端-用户信息相关
 *
 * Created by 冰封承諾Andy on 2019/3/12.
 */
@Service
public class SellerInfoServiceImpl implements SellerInfoService {
    @Autowired
    private SellerInfoRepository  repository;

    @Override
    public SellerInfo findSellerInfoByOpenId(String openId) {
        return repository.findByOpenid(openId);
    }
}
