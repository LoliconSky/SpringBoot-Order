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

    private ResultVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResultVo<T> createBySuccess(T data) {
        return new ResultVo<>(0, "成功", data);
    }

    public static ResultVo createBySuccess() {
        return new ResultVo<>(0, "成功");
    }

    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVO = new ResultVo();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

}
