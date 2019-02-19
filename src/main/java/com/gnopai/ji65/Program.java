package com.gnopai.ji65;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class Program {
    List<Byte> bytes;
    Address memoryAddress;
    Map<String, Integer> labels;

    public Program(List<Byte> bytes) {
        this(bytes, Map.of(), new Address(0));
    }

    public Program(List<Byte> bytes, Map<String, Integer> labels, Address memoryAddress) {
        this.bytes = bytes;
        this.labels = labels;
        this.memoryAddress = memoryAddress;
    }
}
