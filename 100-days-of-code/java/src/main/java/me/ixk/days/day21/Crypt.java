/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day21;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import me.ixk.days.day9.Json;

/**
 * 加密工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:02
 */
public class Crypt {

    private final byte[] key;

    private final Cipher cipher;

    public Crypt(byte[] key)
        throws NoSuchAlgorithmException, NoSuchPaddingException {
        this(key, "AES/CBC/PKCS5PADDING");
    }

    public Crypt(byte[] key, String cipher)
        throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = key;
        this.cipher = Cipher.getInstance(cipher);
    }

    public String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(generateIv());
            SecretKeySpec aesKeySpec = new SecretKeySpec(key, "AES");

            cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, iv);

            String encrypted = new String(
                cipher.doFinal(value.getBytes(StandardCharsets.ISO_8859_1)),
                StandardCharsets.ISO_8859_1
            );
            String ivEncoded = Base64.encode(iv.getIV());
            String macEncoded = Mac.make(
                "HmacSHA256",
                ivEncoded + encrypted,
                key
            );
            ObjectNode json = Json.createObject();
            json.put("iv", ivEncoded);
            json.put("value", encrypted);
            json.put("mac", macEncoded);
            return Base64.encode(json.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public String decrypt(String encrypted) {
        try {
            ObjectNode payload = Json.parseObject(Base64.decode(encrypted));
            if (!this.vaild(Objects.requireNonNull(payload))) {
                return null;
            }
            IvParameterSpec iv = new IvParameterSpec(
                Base64
                    .decode(payload.get("iv").asText())
                    .getBytes(StandardCharsets.ISO_8859_1)
            );
            SecretKeySpec aesKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKeySpec, iv);
            return new String(
                cipher.doFinal(
                    payload
                        .get("value")
                        .asText()
                        .getBytes(StandardCharsets.ISO_8859_1)
                ),
                StandardCharsets.ISO_8859_1
            );
        } catch (Exception e) {
            return null;
        }
    }

    public boolean vaild(ObjectNode payload) {
        if (
            !payload.has("iv") || !payload.has("mac") || !payload.has("value")
        ) {
            return false;
        }
        if (Base64.decode(payload.get("iv").asText()).length() != 16) {
            return false;
        }
        return payload
            .get("mac")
            .asText()
            .equals(
                Mac.make(
                    "HmacSHA256",
                    payload.get("iv").asText() + payload.get("value").asText(),
                    key
                )
            );
    }

    public static byte[] generateKey() {
        return generateRandom(256);
    }

    public static byte[] generateIv() {
        return generateRandom(128);
    }

    public static byte[] generateRandom(int length) {
        KeyGenerator generator;
        try {
            generator = KeyGenerator.getInstance("AES");
            generator.init(length);
            SecretKey key = generator.generateKey();
            return key.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
