package com.bfchengnuo.sell.dao;

import com.bfchengnuo.sell.po.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void testFindOne() {
        Optional<ProductCategory> optional = repository.findById(1);
        optional.ifPresent(productCategory -> System.out.println(productCategory.getCategoryName()));
    }

    @Test
    // 测试里的事务最后一定会回滚，可利用其保证数据的干净
    @Transactional
    public void testSave() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("热销榜");
        productCategory.setCategoryType(2);

        repository.save(productCategory);
    }

    @Test
    public void testFindByCategoryType() {
        List<ProductCategory> productCategoryList = repository.findByCategoryTypeIn(Arrays.asList(2, 3));
        System.out.println(productCategoryList.size());
        Assert.assertNotEquals(0, productCategoryList.size());
    }
}