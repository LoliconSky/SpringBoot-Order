package com.bfchengnuo.sell.service.impl;

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

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void findOne() {
        ProductCategory category = categoryService.findOne(1);
        Assert.assertEquals(new Integer(1), category.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> categoryList = categoryService.findAll();
        Assert.assertNotEquals(0, categoryList.size());
    }

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(Arrays.asList(2, 3));
        Assert.assertNotEquals(0, categoryList.size());
    }

    @Test
    @Transactional
    public void save() {
        ProductCategory category = new ProductCategory();
        category.setCategoryName("测试类");
        category.setCategoryType(10);
        ProductCategory save = categoryService.save(category);
        Assert.assertNotNull(save);
    }
}