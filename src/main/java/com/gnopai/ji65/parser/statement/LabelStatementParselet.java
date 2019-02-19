package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.parser.Parser;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;

public class LabelStatementParselet implements StatementParselet {
    @Override
    public Statement parse(Token token, Parser parser) {
        // consume end-of-line if present, but just move on if it's not
        parser.match(TokenType.EOL);

        // current token should be ":", previous token is the identifier
        String labelName = parser.getPrevious().getLexeme();
        return new LabelStatement(labelName);
    }
}
