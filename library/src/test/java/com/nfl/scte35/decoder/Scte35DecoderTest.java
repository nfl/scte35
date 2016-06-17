package com.nfl.scte35.decoder;

import com.nfl.scte35.decoder.model.BreakDuration;
import com.nfl.scte35.decoder.model.SpliceInfoSection;
import com.nfl.scte35.decoder.model.SpliceInsert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by andres.aguilar on 6/17/16.
 */
public class Scte35DecoderTest {

    private Scte35Decoder scte35Decoder;

    @Before
    public void setUp() throws Exception {
        scte35Decoder = new Scte35Decoder(false);
    }


    @Test
    public void testExample1() throws Exception {
        System.out.println(scte35Decoder.base64Decode("/DAlAAAAAAAAAP/wFAUAAAPof+//SVqZrP4Ae5igAAEBAQAAQcfnVA=="));

    }

    /**
     * Given input:
     * {@code "/DAlAAAAAAAAAP/wFAUAACtnf+/+s9z9LP4Ae5igAAEBAQAAwWSPdQ=="}
     * Expected result:
     * {@code
     * <SpliceInfoSection tableID="252" sectionSyntaxIndicator="0" privateIndicator="0" reserved_0="3" sectionLength="37" protocolVersion="0" ptsAdj ustment="0" cwIndex="0" tier="4095" spliceCommandLength="20">
     * <SpliceInsert spliceEventId="11111" spliceEventCancelIndicator="0" reserved_0="127" outOfNetworkIndicator="1" spliceImmediateFlag="0" reserved_1="15" uniqueProgramId="1" availNum="1" availsExpected="1">
     * <Program>
     * <SpliceTime ptsTime="3017604396"/>
     * </Program>
     * <BreakDuration autoReturn="1" reserved="63" duration="8100000"/>
     * </SpliceInsert>
     * <descriptorLoopLength>0</descriptorLoopLength>
     * <AlignmentStuffing>0</AlignmentStuffing>
     * <Crc_32>-1050374283</Crc_32>
     * </SpliceInfoSection>
     * }
     * <p>
     * NOTE: reserved fields indices are shifted by 1, i.e. reserved_0 => reserved1
     *
     * @throws Exception
     */
    @Test
    public void testExample2() throws Exception {
        SpliceInfoSection spliceInfoSection = scte35Decoder.base64Decode("/DAlAAAAAAAAAP/wFAUAACtnf+/+s9z9LP4Ae5igAAEBAQAAwWSPdQ==");
        assertEquals(252, spliceInfoSection.tableID);
        assertEquals(3, spliceInfoSection.reserved1);
        assertEquals(37, spliceInfoSection.sectionLength);
        assertEquals(4095, spliceInfoSection.tier);
        assertEquals(20, spliceInfoSection.spliceCommandLength);
        SpliceInsert spliceInsert = spliceInfoSection.spliceInsert;
        assertEquals(11111, spliceInsert.spliceEventID);
        //assertEquals(127,spliceInsert.reserved1); //Not set in decoder
        assertEquals(1, spliceInsert.outOfNetworkIndicator);
        //assertEquals(15,spliceInsert.reserved2); //Not set in decoder
        assertEquals(1, spliceInsert.uniqueProgramID);
        assertEquals(1, spliceInsert.availNum);
        assertEquals(1, spliceInsert.availsExpected);
        BreakDuration breakDuration = spliceInsert.breakDuration;
        assertEquals(1, breakDuration.autoReturn);
        //assertEquals(63, breakDuration.reserved1); //Not set in decoder
        assertEquals(8100000L, breakDuration.duration);
        assertEquals(-1050374283L, spliceInfoSection.CRC32);

        System.out.println(spliceInfoSection);
    }

}