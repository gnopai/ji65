package com.gnopai.ji65;

import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
public class Program {
    List<Chunk> chunks;
    Map<String, Integer> labels;

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
