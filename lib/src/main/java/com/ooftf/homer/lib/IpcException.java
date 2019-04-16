package com.ooftf.homer.lib;

public class IpcException extends Exception{
    int code;

    public IpcException(String message, int code) {
        super(message);
        this.code = code;
    }
}
