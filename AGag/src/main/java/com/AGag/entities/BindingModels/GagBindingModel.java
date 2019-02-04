package com.AGag.entities.BindingModels;

public class GagBindingModel {

    private String name;

    private String tagString;

    public GagBindingModel(String name, String tagString) {
        this.name = name;
        this.tagString = tagString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }
}
