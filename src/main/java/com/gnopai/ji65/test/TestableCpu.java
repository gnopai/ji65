package com.gnopai.ji65.test;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Cpu;
import lombok.Getter;

import java.util.*;

public class TestableCpu extends Cpu {
    private final Map<Address, MemoryWatch> memoryWatches = new HashMap<>();
    private final Map<Address, MemoryMock> memoryMocks = new HashMap<>();

    public TestableCpu() {
        super((byte) 0, (byte) 0, (byte) 0, (byte) 0xFF, 0, (byte) 0);
    }

    public void addWatch(Address address) {
        memoryWatches.put(address, new MemoryWatch());
    }

    public List<Byte> getWatchBytesWritten(Address address) {
        return getWatch(address)
                .map(MemoryWatch::getBytesWritten)
                .orElseThrow(() -> new RuntimeException("No watch defined at address " + address));
    }

    public int getWatchReadCount(Address address) {
        return getWatch(address)
                .map(MemoryWatch::getReadCount)
                .orElseThrow(() -> new RuntimeException("No watch defined at address " + address));
    }

    public void mockMemoryValues(Address address, Byte... values) {
        memoryMocks.put(address, new MemoryMock(List.of(values)));
    }

    private Optional<MemoryWatch> getWatch(Address address) {
        return Optional.ofNullable(memoryWatches.get(address));
    }

    @Override
    public void setMemoryValue(Address address, byte value) {
        getWatch(address).ifPresent(memoryWatch -> memoryWatch.addByteWritten(value));
        super.setMemoryValue(address, value);
    }

    @Override
    public byte getMemoryValue(Address address) {
        getWatch(address).ifPresent(MemoryWatch::addRead);

        return Optional.ofNullable(memoryMocks.get(address))
                .map(MemoryMock::getNextValue)
                .orElseGet(() -> super.getMemoryValue(address));
    }

    @Getter
    private static class MemoryWatch {
        private final List<Byte> bytesWritten = new ArrayList<>();
        private int readCount = 0;

        public void addRead() {
            readCount++;
        }

        public void addByteWritten(byte b) {
            bytesWritten.add(b);
        }
    }

    private static class MemoryMock {
        private final List<Byte> values;
        private int index = 0;

        private MemoryMock(List<Byte> values) {
            this.values = values;
        }

        public byte getNextValue() {
            int lastIndex = values.size() - 1;
            if (index < lastIndex) {
                return values.get(index++);
            }
            return values.get(lastIndex);
        }
    }
}
