package cn.aulang.oauth.client.user;

/**
 * @author aulang
 * @email aulang@qq.com
 * @date 2020-10-22 10:29
 */
public class UserHolder {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static User get() {
        return threadLocal.get();
    }

    public static void set(User user) {
        threadLocal.set(user);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
