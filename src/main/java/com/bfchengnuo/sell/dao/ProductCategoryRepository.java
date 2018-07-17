package com.bfchengnuo.sell.dao;

import com.bfchengnuo.sell.po.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> CategoryTypes);
}
