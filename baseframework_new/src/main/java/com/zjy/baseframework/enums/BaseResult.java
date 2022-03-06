package com.zjy.baseframework.enums;

import com.zjy.baseframework.common.Constants;

import java.io.Serializable;
import java.util.Objects;

public class BaseResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ResultStatus status;
    private String msg;
    private T value;

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("status: %s; msg: %s; value: %s", status, msg, Objects.toString(value, Constants.EMPTY_STRING));
    }

    /// <summary>
    /// 无参构造函数
    /// </summary>
    public BaseResult() {
        this(ResultStatus.OK, null, null);
    }

    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    public BaseResult(ResultStatus status) {
        this(status, null, null);
    }


    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    /// <param name="message">返回信息</param>
    public BaseResult(ResultStatus status, String msg) {
        this(status, msg, null);
    }


    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    /// <param name="val">记录</param>
    public BaseResult(ResultStatus status, T val) {
        this(status, null, val);
    }


    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    /// <param name="message">返回信息</param>
    /// <param name="val">记录</param>
    public BaseResult(ResultStatus status, String msg, T val) {
        this.status = status;
        this.msg = msg;
        this.value = val;
    }

    /// <summary>
    /// OK
    /// </summary>
    /// <returns></returns>
    public static <T> BaseResult<T> ok() {
        return ok(null);
    }

    /// <summary>
    /// OK
    /// </summary>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> ok(T value) {
        return ok(value, null);
    }

    /// <summary>
    /// OK
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> ok(T value, String message) {
        return new BaseResult<>(ResultStatus.OK, message, value);
    }

    /// <summary>
    /// NO
    /// </summary>
    /// <returns></returns>
    public static <T> BaseResult<T> no() {
        return no(Constants.EMPTY_STRING);
    }

    /// <summary>
    /// NO
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <returns></returns>
    public static <T> BaseResult<T> no(String message) {
        return no(message, null);
    }

    /// <summary>
    /// NO
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> no(String message, T value) {
        return new BaseResult<>(ResultStatus.NO, message, value);
    }

    /// <summary>
    /// ERROR
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <returns></returns>
    public static <T> BaseResult<T> error(String message) {
        return error(message, null);
    }

    /// <summary>
    /// ERROR
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> error(String message, T value) {
        return new BaseResult<>(ResultStatus.ERROR, message, value);
    }
}
