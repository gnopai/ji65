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
        InstructionStatement.InstructionStatementBuilder builder = InstructionStatement.builder()
                .instructionType((InstructionType) token.getValue());

        if (parser.match(TokenType.A)) {
            return builder.addressingModeType(AddressingModeType.ACCUMULATOR).build();
        }

        if (parser.match(TokenType.LEFT_PAREN)) {
            return indirect(builder, parser);
        }

        Expression addressExpression = parser.expression();
        builder = builder.addressExpression(addressExpression);

        if (parser.match(TokenType.COMMA)) {
            return indexed(parser, builder);
        }

        return builder.addressingModeType(AddressingModeType.ABSOLUTE).build();
    }

    private InstructionStatement indirect(InstructionStatement.InstructionStatementBuilder builder, Parser parser) {
        Expression addressExpression = parser.expression();
        builder = builder.addressExpression(addressExpression);

        if (parser.match(TokenType.COMMA)) {
            parser.consume(TokenType.X, "Expected X for index");
            parser.consume(TokenType.RIGHT_PAREN, "Expected closing parenthesis for indirect address");
            return builder.addressingModeType(AddressingModeType.INDEXED_INDIRECT).build();
        }

        parser.consume(TokenType.RIGHT_PAREN, "Expected closing parenthesis for indirect address");
        if (parser.match(TokenType.COMMA)) {
            parser.consume(TokenType.Y, "Expected Y for index");
            return builder.addressingModeType(AddressingModeType.INDIRECT_INDEXED).build();
        }

        return builder.addressingModeType(AddressingModeType.INDIRECT).build();
    }

    private Statement indexed(Parser parser, InstructionStatement.InstructionStatementBuilder builder) {
        Token register = parser.consume();
        switch (register.getType()) {
            case X:
                return builder.addressingModeType(AddressingModeType.ABSOLUTE_X).build();
            case Y:
                return builder.addressingModeType(AddressingModeType.ABSOLUTE_Y).build();
            default:
                throw parser.error("Expected X or Y for index");
        }
    }
}
