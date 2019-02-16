package com.gnopai.ji65;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class Program {
    List<Byte> bytes;
    Map<String, Integer> labels;

    public Program(List<Byte> bytes) {
        this(bytes, Map.of());
    }

    public Program(List<Byte> bytes, Map<String, Integer> labels) {
        this.bytes = bytes;
        this.labels = labels;
    }
}
