package com.gnopai.ji65.config;

import com.gnopai.ji65.Address;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
public class SegmentConfig {
    String segmentName;
    String memoryConfigName;
    SegmentType segmentType;
    Address startAddress;
    int alignment;
}
