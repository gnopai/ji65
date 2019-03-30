package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Segment;
import com.gnopai.ji65.config.SegmentConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SegmentAddressCalculatorTest {

    @Test
    void testWithStartAddress_inRange() {
        Segment segment = segmentWithStartAddress(0x2000);
        Address result = new SegmentAddressCalculator()
                .calculateAddress(segment, 0x2000, 0x2800);
        assertEquals(new Address(0x2000), result);
    }

    @Test
    void testWithStartAddress_aboveAcceptableRange() {
        Segment segment = segmentWithStartAddress(0x2001);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> new SegmentAddressCalculator()
                .calculateAddress(segment, 0x1000, 0x2000)
        );
        assertEquals("Invalid start address for segment 'whee': 8193", exception.getMessage());
    }

    @Test
    void testWithStartAddress_belowAcceptableRange() {
        Segment segment = segmentWithStartAddress(0x2000);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> new SegmentAddressCalculator()
                .calculateAddress(segment, 0x2001, 0x2800)
        );
        assertEquals("Invalid start address for segment 'whee': 8192", exception.getMessage());
    }

    @Test
    void testNoStartAddress_noAlignment() {
        Segment segment = segmentWithAlignment(0);
        Address result = new SegmentAddressCalculator()
                .calculateAddress(segment, 0x2000, 0x2800);
        assertEquals(new Address(0x2000), result);
    }

    @Test
    void testNoStartAddress_alignmentButAlreadyAligned() {
        Segment segment = segmentWithAlignment(0x10);
        Address result = new SegmentAddressCalculator()
                .calculateAddress(segment, 0x2250, 0x2800);
        assertEquals(new Address(0x2250), result);
    }

    @Test
    void testNoStartAddress_alignmentApplied() {
        Segment segment = segmentWithAlignment(0x100);
        Address result = new SegmentAddressCalculator()
                .calculateAddress(segment, 0x2250, 0x2800);
        assertEquals(new Address(0x2300), result);
    }

    private Segment segmentWithStartAddress(int startAddress) {
        return new Segment(SegmentConfig.builder()
                .segmentName("whee")
                .startAddress(new Address(startAddress))
                .build());
    }

    private Segment segmentWithAlignment(int alignment) {
        return new Segment(SegmentConfig.builder()
                .segmentName("whee")
                .alignment(alignment)
                .build());
    }
}