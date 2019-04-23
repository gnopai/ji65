package com.gnopai.ji65.linker;

import com.gnopai.ji65.assembler.*;

public class LabelResolver implements SegmentDataVisitor {
    private Environment environment;
    private int startIndex;
    private int offset;

    public void resolve(MappedSegments mappedSegments, Environment environment) {
        this.environment = environment;
        mappedSegments.getSegments().forEach(this::resolve);
    }

    private void resolve(MappedSegment segment) {
        this.startIndex = segment.getStartAddress().getValue();
        this.offset = 0;
        segment.getData().forEach(this::resolve);
    }

    private void resolve(SegmentData segment) {
        segment.accept(this);
    }

    @Override
    public void visit(InstructionData instructionData) {
        offset += instructionData.getByteCount();
    }

    @Override
    public void visit(RawData rawData) {
        offset += rawData.getByteCount();
    }

    @Override
    public void visit(Label label) {
        environment.define(label.getName(), startIndex + offset);
    }

    @Override
    public void visit(UnresolvedExpression unresolvedExpression) {
        // no-op -- bytes counted in the parent instruction data
    }

    @Override
    public void visit(RelativeUnresolvedExpression relativeUnresolvedExpression) {
        // no-op -- bytes counted in the parent instruction data
    }

    @Override
    public void visit(ReservedData reservedData) {
        offset += reservedData.getByteCount();
    }
}
