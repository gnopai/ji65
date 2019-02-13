package com.gnopai.ji65.compiler;

import java.util.List;

public interface SegmentDataVisitor {
    List<Byte> visit(InstructionData instructionData);

    List<Byte> visit(RawData rawData);

    List<Byte> visit(Label label);
}
