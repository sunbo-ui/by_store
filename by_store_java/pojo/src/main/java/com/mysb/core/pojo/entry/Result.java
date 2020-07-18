package com.mysb.core.pojo.entry;

import java.io.Serializable;

public class Result implements Serializable {
    private boolean success;
    private String message;
    private boolean flage;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    //用于判断是否存cookie还是redis   true--->redis
    public Result(boolean success, String message,boolean flage) {
        this.success = success;
        this.message = message;
        this.flage = flage;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean isFlage() {
        return flage;
    }

    public void setFlage(boolean flage) {
        this.flage = flage;
    }

}
