package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.util.TypeFactory;

import java.util.List;

public class AddressingModeFactory extends TypeFactory<AddressingModeType, AddressingMode> {
    public AddressingModeFactory() {
        super(AddressingMode::getType, List.of(
                new ImmediateAddressingMode(),
                new ZeroPageAddressingMode(),
                new ZeroPageXAddressingMode()
        ));
    }
}
