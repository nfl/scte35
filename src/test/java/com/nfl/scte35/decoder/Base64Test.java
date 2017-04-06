package com.nfl.scte35.decoder;

import org.testng.Assert;

/**
 * Created by andres.aguilar on 4/6/2017.
 * Validate that old {@link Base64} can be safely replaced with {@link org.apache.commons.codec.binary.Base64}
 */
public class Base64Test {
    @org.testng.annotations.Test
    public void testDecodeFast() throws Exception {
        String hello = org.apache.commons.codec.binary.Base64.encodeBase64String( "Hello".getBytes());
        Assert.assertEquals(new String(Base64.decodeFast(hello)), new String (org.apache.commons.codec.binary.Base64.decodeBase64(hello)));
    }

}