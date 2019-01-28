package com.accenture.utils;

import com.accenture.entities.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.Path;

public class Util {
    public static final String DEFAULT_PIC_PATH = "/upload-dir/default/default.png";
    public static final String PASSWORD_LENGTH_MESSAGE = "Password length should be between 5 and 20 symbols long";
    public static final String USERNAME_LENGTH_MESSAGE = "Username length should be between 5 and 20 symbols long";
    public static final String DEFAULT_UPLOAD_DIR = "upload-dir";
    public static final String PATH_TO_UPLOAD_DIR = "file:////C:/Users/aleksandar.simeonov/Desktop/AccentureGag/AGag/upload-dir/";


    public static boolean isAnonymous(){
        return AnonymousAuthenticationToken.class ==
                SecurityContextHolder
                        .getContext()
                        .getAuthentication().getClass();
    }

    public static UserDetails currentUser() {
        if (Util.isAnonymous()) {
            return null;
        }
        return (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
