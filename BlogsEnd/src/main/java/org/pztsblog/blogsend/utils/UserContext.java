package org.pztsblog.blogsend.utils;

public class UserContext {
    // 线程本地变量，专门用来存当前登录用户的 ID
    private static final ThreadLocal<Long> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_THREAD_LOCAL.set(userId);
    }

    public static Long getUserId() {
        return USER_THREAD_LOCAL.get();
    }

    public static void removeUserId() {
        USER_THREAD_LOCAL.remove(); // 用完必须清理，防止内存泄漏
    }
}