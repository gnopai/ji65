package com.gnopai.ji65.config;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProgramConfig {
    List<MemoryConfig> memoryConfigs;
    List<SegmentConfig> segmentConfigs;
}
