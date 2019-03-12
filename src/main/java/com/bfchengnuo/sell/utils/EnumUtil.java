package com.bfchengnuo.sell.utils;

import com.bfchengnuo.sell.enums.CodeEnum;

/**
 * 用于将 code 转换为枚举类型
 * <p>
 * Created by 冰封承諾Andy on 2019/3/11.
 */
public class EnumUtil {

    public static <K, T extends CodeEnum<K>> T getByCode(K code, Class<T> clazz) {
        for (T enumConstant : clazz.getEnumConstants()) {
            if (code.equals(enumConstant.getCode())) {
                return enumConstant;
            }
        }
        return null;
    }
}
