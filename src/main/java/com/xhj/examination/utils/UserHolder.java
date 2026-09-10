package com.xhj.examination.utils;

import com.xhj.examination.entity.User;

public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void set(User user){ tl.set(user); }
    public static User get(){ return tl.get(); }
    public static Long getUserId(){ return tl.get().getId(); }
    public static String getRole(){ return tl.get().getIdentity(); }
    public static void remove(){ tl.remove(); }
}