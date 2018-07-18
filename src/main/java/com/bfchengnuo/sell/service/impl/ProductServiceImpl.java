package com.bfchengnuo.sell.service.impl;

import com.bfchengnuo.sell.dao.ProductInfoRepository;
import com.bfchengnuo.sell.dto.CartDTO;
import com.bfchengnuo.sell.enums.ProductStatusEnum;
import com.bfchengnuo.sell.enums.ResultEnum;
import com.bfchengnuo.sell.exception.SellException;
import com.bfchengnuo.sell.po.ProductCategory;
import com.bfchengnuo.sell.po.ProductInfo;
import com.bfchengnuo.sell.service.CategoryService;
import com.bfchengnuo.sell.service.ProductService;
import com.bfchengnuo.sell.vo.ProductInfoVO;
import com.bfchengnuo.sell.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductInfoRepository repository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.getOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        cartDTOList.forEach(cartDTO -> {
            Optional<ProductInfo> optional = repository.findById(cartDTO.getProductId());
            if (!optional.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = optional.get();
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            repository.save(productInfo);
        });
    }

    /**
     * 根据购物车的商品和数量进行相应的减库存
     * TODO 考虑到并发问题，欲采用 Redis 锁机制来规避
     * @param cartDTOList 购物车（包含商品和数量的列表）
     */
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        cartDTOList.forEach(cartDTO -> {
            // repository.getOne(cartDTO.getProductId());
            Optional<ProductInfo> optional = repository.findById(cartDTO.getProductId());
            if (!optional.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = optional.get();
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
        });
    }

    @Override
    public List<ProductVo> getProductList() {
        // 获取上架商品的详细信息
        List<ProductInfo> productInfoList = this.findUpAll();
        List<Integer> categoryTypes = productInfoList.stream()
                .map(ProductInfo::getCategoryType).collect(Collectors.toList());
        // 获取商品的类目信息
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(categoryTypes);
        // 数据拼装
       return assembleProductVoList(productInfoList, categoryList);
    }

    /**
     * 根据商品详情和类目信息组装前台需要的 ProductVO
     * 嵌套层次为两层，类目为第一层，类目下包含有多个商品的详情；
     * 首先遍历类目，将遍历到的类目下的商品筛选出来组成第二层次
     *
     * @param productInfoList 商品详情数据 list
     * @param categoryList 对应的类目信息 list
     * @return 前台需要的 ProductVO
     */
    private List<ProductVo> assembleProductVoList(List<ProductInfo> productInfoList, List<ProductCategory> categoryList) {
        List<ProductVo> productVoList = new ArrayList<>();
        categoryList.forEach(productCategory -> {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            productInfoList.forEach(productInfo -> {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    // 避免调用多个 setter
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            });
            productVo.setProductInfoVOList(productInfoVOList);
            productVoList.add(productVo);
        });
        return productVoList;
    }
}
