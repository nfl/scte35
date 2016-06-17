package com.nfl.scte35.decoder;

public class Main {

    public static void main(String[] args) {

        Scte35Decoder scte35Decoder = new Scte35Decoder();
        System.out.println(scte35Decoder.base64Decode("/DAlAAAAAAAAAP/wFAUAAAPof+//SVqZrP4Ae5igAAEBAQAAQcfnVA=="));

        System.out.println("==============");
        System.out.println(scte35Decoder.base64Decode("/DAlAAAAAAAAAP/wFAUAACtnf+/+s9z9LP4Ae5igAAEBAQAAwWSPdQ=="));
    }
}
