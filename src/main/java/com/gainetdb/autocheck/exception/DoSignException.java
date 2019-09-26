package com.gainetdb.autocheck.exception;


import com.gainetdb.autocheck.enums.ResultEnum;

/**
 * Created by 廖师兄
 * 2017-06-11 18:55
 */
public class DoSignException extends RuntimeException{

    private Integer code;

    public DoSignException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public DoSignException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
