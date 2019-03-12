package com.bfchengnuo.sell.service;

import com.bfchengnuo.sell.dto.CartDTO;
import com.bfchengnuo.sell.po.ProductInfo;
import com.bfchengnuo.sell.vo.ProductVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
public interface ProductService {
    ProductInfo findOne(String productId);

    /**
     * 查询所有上架的商品
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    /**
     * 加库存
     * @param cartDTOList
     */
    void increaseStock(List<CartDTO> cartDTOList);

    /**
     * 减库存
     * @param cartDTOList
     */
    void decreaseStock(List<CartDTO> cartDTOList);

    List<ProductVo> getProductList();

    /**
     * 上架
     *
     * @param productId
     * @return
     */
    ProductInfo onSale(String productId);

    /**
     * 下架
     *
     * @param productId
     * @return
     */
    ProductInfo offSale(String productId);
}
