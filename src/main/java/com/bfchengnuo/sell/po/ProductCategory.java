package com.bfchengnuo.sell.po;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 对于 @DynamicUpdate 的说明：
 * 如果我们在更新表时,只想更新某个字段,就不要加 @DynamicUpdate,通常为了更新表时的效率,都是不加的.
 * 反之,如果我们更新某个字段时,更新所有的字段,就可以加上 @DynamicUpdate.
 * DynamicInsert 同理
 *
 * 本例中，如果增加了 updateTime 字段，则需要使用 DynamicUpdate 注解配合数据设置的
 *      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 *      来由数据库自动更新这个更新时间，因为默认 updateTime 不设置值或者不变的话，sql 是不会拼上的，数据库设置的规则也就无效了
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@Entity
@DynamicUpdate
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String categoryName;

    private Integer categoryType;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }
}
