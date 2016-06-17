package com.nfl.scte35.decoder.model;

/**
 * Created by andres.aguilar on 6/16/16.
 */
public class SpliceInsert {

    public int spliceEventID;
    public int spliceEventCancelIndicator;
    public int reserved1;
    public int outOfNetworkIndicator;
    public int programSpliceFlag;
    public SpliceTime sisp = new SpliceTime();
    public int durationFlag;
    public int spliceImmediateFlag;
    public BreakDuration breakDuration = new BreakDuration();
    public int reserved2;
    public int uniqueProgramID;
    public int availNum;
    public int availsExpected;
}
