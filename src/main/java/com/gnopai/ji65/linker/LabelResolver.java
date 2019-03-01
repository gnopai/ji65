package com.gnopai.ji65.linker;

import com.gnopai.ji65.assembler.*;

public class LabelResolver implements SegmentDataVisitor {
    private Environment environment;
    private int startIndex;
    private int index;

    public void resolve(AssembledSegments segments, int startIndex) {
        this.environment = segments.getEnvironment();
        this.startIndex = startIndex;
        this.index = 0;
        segments.getSegment("CODE")
                .orElseThrow(() -> new RuntimeException("Expected at least a code segment for now"))
                .getSegmentData()
                .forEach(this::resolve);
    }

    private void resolve(SegmentData segment) {
        segment.accept(this);
    }

    @Override
    public void visit(InstructionData instructionData) {
        index += instructionData.getByteCount();
    }

    @Override
    public void visit(RawData rawData) {
        index += rawData.getByteCount();
    }

    @Override
    public void visit(Label label) {
        environment.define(label.getName(), startIndex + index);
    }

    @Override
    public void visit(UnresolvedExpression unresolvedExpression) {
        // no-op -- bytes counted in the parent instruction data
    }
}
