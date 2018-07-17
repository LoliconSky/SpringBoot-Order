package com.bfchengnuo.sell.service.impl;

import com.bfchengnuo.sell.dao.ProductCategoryRepository;
import com.bfchengnuo.sell.po.ProductCategory;
import com.bfchengnuo.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        Optional<ProductCategory> optional = repository.findById(categoryId);
        return optional.orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> CategoryTypes) {
        return repository.findByCategoryTypeIn(CategoryTypes);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
