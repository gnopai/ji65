package com.gnopai.ji65.config;

import com.gnopai.ji65.parser.TokenStream;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gnopai.ji65.scanner.TokenType.*;
import static java.util.stream.Collectors.toMap;

public class ConfigParser {
    private final TokenStream tokenStream;

    public ConfigParser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    public List<ConfigBlock> parse() {
        List<ConfigBlock> blocks = new ArrayList<>();
        while (tokenStream.hasMore()) {
            blocks.add(parseBlock());
        }
        return blocks;
    }

    private ConfigBlock parseBlock() {
        Token identifier = tokenStream.consume(TokenType.IDENTIFIER, "Expected block identifier");
        tokenStream.consume(LEFT_BRACE, "Expected block starting brace");
        List<ConfigSegment> allSegments = new ArrayList<>();
        while (!tokenStream.match(RIGHT_BRACE)) {
            allSegments.add(parseSegment());
        }
        return new ConfigBlock(identifier.getLexeme(), allSegments);
    }

    private ConfigSegment parseSegment() {
        Token identifier = tokenStream.consume(IDENTIFIER, "Expected segment identifier");
        tokenStream.consume(COLON, "Expected colon");

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(parseSingleAttribute());
        while (!tokenStream.match(SEMICOLON)) {
            tokenStream.match(COMMA); // optional comma between attributes
            attributes.add(parseSingleAttribute());
        }

        Map<String, Token> attributeMap = attributes.stream()
                .collect(toMap(Attribute::getIdentifier, Attribute::getValue));
        return new ConfigSegment(identifier.getLexeme(), attributeMap);
    }

    private Attribute parseSingleAttribute() {
        Token identifier = tokenStream.consume(IDENTIFIER, "Expected attribute identifier");
        tokenStream.consume(EQUAL, "Expected equal sign");
        Token value = consumeAttributeValue();
        return new Attribute(identifier.getLexeme(), value);
    }

    private Token consumeAttributeValue() {
        Token value = tokenStream.consume();
        switch (value.getType()) {
            case NUMBER:
            case STRING:
            case IDENTIFIER:
                return value;
            default:
                throw tokenStream.error("Invalid attribute value");
        }
    }

    @Value
    private static class Attribute {
        String identifier;
        Token value;
    }
}
