package com.bfchengnuo.sell.vo;

import lombok.Data;

/**
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@Data
public class ResultVo<T> {
    private int code;
    private String msg;
    private T data;

    public ResultVo() {
    }

    private ResultVo(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultVo createBySuccess(Object data) {
        return new ResultVo<>(0, "成功", data);
    }

}
