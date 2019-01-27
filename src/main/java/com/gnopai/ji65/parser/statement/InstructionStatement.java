package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.address.AddressingModeType;
import com.gnopai.ji65.instruction.InstructionType;
import com.gnopai.ji65.parser.expression.Expression;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InstructionStatement implements Statement {
    InstructionType instructionType;
    Expression addressExpression;
    AddressingModeType addressingModeType;
}
