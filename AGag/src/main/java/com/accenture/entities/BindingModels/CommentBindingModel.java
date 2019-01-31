package com.accenture.entities.BindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CommentBindingModel {
    @NotNull
    @Length(min = 1)
    String content;

    public CommentBindingModel(){

    }

    public CommentBindingModel(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
