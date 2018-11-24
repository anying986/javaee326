package com.itheima.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        return s.replace("-","");
    }
}
