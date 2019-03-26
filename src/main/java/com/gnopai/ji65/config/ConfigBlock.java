package com.gnopai.ji65.config;

import lombok.Value;

import java.util.List;

@Value
class ConfigBlock {
    String name;
    List<ConfigSegment> segments;
}
