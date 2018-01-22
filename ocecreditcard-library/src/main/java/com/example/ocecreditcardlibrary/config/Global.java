package com.example.ocecreditcardlibrary.config;

/**
 * Created by kanthimp on 13/12/2560.
 */

public class Global {
    private static Global instance;
    private int requestCamera = 2626;
    private String cloudVisionKey = "";

    public static Global getInstance() {
        if (instance == null)
            instance = new Global();
        return instance;
    }

    public int getRequestCamera() {
        return requestCamera;
    }

    public void setRequestCamera(int requestCamera) {
        this.requestCamera = requestCamera;
    }

    public String getCloudVisionKey() {
        return cloudVisionKey;
    }

    public void setCloudVisionKey(String cloudVisionKey) {
        this.cloudVisionKey = cloudVisionKey;
    }
}
