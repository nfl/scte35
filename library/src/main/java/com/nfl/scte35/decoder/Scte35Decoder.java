
package com.nfl.scte35.decoder;

import com.nfl.scte35.decoder.model.SegmentationDescriptor;
import com.nfl.scte35.decoder.model.SpliceInfoSection;
import com.nfl.scte35.decoder.model.SpliceInsert;
import com.nfl.scte35.decoder.model.TimeSignal;

import javax.xml.bind.DatatypeConverter;

public final class Scte35Decoder {

    public static final int SPLICE_NULL = 0x00;
    public static final int SPLICE_SCHEDULE = 0x04;
    public static final int SPLICE_INSERT = 0x05;
    public static final int TIME_SIGNAL = 0x06;
    public static final int BANDWIDTH_RESERVATION = 0x07;
    public static final int PRIVATE_COMMAND = 0x00ff;
    public static final int[] CrcTable = {
            0x00000000, 0x04C11DB7, 0x09823B6E, 0x0D4326D9, 0x130476DC, 0x17C56B6B, 0x1A864DB2, 0x1E475005, 0x2608EDB8, 0x22C9F00F,
            0x2F8AD6D6, 0x2B4BCB61, 0x350C9B64, 0x31CD86D3, 0x3C8EA00A, 0x384FBDBD, 0x4C11DB70, 0x48D0C6C7, 0x4593E01E, 0x4152FDA9,
            0x5F15ADAC, 0x5BD4B01B, 0x569796C2, 0x52568B75, 0x6A1936C8, 0x6ED82B7F, 0x639B0DA6, 0x675A1011, 0x791D4014, 0x7DDC5DA3,
            0x709F7B7A, 0x745E66CD, 0x9823B6E0, 0x9CE2AB57, 0x91A18D8E, 0x95609039, 0x8B27C03C, 0x8FE6DD8B, 0x82A5FB52, 0x8664E6E5,
            0xBE2B5B58, 0xBAEA46EF, 0xB7A96036, 0xB3687D81, 0xAD2F2D84, 0xA9EE3033, 0xA4AD16EA, 0xA06C0B5D, 0xD4326D90, 0xD0F37027,
            0xDDB056FE, 0xD9714B49, 0xC7361B4C, 0xC3F706FB, 0xCEB42022, 0xCA753D95, 0xF23A8028, 0xF6FB9D9F, 0xFBB8BB46, 0xFF79A6F1,
            0xE13EF6F4, 0xE5FFEB43, 0xE8BCCD9A, 0xEC7DD02D, 0x34867077, 0x30476DC0, 0x3D044B19, 0x39C556AE, 0x278206AB, 0x23431B1C,
            0x2E003DC5, 0x2AC12072, 0x128E9DCF, 0x164F8078, 0x1B0CA6A1, 0x1FCDBB16, 0x018AEB13, 0x054BF6A4, 0x0808D07D, 0x0CC9CDCA,
            0x7897AB07, 0x7C56B6B0, 0x71159069, 0x75D48DDE, 0x6B93DDDB, 0x6F52C06C, 0x6211E6B5, 0x66D0FB02, 0x5E9F46BF, 0x5A5E5B08,
            0x571D7DD1, 0x53DC6066, 0x4D9B3063, 0x495A2DD4, 0x44190B0D, 0x40D816BA, 0xACA5C697, 0xA864DB20, 0xA527FDF9, 0xA1E6E04E,
            0xBFA1B04B, 0xBB60ADFC, 0xB6238B25, 0xB2E29692, 0x8AAD2B2F, 0x8E6C3698, 0x832F1041, 0x87EE0DF6, 0x99A95DF3, 0x9D684044,
            0x902B669D, 0x94EA7B2A, 0xE0B41DE7, 0xE4750050, 0xE9362689, 0xEDF73B3E, 0xF3B06B3B, 0xF771768C, 0xFA325055, 0xFEF34DE2,
            0xC6BCF05F, 0xC27DEDE8, 0xCF3ECB31, 0xCBFFD686, 0xD5B88683, 0xD1799B34, 0xDC3ABDED, 0xD8FBA05A, 0x690CE0EE, 0x6DCDFD59,
            0x608EDB80, 0x644FC637, 0x7A089632, 0x7EC98B85, 0x738AAD5C, 0x774BB0EB, 0x4F040D56, 0x4BC510E1, 0x46863638, 0x42472B8F,
            0x5C007B8A, 0x58C1663D, 0x558240E4, 0x51435D53, 0x251D3B9E, 0x21DC2629, 0x2C9F00F0, 0x285E1D47, 0x36194D42, 0x32D850F5,
            0x3F9B762C, 0x3B5A6B9B, 0x0315D626, 0x07D4CB91, 0x0A97ED48, 0x0E56F0FF, 0x1011A0FA, 0x14D0BD4D, 0x19939B94, 0x1D528623,
            0xF12F560E, 0xF5EE4BB9, 0xF8AD6D60, 0xFC6C70D7, 0xE22B20D2, 0xE6EA3D65, 0xEBA91BBC, 0xEF68060B, 0xD727BBB6, 0xD3E6A601,
            0xDEA580D8, 0xDA649D6F, 0xC423CD6A, 0xC0E2D0DD, 0xCDA1F604, 0xC960EBB3, 0xBD3E8D7E, 0xB9FF90C9, 0xB4BCB610, 0xB07DABA7,
            0xAE3AFBA2, 0xAAFBE615, 0xA7B8C0CC, 0xA379DD7B, 0x9B3660C6, 0x9FF77D71, 0x92B45BA8, 0x9675461F, 0x8832161A, 0x8CF30BAD,
            0x81B02D74, 0x857130C3, 0x5D8A9099, 0x594B8D2E, 0x5408ABF7, 0x50C9B640, 0x4E8EE645, 0x4A4FFBF2, 0x470CDD2B, 0x43CDC09C,
            0x7B827D21, 0x7F436096, 0x7200464F, 0x76C15BF8, 0x68860BFD, 0x6C47164A, 0x61043093, 0x65C52D24, 0x119B4BE9, 0x155A565E,
            0x18197087, 0x1CD86D30, 0x029F3D35, 0x065E2082, 0x0B1D065B, 0x0FDC1BEC, 0x3793A651, 0x3352BBE6, 0x3E119D3F, 0x3AD08088,
            0x2497D08D, 0x2056CD3A, 0x2D15EBE3, 0x29D4F654, 0xC5A92679, 0xC1683BCE, 0xCC2B1D17, 0xC8EA00A0, 0xD6AD50A5, 0xD26C4D12,
            0xDF2F6BCB, 0xDBEE767C, 0xE3A1CBC1, 0xE760D676, 0xEA23F0AF, 0xEEE2ED18, 0xF0A5BD1D, 0xF464A0AA, 0xF9278673, 0xFDE69BC4,
            0x89B8FD09, 0x8D79E0BE, 0x803AC667, 0x84FBDBD0, 0x9ABC8BD5, 0x9E7D9662, 0x933EB0BB, 0x97FFAD0C, 0xAFB010B1, 0xAB710D06,
            0xA6322BDF, 0xA2F33668, 0xBCB4666D, 0xB8757BDA, 0xB5365D03, 0xB1F740B4
    };
    public static SegmentationDescriptor[] seg = new SegmentationDescriptor[10];

