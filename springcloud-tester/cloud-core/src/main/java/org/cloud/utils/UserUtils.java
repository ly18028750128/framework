package org.cloud.utils;

public class UserUtils {
    private UserUtils(){

    }
    private final static UserUtils instance = new UserUtils();

    public static UserUtils single(){
        return instance;
    }

//    public final
}
