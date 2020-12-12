package me.ixk.days.day21;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/12 下午 4:20
 */
class Day21Test {

    @Test
    void crypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        final byte[] key = Crypt.generateKey();
        final Crypt crypt = new Crypt(key);
        final String encrypt = crypt.encrypt("123");
        assertNotNull(encrypt);
        assertEquals("123", crypt.decrypt(encrypt));
    }
}
