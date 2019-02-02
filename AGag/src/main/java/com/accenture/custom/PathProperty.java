package com.accenture.custom;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.upload")
public class PathProperty {

    private String path = "file:////C:/Users/Fast1r1s/Desktop/AccentureGag/AGag/upload-dir/";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
