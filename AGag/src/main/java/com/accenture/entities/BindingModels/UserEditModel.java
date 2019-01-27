package com.accenture.entities.BindingModels;

public class UserEditModel {
    private String nickname;

    private String password;

    private String oldPassword;

    private String email;

    private String profilePic;

    public UserEditModel(String nickname, String password, String oldPassword, String email, String profilePic) {
        this.nickname = nickname;
        this.password = password;
        this.oldPassword = oldPassword;
        this.email = email;
        this.profilePic = profilePic;
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

    public String getProfilePic() {
        return profilePic;
    }
}
