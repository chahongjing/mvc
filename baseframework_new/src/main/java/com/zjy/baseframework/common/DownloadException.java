package com.zjy.baseframework.common;

/**
 * author: meichao
 * create: 2019-09-10 18:05
 * Description:
 */
public class DownloadException extends RuntimeException{

    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(String message, Throwable newNested) {
        super(message, newNested);
    }

    public DownloadException(Throwable newNested) {
        super(newNested);
    }
}
