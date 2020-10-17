package cn.aulang.office.sdk.util;

import java.security.SecureRandom;
import java.util.Calendar;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 18:57
 */
public class UUID {
    // 62进制数
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z'};

    // 62进制个数
    private static final int LENGTH = DIGITS.length;
    // 随机数生成器
    private static final SecureRandom GENERATOR = new SecureRandom();
    // 两位62进制对应的十进制数最大值
    private static final int MAX = DIGITS.length * DIGITS.length - 1;

    /**
     * 十进制数转62进制数，十进制数不能大于两位62进制对应的十进制数最大值
     *
     * @param number 十进制数
     * @return 两位62进制数
     */
    private static char[] decTo62(int number) {
        if (number > MAX) {
            throw new IllegalArgumentException("超出系统最大值：" + MAX);
        }
        if (number < 0) {
            throw new IllegalArgumentException("不能小于0：" + number);
        }

        char[] chars = new char[]{'0', '0'};
        chars[1] = DIGITS[(number - (number / LENGTH) * LENGTH)];
        number = number / LENGTH;
        chars[0] = DIGITS[(number - (number / LENGTH) * LENGTH)];

        return chars;
    }

    /**
     * UUID字符串生成
     *
     * @return String UUID字符串
     */
    public static String generate() {
        char[] chars = new char[17];
        Calendar now = Calendar.getInstance();

        // 随机数
        chars[0] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 年
        char[] src = decTo62(now.get(Calendar.YEAR));
        System.arraycopy(src, 0, chars, 1, 2);
        // 随机数
        chars[3] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 月
        chars[4] = DIGITS[now.get(Calendar.MONTH)];
        // 随机数
        chars[5] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 日
        chars[6] = DIGITS[now.get(Calendar.DAY_OF_MONTH)];
        // 随机数
        chars[7] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 时
        chars[8] = DIGITS[now.get(Calendar.HOUR_OF_DAY)];
        // 随机数
        chars[9] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 分
        chars[10] = DIGITS[now.get(Calendar.MINUTE)];
        // 随机数
        chars[11] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 秒
        chars[12] = DIGITS[now.get(Calendar.SECOND)];
        // 随机数
        chars[13] = DIGITS[GENERATOR.nextInt(LENGTH)];
        // 毫秒
        src = decTo62(now.get(Calendar.MILLISECOND));
        System.arraycopy(src, 0, chars, 14, 2);
        // 随机数
        chars[16] = DIGITS[GENERATOR.nextInt(LENGTH)];

        return new String(chars);
    }
}