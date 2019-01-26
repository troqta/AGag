package com.accenture.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util {
    public static final String DEFAULT_PIC_PATH = "C:\\Users\\Fast1r1s\\Desktop\\default.png";
    public static final String PASSWORD_LENGTH_MESSAGE = "Password length should be between 5 and 20 symbols long";
    public static final String USERNAME_LENGTH_MESSAGE = "Username length should be between 5 and 20 symbols long";


    public static boolean isAnonymous(){
        return AnonymousAuthenticationToken.class ==
                SecurityContextHolder
                        .getContext()
                        .getAuthentication().getClass();
    }
}
