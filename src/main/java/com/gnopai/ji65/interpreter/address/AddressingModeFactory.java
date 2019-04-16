package com.gnopai.ji65.interpreter.address;

import com.gnopai.ji65.AddressingModeType;
import com.gnopai.ji65.util.TypeFactory;

import java.util.List;

public class AddressingModeFactory extends TypeFactory<AddressingModeType, AddressingMode> {
    public AddressingModeFactory() {
        super(AddressingModeType.class, AddressingMode::getType, List.of(
                new AccumulatorAddressingMode(),
                new ImplicitAddressingMode(),
                new ImmediateAddressingMode(),
                new ZeroPageAddressingMode(),
                new ZeroPageXAddressingMode(),
                new ZeroPageYAddressingMode(),
                new AbsoluteAddressingMode(),
                new AbsoluteXAddressingMode(),
                new AbsoluteYAddressingMode(),
                new IndirectAddressingMode(),
                new IndexedIndirectAddressingMode(),
                new IndirectIndexedAddressingMode()
        ));
    }
}
