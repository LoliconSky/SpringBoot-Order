package com.bfchengnuo.sell.enums;

import lombok.Getter;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@Getter
public enum ProductStatusEnum implements CodeEnum<Integer> {
    UP(0, "在架"),
    DOWN(1, "下架");

    private Integer code;
    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
