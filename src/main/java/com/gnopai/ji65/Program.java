package com.gnopai.ji65;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class Program {
    List<Chunk> chunks;
    Map<String, Integer> labels;
    Address startAddress;

    public Program(List<Chunk> chunks, Map<String, Integer> labels, Address startAddress) {
        this.chunks = chunks;
        this.labels = labels;
        this.startAddress = startAddress;
    }

    @Value
    public static class Chunk {
        Address address;
        List<Byte> bytes;
    }
}
