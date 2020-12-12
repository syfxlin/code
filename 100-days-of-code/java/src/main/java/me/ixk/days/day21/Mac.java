/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day21;

import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;

/**
 * Mac 工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:04
 */
public class Mac {

    protected static String byteArrayToHexString(final byte[] b) {
        final StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static String make(
        final String algo,
        final String data,
        final String key
    ) {
        return make(
            algo,
            data,
            key.getBytes(StandardCharsets.ISO_8859_1),
            false
        );
    }

    public static String make(
        final String algo,
        final String data,
        final byte[] key
    ) {
        return make(algo, data, key, false);
    }

    public static String make(
        final String algo,
        final String data,
        final String key,
        final boolean raw
    ) {
        return make(algo, data, key.getBytes(StandardCharsets.ISO_8859_1), raw);
    }

    public static String make(
        final String algo,
        final String data,
        final byte[] key,
        final boolean raw
    ) {
        try {
            final SecretKeySpec hmacKeySpec = new SecretKeySpec(key, algo);
            final javax.crypto.Mac hmac = javax.crypto.Mac.getInstance(algo);
            hmac.init(hmacKeySpec);
            final byte[] rawHmac = hmac.doFinal(
                data.getBytes(StandardCharsets.ISO_8859_1)
            );
            return raw
                ? new String(rawHmac, StandardCharsets.ISO_8859_1)
                : byteArrayToHexString(rawHmac);
        } catch (final Exception ex) {
            return null;
        }
    }
}
