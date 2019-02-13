package com.gnopai.ji65.linker;

import com.gnopai.ji65.NewProgram;
import com.gnopai.ji65.compiler.*;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class Linker implements SegmentDataVisitor {

    // TODO take in a config object too that specifies how the segments should be laid out
    // TODO how to specify addressing? A Map? A giant List or array?
    public NewProgram link(CompiledSegments segments) {
        List<Byte> bytes = segments.getSegment("CODE")
                .orElseThrow(() -> new RuntimeException("Expected at least a code segment for now"))
                .getSegmentData()
                .stream()
                .flatMap(segmentData -> getBytes(segmentData).stream())
                .collect(toUnmodifiableList());
        return new NewProgram(bytes);
    }

    private List<Byte> getBytes(SegmentData segmentData) {
        return segmentData.accept(this);
    }

    @Override
    public List<Byte> visit(InstructionData instructionData) {
        return List.copyOf(ImmutableList.<Byte>builder()
                .add(instructionData.getOpcode().getOpcode())
                .addAll(getBytes(instructionData.getOperand()))
                .build()
        );
    }

    @Override
    public List<Byte> visit(RawData rawData) {
        return rawData.getBytes();
    }

    @Override
    public List<Byte> visit(Label label) {
        // TODO store label info in the program
        return Collections.emptyList();
    }
}
