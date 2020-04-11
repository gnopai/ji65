package com.gnopai.ji65.assembler;

import com.gnopai.ji65.parser.statement.TestStatement;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TestData implements SegmentData {
    String testName;
    @Singular
    List<TestStatement> statements;

    @Override
    public int getByteCount() {
        return 0;
    }

    @Override
    public void accept(SegmentDataVisitor visitor) {
        visitor.visit(this);
    }
}
