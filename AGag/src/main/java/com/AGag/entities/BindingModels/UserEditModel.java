package com.AGag.entities.BindingModels;

public class UserEditModel {
    private String nickname;

    private String password;

    private String oldPassword;

    private String email;


    public UserEditModel(String nickname, String password, String oldPassword, String email) {
        this.nickname = nickname;
        this.password = password;
        this.oldPassword = oldPassword;
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getEmail() {
        return email;
    }

}
