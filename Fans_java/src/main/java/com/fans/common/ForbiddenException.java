package com.fans.common;

/** 无权限访问/操作某条数据 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
