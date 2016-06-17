package com.nfl.scte35.decoder.model;

/**
 * Created by andres.aguilar on 6/16/16.
 */
public class DTMFDescriptor {

    public int spliceDescriptorTag;
    public int descriptorLength;
    public int identifier;
    public int preroll;
    public int dtmfCount;
    public int reserved;
    public byte[] DTMFChar = new byte[8];
}
