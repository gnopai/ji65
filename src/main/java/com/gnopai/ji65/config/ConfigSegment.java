package com.gnopai.ji65.config;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.parser.ParseException;
import com.gnopai.ji65.scanner.Token;
import com.gnopai.ji65.scanner.TokenType;
import lombok.Value;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.gnopai.ji65.scanner.TokenType.*;

@Value
class ConfigSegment {
    String name;
    Map<String, Token> values;

    public Optional<Token> getValue(String name) {
        return Optional.ofNullable(values.get(name));
    }

    public Optional<Integer> getIntValue(String name) {
        Optional<Token> token = getValue(name);
        if (!token.isPresent()) {
            return Optional.empty();
        }

        Integer value = token.filter(t -> NUMBER.equals(t.getType()))
                .map(Token::getValue)
                .map(v -> (int) v)
                .orElseThrow(() -> new ParseException(token.get(), "Expected numerical value"));
        return Optional.of(value);
    }

    public Optional<Byte> getByteValue(String name) {
        return getIntValue(name).map(Integer::byteValue);
    }

    public Optional<Address> getAddressValue(String name) {
        return getIntValue(name).map(Address::new);
    }

    public Optional<String> getStringValue(String name) {
        Optional<Token> token = getValue(name);
        if (!token.isPresent()) {
            return Optional.empty();
        }

        TokenType type = token.get().getType();

        if (IDENTIFIER.equals(type)) {
            return Optional.of(token.get().getLexeme());
        }
        if (STRING.equals(type)) {
            return Optional.of((String) token.get().getValue());
        }

        throw new ParseException(token.get(), "Expected string value");
    }

    public <T> Optional<T> getValue(String name, Function<String, Optional<T>> mapper) {
        Optional<Token> token = getValue(name);
        if (!token.isPresent()) {
            return Optional.empty();
        }

        Optional<T> value = mapper.apply(token.get().getLexeme());
        if (!value.isPresent()) {
            throw new ParseException(token.get(), "Invalid value");
        }
        return value;
    }

    public Optional<Boolean> getBooleanValue(String name) {
        return getStringValue(name).map("yes"::equalsIgnoreCase);
    }
}
