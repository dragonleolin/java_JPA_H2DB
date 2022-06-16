package com.example.demo.util;

import java.io.UnsupportedEncodingException;

public class HexStringUtil {


    /* [static] field */

    /* [static] */

    /* [static] method */

    /* [instance] field */

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    /* [instance] constructor */

    /* [instance] method */

    public static String toString(byte[] bytes) throws UnsupportedEncodingException {
//        TsmpArgExaminer.requireNonNull(bytes, "bytes");

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] toBytes(String str) {
//        TsmpArgExaminer.requireNonNullNonNull(str, "str");

        if (str.length() == 0) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            String subStr = str.substring(2 * i, 2 * i + 2);
            bytes[i] = ((byte) Integer.parseInt(subStr, 16));
        }

        return bytes;
    }

    /* [instance] getter/setter */
}
