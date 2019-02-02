package com.gnopai.ji65.parser.statement;

import com.gnopai.ji65.directive.DirectiveType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DirectiveStatement implements Statement {
    DirectiveType type;
}
