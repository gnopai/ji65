package com.gnopai.ji65.scanner;

import com.gnopai.ji65.DirectiveType;
import com.gnopai.ji65.InstructionType;
import com.gnopai.ji65.util.ErrorPrinter;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.gnopai.ji65.scanner.TokenType.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScannerIntegrationTest {
    @Test
    void test() throws Exception {
        String sourceText = readSourceText("scanner_sample.txt");
        List<Token> tokens = new Scanner(new TokenReader(new ErrorPrinter())).scan(sourceText);

        assertEquals(List.of(
                new Token(DIRECTIVE, ".segment", DirectiveType.SEGMENT, 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'Z', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'E', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'R', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'O', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'P', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'A', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'G', 1),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'E', 1),
                new Token(EOL, "\n", null, 1),
                new Token(IDENTIFIER, "NPC_MAX", null, 2),
                new Token(EQUAL, "=", null, 2),
                new Token(NUMBER, "8", 8, 2),
                new Token(EOL, "\n", null, 2),
                new Token(IDENTIFIER, "NPC_OAM_SIZE", null, 3),
                new Token(EQUAL, "=", null, 3),
                new Token(NUMBER, "16", 16, 3),
                new Token(EOL, "\n", null, 3),
                new Token(EOL, "\n", null, 4),

                new Token(DIRECTIVE, ".macro", DirectiveType.MACRO_START, 5),
                new Token(IDENTIFIER, "ldx_npc_offset", null, 5),
                new Token(EOL, "\n", null, 5),
                new Token(INSTRUCTION, "ldy", InstructionType.LDY, 6),
                new Token(IDENTIFIER, "npc_index", null, 6),
                new Token(EOL, "\n", null, 6),
                new Token(INSTRUCTION, "ldx", InstructionType.LDX, 7),
                new Token(IDENTIFIER, "npc_indexes", null, 7),
                new Token(COMMA, ",", null, 7),
                new Token(Y, "Y", null, 7),
                new Token(EOL, "\n", null, 7),

                new Token(DIRECTIVE, ".endmacro", DirectiveType.MACRO_END, 8),
                new Token(EOL, "\n", null, 8),
                new Token(EOL, "\n", null, 9),
                new Token(EOL, "\n", null, 10),
                new Token(IDENTIFIER, "npcs", null, 11),
                new Token(COLON, ":", null, 11),
                new Token(DIRECTIVE, ".res", DirectiveType.RESERVE, 11),
                new Token(IDENTIFIER, "NPC_TOTAL_BYTES", null, 11),
                new Token(EOL, "\n", null, 11),
                new Token(EOL, "\n", null, 12),

                new Token(IDENTIFIER, "npc_check_for_unbuffering", null, 13),
                new Token(COLON, ":", null, 13),
                new Token(EOL, "\n", null, 13),
                new Token(EOL, "\n", null, 14),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 15),
                new Token(POUND, "#", null, 15),
                new Token(NUMBER, "0", 0, 15),
                new Token(EOL, "\n", null, 15),
                new Token(INSTRUCTION, "sta", InstructionType.STA, 16),
                new Token(IDENTIFIER, "npc_index", null, 16),
                new Token(EOL, "\n", null, 16),
                new Token(AT_SIGN, "@", null, 17),
                new Token(IDENTIFIER, "loop", null, 17),
                new Token(COLON, ":", null, 17),
                new Token(EOL, "\n", null, 17),
                new Token(IDENTIFIER, "ldx_npc_offset", null, 18),
                new Token(EOL, "\n", null, 18),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 19),
                new Token(IDENTIFIER, "npcs", null, 19),
                new Token(PLUS, "+", null, 19),
                new Token(IDENTIFIER, "NPC_STATUS", null, 19),
                new Token(COMMA, ",", null, 19),
                new Token(X, "X", null, 19),
                new Token(EOL, "\n", null, 19),
                new Token(INSTRUCTION, "and", InstructionType.AND, 20),
                new Token(POUND, "#", null, 20),
                new Token(LEFT_PAREN, "(", null, 20),
                new Token(IDENTIFIER, "NPC_STATUS_ACTIVE", null, 20),
                new Token(PIPE, "|", null, 20),
                new Token(IDENTIFIER, "NPC_STATUS_ONSCREEN", null, 20),
                new Token(RIGHT_PAREN, ")", null, 20),
                new Token(EOL, "\n", null, 20),
                new Token(INSTRUCTION, "cmp", InstructionType.CMP, 21),
                new Token(POUND, "#", null, 21),
                new Token(IDENTIFIER, "NPC_STATUS_ACTIVE", null, 21),
                new Token(EOL, "\n", null, 21),
                new Token(INSTRUCTION, "bne", InstructionType.BNE, 22),

                new Token(AT_SIGN, "@", null, 22),
                new Token(IDENTIFIER, "next", null, 22),
                new Token(EOL, "\n", null, 22),
                new Token(EOL, "\n", null, 23),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 24),
                new Token(IDENTIFIER, "npcs", null, 24),
                new Token(PLUS, "+", null, 24),
                new Token(IDENTIFIER, "NPC_X1", null, 24),
                new Token(COMMA, ",", null, 24),
                new Token(X, "X", null, 24),
                new Token(EOL, "\n", null, 24),
                new Token(INSTRUCTION, "and", InstructionType.AND, 25),
                new Token(POUND, "#", null, 25),
                new Token(NUMBER, "$F0", 240, 25),
                new Token(EOL, "\n", null, 25),
                new Token(INSTRUCTION, "eor", InstructionType.EOR, 26),
                new Token(IDENTIFIER, "npc_offscreen_column", null, 26),
                new Token(PLUS, "+", null, 26),
                new Token(NUMBER, "0", 0, 26),
                new Token(EOL, "\n", null, 26),
                new Token(INSTRUCTION, "bne", InstructionType.BNE, 27),
                new Token(AT_SIGN, "@", null, 27),
                new Token(IDENTIFIER, "next", null, 27),
                new Token(EOL, "\n", null, 27),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 28),
                new Token(IDENTIFIER, "npcs", null, 28),
                new Token(PLUS, "+", null, 28),
                new Token(IDENTIFIER, "NPC_X2", null, 28),
                new Token(COMMA, ",", null, 28),
                new Token(X, "X", null, 28),
                new Token(EOL, "\n", null, 28),
                new Token(INSTRUCTION, "and", InstructionType.AND, 29),
                new Token(POUND, "#", null, 29),
                new Token(NUMBER, "$01", 1, 29),
                new Token(EOL, "\n", null, 29),
                new Token(INSTRUCTION, "eor", InstructionType.EOR, 30),
                new Token(IDENTIFIER, "npc_offscreen_column", null, 30),
                new Token(PLUS, "+", null, 30),
                new Token(NUMBER, "1", 1, 30),
                new Token(EOL, "\n", null, 30),
                new Token(INSTRUCTION, "bne", InstructionType.BNE, 31),
                new Token(AT_SIGN, "@", null, 31),
                new Token(IDENTIFIER, "next", null, 31),
                new Token(EOL, "\n", null, 31),
                new Token(INSTRUCTION, "jsr", InstructionType.JSR, 32),
                new Token(IDENTIFIER, "npc_unload", null, 32),
                new Token(EOF, "", null, 32)
        ), tokens);
    }

    private String readSourceText(String fileName) throws IOException {
        URL url = Resources.getResource(fileName);
        return Resources.toString(url, UTF_8);
    }
}
