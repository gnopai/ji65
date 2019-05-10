package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.expression.Expression;
import com.gnopai.ji65.parser.expression.PrimaryExpression;
import com.gnopai.ji65.scanner.TokenType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(exclude = "currentScope")
@ToString(exclude = "currentScope")
public class Environment {
    private final Scope rootScope;
    private Scope currentScope;

    public Environment() {
        rootScope = new Scope();
        currentScope = rootScope;
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

    public void undefine(String name) {
        currentScope.undefine(name);
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

        public void undefine(String name) {
            values.remove(name);
        }

        public Scope getChildScope(String scope) {
            return children.computeIfAbsent(scope, s -> new Scope(this));
        }
    }
}
