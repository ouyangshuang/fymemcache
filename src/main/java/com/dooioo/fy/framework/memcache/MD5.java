package com.dooioo.fy.framework.memcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 *
 * @author ouyang
 * @since 2015-07-01 17:45
 */
public class MD5 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5.class);

    public static String getMD5String(byte... bytes) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] digest = md.digest();
            StringBuilder buf = new StringBuilder("");
            for (byte b : digest) {
                int i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append('0');
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getClass().getName(), e);
        }
        return result;
    }

}
