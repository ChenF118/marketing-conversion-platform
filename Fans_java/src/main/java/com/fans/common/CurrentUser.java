package com.fans.common;

/**
 * 当前登录用户上下文（由 JwtFilter 写入，请求结束后清理）。
 * power >= 1 视为超管，可见全部数据；power = 0 为运营，仅可见 ownerId = 自己的数据。
 */
public final class CurrentUser {

    public record Info(Integer id, int power) {}

    private static final ThreadLocal<Info> HOLDER = new ThreadLocal<>();

    private CurrentUser() {}

    public static void set(Info info) {
        HOLDER.set(info);
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static Info get() {
        return HOLDER.get();
    }

    public static Integer id() {
        Info info = HOLDER.get();
        return info == null ? null : info.id();
    }

    public static int power() {
        Info info = HOLDER.get();
        return info == null ? 0 : info.power();
    }

    public static boolean isAdmin() {
        return power() >= 1;
    }

    /** 超管可访问全部；运营只能访问自己创建的记录（历史无归属数据仅超管可见） */
    public static boolean canAccess(Integer ownerId) {
        Info info = HOLDER.get();
        if (info == null) return false;
        if (info.power() >= 1) return true;
        return ownerId != null && ownerId.equals(info.id());
    }
}
