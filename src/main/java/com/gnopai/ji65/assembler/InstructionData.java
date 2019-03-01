package com.gnopai.ji65.assembler;

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
    public int getByteCount() {
        return opcode.getByteCount();
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
