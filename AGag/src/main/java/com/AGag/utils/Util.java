package com.AGag.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Util {
//    @Autowired
//    private static PathProperty pathProperty;



    public static final String DEFAULT_PIC_PATH = "/upload-dir/default/default.png";
    public static final String PASSWORD_LENGTH_MESSAGE = "Password length should be between 5 and 20 symbols long";
    public static final String USERNAME_LENGTH_MESSAGE = "Username length should be between 5 and 20 symbols long";
    public static final String DEFAULT_UPLOAD_DIR = "upload-dir";
//    public static final  String PATH_TO_UPLOAD_DIR = pathProperty.getPath();
    public static final int GAGS_PER_PAGE = 2;
    public static final String BAD_CREDENTIALS_MESSAGE = "Wrong username/password!";


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
