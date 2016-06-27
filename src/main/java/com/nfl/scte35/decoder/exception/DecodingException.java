package com.nfl.scte35.decoder.exception;

/**
 * Created by andres.aguilar on 6/17/16.
 */
public class DecodingException extends Exception {
    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(String message, Exception e) {
        super(message, e);
    }
}
