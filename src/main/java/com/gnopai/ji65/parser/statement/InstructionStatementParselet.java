package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.address.AddressingModeType;
import com.gnopai.ji65.instruction.InstructionType;
import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class InstructionStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        InstructionType instructionType = (InstructionType) token.getValue();
        InstructionStatement.InstructionStatementBuilder builder = InstructionStatement.builder().instructionType(instructionType);

        if (parser.match(TokenType.EOL)) {
            return builder.addressingModeType(AddressingModeType.IMPLICIT).build();
        }

        if (parser.match(TokenType.A)) {
            parser.consume(TokenType.EOL, "Expected end of line");
            return builder.addressingModeType(AddressingModeType.ACCUMULATOR).build();
        }

        if (parser.match(TokenType.LEFT_PAREN)) {
            return indirect(builder, parser);
        }

        if (parser.match(TokenType.POUND)) {
            return immediate(builder, parser);
        }

        Expression addressExpression = parser.expression();
        builder = builder.addressExpression(addressExpression);

        if (parser.match(TokenType.COMMA)) {
            return indexed(parser, builder);
        }

        parser.consume(TokenType.EOL, "Expected end of line");
        AddressingModeType addressingModeType = instructionType.isBranchInstruction() ? AddressingModeType.RELATIVE : AddressingModeType.ABSOLUTE;
        return builder.addressingModeType(addressingModeType).build();
    }

    private InstructionStatement indirect(InstructionStatement.InstructionStatementBuilder builder, Parser parser) {
        Expression addressExpression = parser.expression();
        builder = builder.addressExpression(addressExpression);

        if (parser.match(TokenType.COMMA)) {
            parser.consume(TokenType.X, "Expected X for index");
            parser.consume(TokenType.RIGHT_PAREN, "Expected closing parenthesis for indirect address");
            parser.consume(TokenType.EOL, "Expected end of line");
            return builder.addressingModeType(AddressingModeType.INDEXED_INDIRECT).build();
        }

        parser.consume(TokenType.RIGHT_PAREN, "Expected closing parenthesis for indirect address");
        if (parser.match(TokenType.COMMA)) {
            parser.consume(TokenType.Y, "Expected Y for index");
            parser.consume(TokenType.EOL, "Expected end of line");
            return builder.addressingModeType(AddressingModeType.INDIRECT_INDEXED).build();
        }

        parser.consume(TokenType.EOL, "Expected end of line");
        return builder.addressingModeType(AddressingModeType.INDIRECT).build();
    }

    private Statement immediate(InstructionStatement.InstructionStatementBuilder builder, Parser parser) {
        Expression addressExpression = parser.expression();
        parser.consume(TokenType.EOL, "Expected end of line");
        return builder
                .addressingModeType(AddressingModeType.IMMEDIATE)
                .addressExpression(addressExpression)
                .build();
    }

    private Statement indexed(Parser parser, InstructionStatement.InstructionStatementBuilder builder) {
        Token register = parser.consume();
        switch (register.getType()) {
            case X:
                parser.consume(TokenType.EOL, "Expected end of line");
                return builder.addressingModeType(AddressingModeType.ABSOLUTE_X).build();
            case Y:
                parser.consume(TokenType.EOL, "Expected end of line");
                return builder.addressingModeType(AddressingModeType.ABSOLUTE_Y).build();
            default:
                throw parser.error("Expected X or Y for index");
        }
    }
}
