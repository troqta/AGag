package com.accenture.entities.BindingModels;

import com.accenture.utils.Util;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UserBindingModel {
    @NotNull
    @Length(min = 5, max = 20, message = Util.PASSWORD_LENGTH_MESSAGE)
    private String password;

    @NotNull
    @Length(min = 5, max = 20, message = Util.USERNAME_LENGTH_MESSAGE)
    private String username;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
