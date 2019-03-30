package com.gnopai.ji65.config;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
public class MemoryConfig {
    String name;
    Address startAddress;
    int size;
    MemoryType memoryType;
    String file;
    boolean dataFillEnabled;
    byte fillValue;
}
