package com.example.ocecreditcardlibrary.util;

/**
 * Created by kanthimp on 14/12/2560.
 */

public class ResultService {
    private Success success;
    private String failed = "";

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }
}
