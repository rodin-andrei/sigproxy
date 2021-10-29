/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author okulikov
 */
public class IOUtils {
    public static String readString(InputStream in) throws IOException {
        final ByteArrayOutputStream builder = new ByteArrayOutputStream();
        final byte[] buff = new byte[8192];
        int len;
        while ((len = in.read(buff)) != -1) {
            builder.write(buff, 0, len);
        }
        return builder.toString();
    }

}
