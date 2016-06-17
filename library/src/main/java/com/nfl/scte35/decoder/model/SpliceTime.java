package com.nfl.scte35.decoder.model;

/**
 * Created by andres.aguilar on 6/16/16.
 */
public class SpliceTime {

    public int timeSpecifiedFlag;
    public int reserved1;
    public long ptsTime;
    public int reserved2;

    @Override
    public String toString() {
        return "SpliceTime{" +
                "timeSpecifiedFlag=" + timeSpecifiedFlag +
                ", reserved1=" + reserved1 +
                ", ptsTime=" + ptsTime +
                ", reserved2=" + reserved2 +
                '}';
    }
}
