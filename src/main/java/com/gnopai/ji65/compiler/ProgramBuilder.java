package com.gnopai.ji65.compiler;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramBuilder {
    private final byte[] bytes;
    private final Map<String, Integer> labels;
    private final int startIndex;
    private int index;

    public ProgramBuilder(int size, int startIndex) {
        bytes = new byte[size];
        labels = new HashMap<>();
        this.startIndex = startIndex;
        index = 0;
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

    public ProgramBuilder label(String name) {
        labels.put(name, startIndex + index);
        return this;
    }

    public Program build() {
        List<Byte> byteList = new ArrayList<>(bytes.length);
        for (byte b : bytes) {
            byteList.add(b);
        }
        return new Program(List.copyOf(byteList), Map.copyOf(labels), new Address(startIndex));
    }
}
