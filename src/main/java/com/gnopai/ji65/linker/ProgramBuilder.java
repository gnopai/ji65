package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ProgramBuilder {
    private final Map<Integer, byte[]> segmentBytes;
    private final Map<String, Integer> labels;

    private byte[] bytes;
    private int startIndex;
    private int index;

    public ProgramBuilder() {
        segmentBytes = new HashMap<>();
        labels = new HashMap<>();
    }

    public ProgramBuilder segment(MappedSegment segment) {
        completeLastSegment();
        bytes = new byte[segment.getSize()];
        startIndex = segment.getStartAddress().getValue();
        index = 0;
        return this;
    }

    public ProgramBuilder bytes(byte b) {
        this.bytes[index++] = b;
        return this;
    }

    public ProgramBuilder bytes(List<Byte> bytes) {
        for (byte b : bytes) {
            this.bytes[index++] = b;
        }
        return this;
    }

    public ProgramBuilder label(String name, int address) {
        labels.put(name, address);
        return this;
    }

    private void completeLastSegment() {
        if (bytes != null) {
            segmentBytes.put(startIndex, bytes);
            bytes = null;
        }
    }

    public Program build() {
        completeLastSegment();

        List<Program.Chunk> chunks = segmentBytes.entrySet().stream()
                .map(entry -> makeChunk(entry.getKey(), entry.getValue()))
                .collect(toList());

        return new Program(chunks, Map.copyOf(labels));
    }

    public int getCurrentIndex() {
        return startIndex + index;
    }

    private Program.Chunk makeChunk(int startIndex, byte[] bytes) {
        List<Byte> byteList = new ArrayList<>(bytes.length);
        for (byte b : bytes) {
            byteList.add(b);
        }
        return new Program.Chunk(new Address(startIndex), List.copyOf(byteList));
    }
}
