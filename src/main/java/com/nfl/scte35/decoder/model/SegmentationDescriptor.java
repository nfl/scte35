package com.nfl.scte35.decoder.model;

/**
 * Created by andres.aguilar on 6/16/16.
 */
public class SegmentationDescriptor {

    public int spliceDescriptorTag;
    public int descriptorLength;
    public int identifier;
    public int segmentationEventID;
    public int segmentationEventCancelIndicator;
    public int reserved1;
    public int programSegmentationFlag;
    public int segmentationDurationFlag;
    public int deliveryNotRestricted;
    public int webDeliveryAllowedFlag;
    public int noRegionalBlackoutFlag;
    public int archiveAllowed;
    public int deviceRestriction;
    public int reserved2;
    public long segmentationDuration;
    public long turnerIdentifier;
    public int segmentationUPIDtype;
    public int segmentationUPIDlength;
    public int segmentationTypeID;
    public int segmentNum;
    public int segmentsExpected;
}
