package com.bfchengnuo.sell.controller;

import com.bfchengnuo.sell.service.CategoryService;
import com.bfchengnuo.sell.service.ProductService;
import com.bfchengnuo.sell.vo.ProductVo;
import com.bfchengnuo.sell.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 买家商品接口
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@RestController
@RequestMapping("buyer/product")
public class BuyerProductController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("list")
    public ResultVo list() {
        List<ProductVo> productVoList = productService.getProductList();
        return ResultVo.createBySuccess(productVoList);
    }
}
