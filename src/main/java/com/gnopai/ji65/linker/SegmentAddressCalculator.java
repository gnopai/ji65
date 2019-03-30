package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.assembler.Segment;

public class SegmentAddressCalculator {
    public Address calculateAddress(Segment segment, int currentAddress, int nextAddress) {
        return segment.getStartAddress()
                .map(startAddress -> {
                    int startAddressValue = startAddress.getValue();
                    if (startAddressValue < currentAddress || startAddressValue >= nextAddress) {
                        throw new RuntimeException("Invalid start address for segment '" + segment.getName() + "': " + startAddress.getValue());
                    }
                    return startAddress;
                })
                .orElse(alignAddress(currentAddress, segment.getAlignment()));
    }

    private Address alignAddress(int address, int alignment) {
        int alignmentOffset = alignment > 0 ? (alignment - (address % alignment)) % alignment : 0;
        return new Address(address + alignmentOffset);
    }
}