    public long crc32(byte[] b64, int startIdx, int endIdx) {
        int value = 0xFFFFFFFF;
        int ptr;

        for (int i = startIdx; i < endIdx; i++) {
            ptr = (((value >> 24) & 0x00ff) ^ b64[i]) & 0x00FF;
            value = (value << 8) ^ CrcTable[ptr];
        }

        return (value & 0xFFFFFFFFL);
    }

    private String decode35(byte[] b64) {
        String ot = "";
        int i1;
        int i2;
        long l1;
        long l2;
        long l3;
        long l4;
        long l5;
        long l6;
        long l7;
        long l8;
        int bufptr;
        int desptr;
        int segptr = 0;

        String stemp = "";
        ot = "Hex=0x";

        for (int i = 0; i < b64.length; i++) {
            stemp += String.format("%02X", b64[i]);
        }
        ot += stemp + "\nBase64=" + Base64.encodeToString(b64, false) + "\n\n";

        ot += "Decoded length = " + b64.length + "\n";

        SpliceInfoSection spliceInfoSection = new SpliceInfoSection();

        spliceInfoSection.tableID = b64[0] & 0x00ff;
        if (spliceInfoSection.tableID != 0x0FC) {
            ot = "Invalid Table ID != 0xFC";
            return ot;
        }
        ot += "Table ID = 0xFC\n";

        spliceInfoSection.sectionSyntaxIndicator = (b64[1] >> 7) & 0x01;
        if (spliceInfoSection.sectionSyntaxIndicator != 0) {
            ot += "ERROR Long section used\n";
        } else {
            ot += "MPEG Short Section\n";
        }

        spliceInfoSection.privateIndicator = (b64[1] >> 6) & 0x01;
        if (spliceInfoSection.privateIndicator != 0) {
            ot += "ERROR Private section signaled\n";
        } else {
            ot += "Not Private\n";
        }

        spliceInfoSection.reserved1 = (b64[1] >> 4) & 0x03;
        ot += String.format("Reserved = 0x%x\n", spliceInfoSection.reserved1);

        i1 = b64[1] & 0x0f;
        i2 = b64[2] & 0x00ff;
        spliceInfoSection.sectionLength = (i1 << 8) + i2;
        ot += ("Section Length = " + spliceInfoSection.sectionLength + "\n");

        spliceInfoSection.protocolVersion = b64[3];
        ot += ("Protocol Version = " + spliceInfoSection.protocolVersion + "\n");

        spliceInfoSection.encryptedPacket = (b64[4] >> 7) & 0x01;
        spliceInfoSection.encryptionAlgorithm = (b64[4] >> 1) & 0x3F;
        if (spliceInfoSection.encryptedPacket != 0) {
            ot += "Encrypted Packet\n";
            ot += String.format("Encryption Algorithm = 0x%x\n", spliceInfoSection.encryptionAlgorithm);
        } else {
            ot += "unencrypted Packet\n";
        }

        l1 = b64[4] & 0x01;
        l2 = b64[5] & 0x00ff;
        l3 = b64[6] & 0x00ff;
        l4 = b64[7] & 0x00ff;
        l5 = b64[8] & 0x00ff;
        spliceInfoSection.ptsAdjustment = (l1 << 32) + (l2 << 24) + (l3 << 16) + (l4 << 8) + l5;
        ot += String.format("PTS Adjustment = 0x%09x\n", spliceInfoSection.ptsAdjustment);

        spliceInfoSection.cwIndex = b64[9] & 0x00ff;
        if (spliceInfoSection.encryptedPacket != 0) {
            ot += String.format("CW Index = 0x%x\n", spliceInfoSection.cwIndex);
        }

        i1 = b64[10] & 0x00ff;
        i2 = (b64[11] & 0x00f0) >> 4;
        spliceInfoSection.tier = (i1 << 4) + i2;
        ot += String.format("Tier = 0x%x\n", spliceInfoSection.tier);

        i1 = b64[11] & 0x000f;
        i2 = b64[12] & 0x00ff;
        spliceInfoSection.spliceCommandLength = (i1 << 8) + i2;
        ot += String.format("Splice Command Length = 0x%x\n", spliceInfoSection.spliceCommandLength);

        spliceInfoSection.spliceCommandType = b64[13] & 0x00ff;
        bufptr = 14;
        SpliceInsert spliceInsert = new SpliceInsert();
        switch (spliceInfoSection.spliceCommandType) {
            case SPLICE_NULL:
                ot += "Splice Null\n";
                break;
            case SPLICE_SCHEDULE:
                ot += "Splice Schedule\n";
                break;
            case SPLICE_INSERT:
                ot += "Splice Insert\n";
                l1 = b64[bufptr] & 0x00ff;
                bufptr++;
                l2 = b64[bufptr] & 0x00ff;
                bufptr++;
                l3 = b64[bufptr] & 0x00ff;
                bufptr++;
                l4 = b64[bufptr] & 0x00ff;
                bufptr++;
                spliceInsert.spliceEventID = (int) (((l1 << 24) + (l2 << 16) + (l3 << 8) + l4) & 0x00ffffffff);
                ot += String.format("Splice Event ID = 0x%x\n", spliceInsert.spliceEventID);

                i1 = b64[bufptr] & 0x080;
                bufptr++;
                if (i1 != 0) {
                    spliceInsert.spliceEventCancelIndicator = 1;
                    ot += "Splice Event Canceled\n";
                } else {
                    spliceInsert.spliceEventCancelIndicator = 0;
                }

                spliceInsert.outOfNetworkIndicator = (b64[bufptr] & 0x080) >> 7;
                spliceInsert.programSpliceFlag = (b64[bufptr] & 0x040) >> 6;
                spliceInsert.durationFlag = (b64[bufptr] & 0x020) >> 5;
                spliceInsert.spliceImmediateFlag = (b64[bufptr] & 0x010) >> 4;
                bufptr++;
                ot += "Flags OON=" + spliceInsert.outOfNetworkIndicator + " Prog=" + spliceInsert.programSpliceFlag
                        + " Duration=" + spliceInsert.durationFlag + " Immediate=" + spliceInsert.spliceImmediateFlag + "\n";

                if ((spliceInsert.programSpliceFlag == 1) && (spliceInsert.spliceImmediateFlag == 0)) {
                    if ((b64[bufptr] & 0x080) != 0) {
                        // time specified
                        l1 = b64[bufptr] & 0x01;
                        bufptr++;
                        l2 = b64[bufptr] & 0x00ff;
                        bufptr++;
                        l3 = b64[bufptr] & 0x00ff;
                        bufptr++;
                        l4 = b64[bufptr] & 0x00ff;
                        bufptr++;
                        l5 = b64[bufptr] & 0x00ff;
                        spliceInsert.sisp.ptsTime = (l1 << 32) + (l2 << 24) + (l3 << 16) + (l4 << 8) + l5;
                        ot += String.format("Splice time = 0x%09x\n", spliceInsert.sisp.ptsTime);
                    }
                    bufptr++;
                }

                if (spliceInsert.durationFlag != 0) {
                    spliceInsert.breakDuration.autoReturn = (b64[bufptr] & 0x080) >> 7;
                    if (spliceInsert.breakDuration.autoReturn != 0) {
                        ot += "Auto Return\n";
                    }
                    l1 = b64[bufptr] & 0x01;
                    bufptr++;
                    l2 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    l3 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    l4 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    l5 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    spliceInsert.breakDuration.duration = (l1 << 32) + (l2 << 24) + (l3 << 16) + (l4 << 8) + l5;
                    double bsecs = spliceInsert.breakDuration.duration;
                    bsecs /= 90000.0;
                    ot += String.format("break duration = 0x%09x = %f seconds\n", spliceInsert.breakDuration.duration, bsecs);
                }
                i1 = b64[bufptr] & 0x00ff;
                bufptr++;
                i2 = b64[bufptr] & 0x00ff;
                bufptr++;
                spliceInsert.uniqueProgramID = (i1 << 8) + i2;
                ot += "Unique Program ID = " + spliceInsert.uniqueProgramID + "\n";

                spliceInsert.availNum = b64[bufptr] & 0x00ff;
                bufptr++;
                ot += "Avail Num = " + spliceInsert.availNum + "\n";

                spliceInsert.availsExpected = b64[bufptr] & 0x00ff;
                bufptr++;
                ot += "Avails Expected = " + spliceInsert.availsExpected + "\n";

                break;
            case TIME_SIGNAL:
                ot += "Time Signal\n";
                TimeSignal timeSignal = new TimeSignal();
                timeSignal.tssp.timeSpecifiedFlag = (b64[bufptr] & 0x080) >> 7;
                if (timeSignal.tssp.timeSpecifiedFlag != 0) {
                    // time specified
                    l1 = b64[bufptr] & 0x01;
                    bufptr++;
                    l2 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    l3 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    l4 = b64[bufptr] & 0x00ff;
                    bufptr++;
                    l5 = b64[bufptr] & 0x00ff;
                    timeSignal.tssp.ptsTime = (l1 << 32) + (l2 << 24) + (l3 << 16) + (l4 << 8) + l5;
                    ot += String.format("Time = 0x%09x\n", timeSignal.tssp.ptsTime);
                }
                bufptr++;
                break;
            case BANDWIDTH_RESERVATION:
                ot += "Bandwidth Reservation\n";
                break;
            case PRIVATE_COMMAND:
                ot += "Private Command\n";
                break;
            default:
                ot += String.format("ERROR Unknown command = 0x%x\n", spliceInfoSection.spliceCommandType);
                // Unknown command, oops
                break;
        }

        if (spliceInfoSection.spliceCommandLength != 0x0fff) { // legacy check
            if (bufptr != (spliceInfoSection.spliceCommandLength + 14)) {
                ot += "ERROR decoded command length " + bufptr + " not equal to specified command length " + spliceInfoSection.spliceCommandLength + "\n";
                //Some kind of error, or unknown command
                //bufptr = spliceInfoSection.spliceCommandLength + 14;
            }
        }

        i1 = b64[bufptr] & 0x00ff;
        bufptr++;
        i2 = b64[bufptr] & 0x00ff;
        bufptr++;
        spliceInfoSection.descriptorLoopLength = (i1 << 8) + i2;
        ot += "Descriptor Loop Length = " + spliceInfoSection.descriptorLoopLength + "\n";

        desptr = bufptr;

        if (spliceInfoSection.descriptorLoopLength > 0) {
            while ((bufptr - desptr) < spliceInfoSection.descriptorLoopLength) {
                int tag = b64[bufptr] & 0x00ff;
                bufptr++;
                int len = b64[bufptr] & 0x00ff;
                bufptr++;
                l1 = b64[bufptr] & 0x00ff;
                bufptr++;
                l2 = b64[bufptr] & 0x00ff;
                bufptr++;
                l3 = b64[bufptr] & 0x00ff;
                bufptr++;
                l4 = b64[bufptr] & 0x00ff;
                bufptr++;
                int identifier = (int) ((l1 << 24) + (l2 << 16) + (l3 << 8) + l4);
                if (identifier == 0x43554549) {
                    switch (tag) {
                        case 0:
                            ot += "Avail Descriptor - Length=" + len + "\n";
                            l1 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            l2 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            l3 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            l4 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            int availDesc = (int) (((l1 << 24) + (l2 << 16) + (l3 << 8) + l4) & 0x00ffffffff);
                            ot += String.format("Avail Descriptor = 0x%08x\n", availDesc);
                            break;
                        case 1:
                            ot += "DTMF Descriptor - Length=" + len + "\n";
                            double preroll = b64[bufptr] & 0x00ff;
                            preroll /= 10;
                            ot += "Preroll = " + preroll + "\n";
                            bufptr++;
                            int dtmfCount = (b64[bufptr] & 0x00E0) >> 5;
                            bufptr++;
                            ot += dtmfCount + "DTMF chars = ";
                            for (int i = 0; i < dtmfCount; i++) {
                                ot += String.format("%c", b64[bufptr] & 0x00ff);
                                bufptr++;
                            }
                            ot += "\n";
                            break;
                        case 2:
                            ot += "Segmentation Descriptor - Length=" + len + "\n";
                            seg[segptr] = new SegmentationDescriptor();
                            l1 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            l2 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            l3 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            l4 = b64[bufptr] & 0x00ff;
                            bufptr++;
                            seg[segptr].segmentationEventID = (int) (((l1 << 24) + (l2 << 16) + (l3 << 8) + l4) & 0x00ffffffff);
                            ot += String.format("Segmentation Event ID = 0x%08x\n", seg[segptr].segmentationEventID);
                            seg[segptr].segmentationEventCancelIndicator = (b64[bufptr] & 0x080) >> 7;
                            bufptr++;
                            if (seg[segptr].segmentationEventCancelIndicator == 0) {
                                ot += "Segmentation Event Cancel Indicator NOT set\n";
                                seg[segptr].programSegmentationFlag = (b64[bufptr] & 0x080) >> 7;
                                seg[segptr].segmentationDurationFlag = (b64[bufptr] & 0x040) >> 6;
                                seg[segptr].deliveryNotRestricted = (b64[bufptr] & 0x020) >> 5;
                                ot += "Delivery Not Restricted flag = " + seg[segptr].deliveryNotRestricted + "\n";
                                if (seg[segptr].deliveryNotRestricted == 0) {
                                    seg[segptr].webDeliveryAllowedFlag = (b64[bufptr] & 0x010) >> 4;
                                    ot += "Web Delivery Allowed flag = " + seg[segptr].webDeliveryAllowedFlag + "\n";
                                    seg[segptr].noRegionalBlackoutFlag = (b64[bufptr] & 0x008) >> 3;
                                    ot += "No Regional Blackout flag = " + seg[segptr].noRegionalBlackoutFlag + "\n";
                                    seg[segptr].archiveAllowed = (b64[bufptr] & 0x004) >> 2;
                                    ot += "Archive Allowed flag = " + seg[segptr].archiveAllowed + "\n";
                                    seg[segptr].deviceRestriction = (b64[bufptr] & 0x003);
                                    ot += "Device Restrictions = " + seg[segptr].deviceRestriction + "\n";
                                }
                                bufptr++;
                                if (seg[segptr].programSegmentationFlag == 0) {
                                    ot += "Component segmention NOT IMPLEMENTED\n";
                                } else {
                                    ot += "Program Segmentation flag SET\n";
                                }
                                if (seg[segptr].segmentationDurationFlag == 1) {
                                    l1 = b64[bufptr] & 0x0ff;
                                    bufptr++;
                                    l2 = b64[bufptr] & 0x00ff;
                                    bufptr++;
                                    l3 = b64[bufptr] & 0x00ff;
                                    bufptr++;
                                    l4 = b64[bufptr] & 0x00ff;
                                    bufptr++;
                                    l5 = b64[bufptr] & 0x00ff;
                                    bufptr++;
                                    seg[segptr].segmentationDuration = (l1 << 32) + (l2 << 24) + (l3 << 16) + (l4 << 8) + l5;
                                    double secs = seg[segptr].segmentationDuration;
                                    secs /= 90000.0;
                                    ot += String.format("Segmentation Duration = 0x%010x = %f seconds\n", seg[segptr].segmentationDuration, secs);
                                }
                                seg[segptr].segmentationUPIDtype = b64[bufptr] & 0x00ff;
                                bufptr++;
                                seg[segptr].segmentationUPIDlength = b64[bufptr] & 0x00ff;
                                bufptr++;
                                switch (seg[segptr].segmentationUPIDtype) {
                                    case 0x00:
                                        ot += "UPID Type = Not Used length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        break;
                                    case 0x01:
                                        ot += "UPID Type = User Defined (Deprecated) length =" + seg[segptr].segmentationUPIDlength + "\nHex=0x";
                                        for (int j = bufptr; j < (bufptr + seg[segptr].segmentationUPIDlength); j++) {
                                            ot += String.format("%02X.", b64[j]);
                                        }
                                        ot += "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x02:
                                        ot += "UPID Type = ISCII (deprecated)length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        String siTemp = "ISCII=";
                                        for (int j = bufptr; j < (bufptr + seg[segptr].segmentationUPIDlength); j++) {
                                            siTemp += (char) b64[j];
                                        }
                                        siTemp += "\n";
                                        ot += siTemp;
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x03:
                                        ot += "UPID Type = Ad-IDlength = " + seg[segptr].segmentationUPIDlength + "\n";
                                        String stTemp = "AdId=";
                                        for (int j = bufptr; j < (bufptr + seg[segptr].segmentationUPIDlength); j++) {
                                            stTemp += (char) b64[j];
                                        }
                                        stTemp += "\n";
                                        ot += stTemp;
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x04:
                                        ot += "UPID Type = UMID SMPTE 330M length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x05:
                                        ot += "UPID Type = ISAN (Deprecated) length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x06:
                                        ot += "UPID Type = ISAN length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x07:
                                        ot += "UPID Type = Tribune ID length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x08:
                                        ot += "UPID Type = Turner Identifier length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        l1 = b64[bufptr] & 0x0ff;
                                        bufptr++;
                                        l2 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        l3 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        l4 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        l5 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        l6 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        l7 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        l8 = b64[bufptr] & 0x00ff;
                                        bufptr++;
                                        seg[segptr].turnerIdentifier = (l1 << 56) + (l2 << 48) + (l3 << 40) + (l4 << 32) + (l5 << 24) + (l6 << 16) + (l7 << 8) + l8;
                                        ot += String.format("Turner Identifier = 0x%016x\n", seg[segptr].turnerIdentifier);
                                        break;
                                    case 0x09:
                                        ot += "UPID Type = ADI length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x0A:
                                        ot += "UPID Type = EIDR length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x0B:
                                        ot += "UPID Type = ATSC Content Identifier length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x0C:
                                        ot += "UPID Type = Managed Private UPID length = " + seg[segptr].segmentationUPIDlength + "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    case 0x0D:
                                        ot += "UPID Type = Multiple UPID length = " + seg[segptr].segmentationUPIDlength + "\nHex=0x";
                                        for (int j = bufptr; j < (bufptr + seg[segptr].segmentationUPIDlength); j++) {
                                            ot += String.format("%02X.", b64[j]);
                                        }
                                        ot += "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                    default:
                                        ot += "UPID Type = UNKNOWN length = " + seg[segptr].segmentationUPIDlength + "\nHex=0x";
                                        for (int j = bufptr; j < (bufptr + seg[segptr].segmentationUPIDlength); j++) {
                                            ot += String.format("%02X.", b64[j]);
                                        }
                                        ot += "\n";
                                        bufptr += seg[segptr].segmentationUPIDlength;
                                        break;
                                }
                                seg[segptr].segmentationTypeID = b64[bufptr] & 0x00ff;
                                bufptr++;
                                switch (seg[segptr].segmentationTypeID) {
                                    case 0x00:
                                        ot += "Type = Not Indicated\n";
                                        break;
                                    case 0x01:
                                        ot += "Type = Content Identification\n";
                                        break;
                                    case 0x10:
                                        ot += "Type = Program Start\n";
                                        break;
                                    case 0x11:
                                        ot += "Type = Program End\n";
                                        break;
                                    case 0x12:
                                        ot += "Type = Program Early Termination\n";
                                        break;
                                    case 0x13:
                                        ot += "Type = Program Breakaway\n";
                                        break;
                                    case 0x14:
                                        ot += "Type = Program Resumption\n";
                                        break;
                                    case 0x15:
                                        ot += "Type = Program Runover Planned\n";
                                        break;
                                    case 0x16:
                                        ot += "Type = Program Runover Unplanned\n";
                                        break;
                                    case 0x17:
                                        ot += "Type = Program Overlap Start\n";
                                        break;
                                    case 0x20:
                                        ot += "Type = Chapter Start\n";
                                        break;
                                    case 0x21:
                                        ot += "Type = Chapter End\n";
                                        break;
                                    case 0x30:
                                        ot += "Type = Provider Advertisement Start\n";
                                        break;
                                    case 0x31:
                                        ot += "Type = Provider Advertisement End\n";
                                        break;
                                    case 0x32:
                                        ot += "Type = Distributor Advertisement Start\n";
                                        break;
                                    case 0x33:
                                        ot += "Type = Distributor Advertisement End\n";
                                        break;
                                    case 0x34:
                                        ot += "Type = Placement Opportunity Start\n";
                                        break;
                                    case 0x35:
                                        ot += "Type = Placement Opportunity End\n";
                                        break;
                                    case 0x40:
                                        ot += "Type = Unscheduled Event Start\n";
                                        break;
                                    case 0x41:
                                        ot += "Type = Unscheduled Event End\n";
                                        break;
                                    case 0x50:
                                        ot += "Type = Network Start\n";
                                        break;
                                    case 0x51:
                                        ot += "Type = Network End\n";
                                        break;
                                    default:
                                        ot += "Type = Unknown = " + seg[segptr].segmentationTypeID + "\n";
                                        break;
                                }
                                seg[segptr].segmentNum = b64[bufptr] & 0x00ff;
                                bufptr++;
                                seg[segptr].segmentsExpected = b64[bufptr] & 0x00ff;
                                bufptr++;
                                ot += "Segment num = " + seg[segptr].segmentNum + " Segments Expected = " + seg[segptr].segmentsExpected + "\n";
                                segptr++;
                            } else {
                                ot += "Segmentation Event Cancel Indicator SET\n";
                            }
                            break;
                    }
                } else {
                    ot += String.format("Private Descriptor tag=%d Length=%d identifier = 0x%08x  Value = 0x", tag, len, identifier);
                    for (int j = bufptr; j < (bufptr + (len - 4)); j++) {
                        ot += String.format("%02X.", b64[j]);
                    }
                    ot += "\n";
                    bufptr += len - 4;
                }
            }
        }

        if (bufptr != (spliceInfoSection.descriptorLoopLength + desptr)) {
            int dlen = bufptr - desptr;
            ot += "ERROR decoded descriptor length " + dlen + " not equal to specified descriptor length " + spliceInfoSection.descriptorLoopLength + "\n";
            bufptr = desptr + spliceInfoSection.descriptorLoopLength;
            ot += "SKIPPING REST OF THE COMMAND!!!!!!\n";
        } else {

            if (spliceInfoSection.encryptedPacket != 0) {
                spliceInfoSection.alignmentStuffing = 0;
                spliceInfoSection.eCRC32 = 0;
            }

            l1 = b64[bufptr] & 0x00ff;
            bufptr++;
            l2 = b64[bufptr] & 0x00ff;
            bufptr++;
            l3 = b64[bufptr] & 0x00ff;
            bufptr++;
            l4 = b64[bufptr] & 0x00ff;
            bufptr++;
            spliceInfoSection.CRC32 = (int) (((l1 << 24) + (l2 << 16) + (l3 << 8) + l4) & 0x00ffffffff);
            ot += String.format("CRC32 = 0x%08x\n", spliceInfoSection.CRC32);
        }
        ot += String.format("calc CRC32 = 0x%08x --- Should = 0x00000000\n", crc32(b64, 0, bufptr));
        return ot;
    }


    public String hexDecode(String hexin) {
        byte[] b64 = DatatypeConverter.parseHexBinary(hexin);
        //System.out.println(Base64.encodeToString(b64, false));
        return decode35(b64);
    }

    public String base64Decode(String base64in) {
        byte[] b64 = Base64.decodeFast(base64in);
        String stemp = "";
        for (int i = 0; i < b64.length; i++) {
            stemp += String.format("%02X", b64[i]);
        }
        //System.out.println(stemp);

        return hexDecode(stemp);//??
    }

}