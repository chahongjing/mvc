package com.zjy.baseframework.common;

import com.zjy.baseframework.enums.ErrorCodeEnum;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getName());
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
