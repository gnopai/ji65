package com.gnopai.ji65.assembler;

import com.gnopai.ji65.Address;
import com.gnopai.ji65.linker.UnnamedLabels;
import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(exclude = "currentScope")
@ToString(exclude = "currentScope")
public class Environment {
    private final Scope rootScope;
    private Scope currentScope;
    private final UnnamedLabels unnamedLabels;
    private int currentAddress;

    public Environment() {
        rootScope = new Scope();
        currentScope = rootScope;
        unnamedLabels = new UnnamedLabels();
        currentAddress = 0;
    }

    public Environment enterChildScope(String scopeName) {
        currentScope = currentScope.getChildScope(scopeName);
        return this;
    }

    public Environment goToRootScope() {
        currentScope = rootScope;
        return this;
    }

    public Optional<Expression> get(String name) {
        if ("*".equals(name)) {
            // magic program-counter var
            return Optional.of(getCurrentAddressExpression());
        }
        return currentScope.get(name);
    }

    public Label getLabel(String name) {
        return currentScope.getLabel(name);
    }

    public void define(String name, Expression value) {
        currentScope.define(name, value);
    }

    public void define(String name, int value) {
        currentScope.define(name, value);
    }

    public void defineGlobal(String name, Expression value) {
        rootScope.define(name, value);
    }

    public void defineGlobal(String name, int value) {
        rootScope.define(name, value);
    }

    public void defineUnnamedLabel(int value) {
        unnamedLabels.add(value);
    }

    public int getUnnamedLabel(Address address, int offset) {
        return unnamedLabels.getFromAddress(address, offset)
                .orElseThrow(() -> new RuntimeException("Invalid unnamed label reference from address " + address + " and offset " + offset));
    }

    public List<Integer> getUnnamedLabels() {
        return unnamedLabels.getAll();
    }

    public int getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(int currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Expression getCurrentAddressExpression() {
        return new PrimaryExpression(TokenType.NUMBER, currentAddress);
    }

    @EqualsAndHashCode(exclude = "parent")
    @ToString(exclude = "parent")
    private static class Scope {
        private final Map<String, Expression> values = new HashMap<>();
        private final Map<String, Scope> children = new HashMap<>();
        private final Scope parent;

        public Scope() {
            this(null);
        }

        public Scope(Scope parent) {
            this.parent = parent;
        }

        public Optional<Expression> get(String name) {
            return Optional.ofNullable(values.get(name))
                    .or(() -> getFromParent(name));
        }

        private Optional<Expression> getFromParent(String name) {
            return Optional.ofNullable(parent)
                    .flatMap(environment -> environment.get(name));
        }

        public Label getLabel(String name) {
            return get(name)
                    .filter(l -> l instanceof Label)
                    .map(l -> (Label) l)
                    .orElseThrow(() -> new RuntimeException("Expected label named \"" + name + "\""));
        }

        public void define(String name, Expression value) {
            values.put(name, value);
        }

        public void define(String name, int value) {
            values.put(name, new PrimaryExpression(TokenType.NUMBER, value));
        }

        public Scope getChildScope(String scope) {
            return children.computeIfAbsent(scope, s -> new Scope(this));
        }
    }
}
