package com.nfl.scte35.decoder;

import org.junit.Test;

/**
 * Created by andres.aguilar on 6/17/16.
 */
public class Scte35DecoderTest {
    @Test
    public void base64Decode() throws Exception {
        Scte35Decoder scte35Decoder = new Scte35Decoder(false);
        System.out.println(scte35Decoder.base64Decode("/DAlAAAAAAAAAP/wFAUAAAPof+//SVqZrP4Ae5igAAEBAQAAQcfnVA=="));

        System.out.println("==============");
        System.out.println(scte35Decoder.base64Decode("/DAlAAAAAAAAAP/wFAUAACtnf+/+s9z9LP4Ae5igAAEBAQAAwWSPdQ=="));
    }

}