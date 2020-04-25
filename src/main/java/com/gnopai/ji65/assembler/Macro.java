package com.gnopai.ji65.assembler;

import com.gnopai.ji65.scanner.Token;
import lombok.Value;

import java.util.List;

@Value
public class Macro {
    String name;
    List<Token> tokens;
    List<String> argumentNames;

    public int getArgumentCount() {
        return argumentNames.size();
    }

    public String getArgumentName(int i) {
        return argumentNames.get(i);
    }
}
