package com.bfchengnuo.sell.service.impl;

import com.bfchengnuo.sell.dao.SellerInfoRepository;
import com.bfchengnuo.sell.po.SellerInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 冰封承諾Andy on 2019/3/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerInfoServiceImplTest {
    @Autowired
    private SellerInfoRepository repository;

    @Test
    public void save() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setOpenid("112233");
        sellerInfo.setPassword("1111");
        sellerInfo.setId("1111");
        sellerInfo.setUsername("喵帕斯");
        repository.save(sellerInfo);
    }

    @Test
    public void findSellerInfoByOpenId() {
        SellerInfo sellerInfo = repository.findByOpenid("112233");
        System.out.println(sellerInfo);
    }
}