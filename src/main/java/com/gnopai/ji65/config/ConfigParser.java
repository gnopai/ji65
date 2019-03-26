package com.gnopai.ji65.config;

import com.gnopai.ji65.parser.TokenConsumer;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gnopai.ji65.scanner.TokenType.*;
import static java.util.stream.Collectors.toMap;

public class ConfigParser {
    private final TokenConsumer tokenConsumer;

    public ConfigParser(TokenConsumer tokenConsumer) {
        this.tokenConsumer = tokenConsumer;
    }

    public List<ConfigBlock> parse() {
        List<ConfigBlock> blocks = new ArrayList<>();
        while (tokenConsumer.hasMore()) {
            blocks.add(parseBlock());
        }
        return blocks;
    }

    private ConfigBlock parseBlock() {
        Token identifier = tokenConsumer.consume(TokenType.IDENTIFIER, "Expected block identifier");
        tokenConsumer.consume(LEFT_BRACE, "Expected block starting brace");
        List<ConfigSegment> allSegments = new ArrayList<>();
        while (!tokenConsumer.match(RIGHT_BRACE)) {
            allSegments.add(parseSegment());
        }
        return new ConfigBlock(identifier.getLexeme(), allSegments);
    }

    private ConfigSegment parseSegment() {
        Token identifier = tokenConsumer.consume(IDENTIFIER, "Expected segment identifier");
        tokenConsumer.consume(COLON, "Expected colon");

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(parseSingleAttribute());
        while (!tokenConsumer.match(SEMICOLON)) {
            tokenConsumer.match(COMMA); // optional comma between attributes
            attributes.add(parseSingleAttribute());
        }

        Map<String, Token> attributeMap = attributes.stream()
                .collect(toMap(Attribute::getIdentifier, Attribute::getValue));
        return new ConfigSegment(identifier.getLexeme(), attributeMap);
    }

    private Attribute parseSingleAttribute() {
        Token identifier = tokenConsumer.consume(IDENTIFIER, "Expected attribute identifier");
        tokenConsumer.consume(EQUAL, "Expected equal sign");
        Token value = consumeAttributeValue();
        return new Attribute(identifier.getLexeme(), value);
    }

    private Token consumeAttributeValue() {
        Token value = tokenConsumer.consume();
        switch (value.getType()) {
            case NUMBER:
            case STRING:
            case IDENTIFIER:
                return value;
            default:
                throw tokenConsumer.error("Invalid attribute value");
        }
    }

    @Value
    private static class Attribute {
        String identifier;
        Token value;
    }
}
