package com.gnopai.ji65;

import com.gnopai.ji65.test.AssertionResult;
import com.gnopai.ji65.test.TestResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gnopai.ji65.TestUtil.assembleAndRunTests;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestingFunctionalTest {

    @Test
    void testSimpleSetAndAssert_resultsPass() {
        List<TestResult> results = assembleAndRunTests(
                ".test whee",
                ".testset A $44",
                ".assert A $44",
                ".endtest"
        );

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("whee")
                        .assertionResult(new AssertionResult(true, (byte) 0x44, (byte) 0x44))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testSimpleSetAndAssert_resultsFail() {
        List<TestResult> results = assembleAndRunTests(
                ".test whee",
                ".testset A $99",
                ".assert A $44",
                ".endtest"
        );

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("whee")
                        .assertionResult(new AssertionResult(false, (byte) 0x44, (byte) 0x99))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testSimpleSetAndAssert_multipleAssertions() {
        List<TestResult> results = assembleAndRunTests(
                ".test whee",
                ".testset A $44",
                ".testset X $55",
                ".assert A $44",
                ".assert X $55",
                ".assert Y 0",
                ".endtest"
        );

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("whee")
                        .assertionResult(new AssertionResult(true, (byte) 0x44, (byte) 0x44))
                        .assertionResult(new AssertionResult(true, (byte) 0x55, (byte) 0x55))
                        .assertionResult(new AssertionResult(true, (byte) 0, (byte) 0))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testSubroutineTest_resultsPass() {
        List<TestResult> results = assembleAndRunTests(
                "sub_to_test:",
                "lda #$55",
                "rts",
                "",
                ".test whee",
                ".testrun sub_to_test",
                ".assert A $55",
                ".endtest"
        );

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("whee")
                        .assertionResult(new AssertionResult(true, (byte) 0x55, (byte) 0x55))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testSubroutineTest_resultsFail() {
        List<TestResult> results = assembleAndRunTests(
                "sub_to_test:",
                "lda #$88",
                "rts",
                "",
                ".test whee",
                ".testrun sub_to_test",
                ".assert A $55",
                ".endtest"
        );

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("whee")
                        .assertionResult(new AssertionResult(false, (byte) 0x55, (byte) 0x88))
                        .build()
        );
        assertEquals(expectedResults, results);
    }

    @Test
    void testMultipleTestsWithMixedResults() {
        List<TestResult> results = assembleAndRunTests(
                "sub_to_test:",
                "tax",
                "rts",
                "",
                ".test whee1",
                ".testset A $88",
                ".testrun sub_to_test",
                ".assert X $88",
                ".endtest",
                "",
                ".test whee2",
                ".testset A $55",
                ".testrun sub_to_test",
                ".assert X $88",
                ".endtest",
                "",
                ".test whee3",
                ".testset A $44",
                ".assert A $44",
                ".assert X 0",
                ".endtest"
        );

        List<TestResult> expectedResults = List.of(
                TestResult.builder()
                        .name("whee1")
                        .assertionResult(new AssertionResult(true, (byte) 0x88, (byte) 0x88))
                        .build(),
                TestResult.builder()
                        .name("whee2")
                        .assertionResult(new AssertionResult(false, (byte) 0x88, (byte) 0x55))
                        .build(),
                TestResult.builder()
                        .name("whee3")
                        .assertionResult(new AssertionResult(true, (byte) 0x44, (byte) 0x44))
                        .assertionResult(new AssertionResult(true, (byte) 0, (byte) 0))
                        .build()
        );
        assertEquals(expectedResults, results);
    }
}
