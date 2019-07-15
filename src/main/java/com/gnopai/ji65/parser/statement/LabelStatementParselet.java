package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class LabelStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        String labelName = token.getLexeme();
        parser.consume(TokenType.COLON, "Expected colon after label");

        // consume end-of-line if present, but just move on if it's not
        parser.match(TokenType.EOL);

        return labelName.startsWith("@") ? new LocalLabelStatement(labelName) : new LabelStatement(labelName);
    }
}
