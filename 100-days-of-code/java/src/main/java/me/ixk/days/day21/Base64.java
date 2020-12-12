/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day21;

import java.nio.charset.StandardCharsets;

/**
 * Base64 工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:00
 */
public class Base64 extends cn.hutool.core.codec.Base64 {

    public static String encode(String data) {
        return encode(data.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String encode(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }

    public static String decode(String data) {
        return new String(
            decode(data.getBytes(StandardCharsets.ISO_8859_1)),
            StandardCharsets.ISO_8859_1
        );
    }

    public static byte[] decode(byte[] data) {
        return java.util.Base64.getDecoder().decode(data);
    }

    public static String encodeUrlSafe(String data) {
        return encodeUrlSafe(data.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String encodeUrlSafe(byte[] data) {
        return new String(java.util.Base64.getUrlEncoder().encode(data));
    }

    public static String decodeUrlSafe(String data) {
        return decodeUrlSafe(data.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String decodeUrlSafe(byte[] data) {
        return new String(java.util.Base64.getUrlDecoder().decode(data));
    }

    public static byte[] toByte(String data) {
        return data.getBytes(StandardCharsets.ISO_8859_1);
    }
}
