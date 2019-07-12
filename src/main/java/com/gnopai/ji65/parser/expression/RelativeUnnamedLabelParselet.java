package com.gnopai.ji65.parser.expression;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class RelativeUnnamedLabelParselet implements PrefixParselet {
    @Override
    public Expression parse(Token token, Parser parser) {
        int offset = determineOffset(parser);
        return new RelativeUnnamedLabelExpression(offset);
    }

    private int determineOffset(Parser parser) {
        int offset = 0;

        while (parser.match(TokenType.PLUS)) {
            offset++;
        }
        if (offset > 0) {
            return offset;
        }

        while (parser.match(TokenType.MINUS)) {
            offset--;
        }
        if (offset < 0) {
            return offset;
        }

        throw parser.error("Expected plus or minus for referencing unnamed label");
    }
}
