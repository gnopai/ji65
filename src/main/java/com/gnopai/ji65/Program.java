package com.gnopai.ji65;

import com.gnopai.ji65.test.Test;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
@AllArgsConstructor
public class Program {
    List<Chunk> chunks;
    Map<String, Integer> labels;
    List<Test> tests;

    public Program(List<Chunk> chunks, Map<String, Integer> labels) {
        this.chunks = chunks;
        this.labels = labels;
        this.tests = List.of();
    }

    public Optional<Address> getLabelAddress(String label) {
        return Optional.ofNullable(labels.get(label))
            .map(Address::new);
    }

    @Value
    public static class Chunk {
        Address address;
        List<Byte> bytes;
    }
}
