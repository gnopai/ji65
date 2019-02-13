package com.gnopai.ji65.compiler;

import com.gnopai.ji65.Opcode;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class InstructionData implements SegmentData {
    Opcode opcode;
    SegmentData operand;

    public InstructionData(Opcode opcode, Byte... bytes) {
        this(opcode, new RawData(List.of(bytes)));
    }

    @Override
    public List<Byte> accept(SegmentDataVisitor visitor) {
        return visitor.visit(this);
    }
}
