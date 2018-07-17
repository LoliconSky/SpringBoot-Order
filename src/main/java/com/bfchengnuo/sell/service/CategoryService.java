package com.bfchengnuo.sell.service;

import com.bfchengnuo.sell.po.ProductCategory;

import java.util.List;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> CategoryTypes);

    ProductCategory save(ProductCategory productCategory);
}
