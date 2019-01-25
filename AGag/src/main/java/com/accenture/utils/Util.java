package com.accenture.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util {
    public static final String DEFAULT_PIC_PATH = "C:\\Users\\aleksandar.simeonov\\Desktop\\default.png";
    public static boolean isAnonymous(){
        return AnonymousAuthenticationToken.class ==
                SecurityContextHolder
                        .getContext()
                        .getAuthentication().getClass();
    }
}
