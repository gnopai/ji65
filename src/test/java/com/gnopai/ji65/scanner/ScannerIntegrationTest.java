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
        SourceFile sourceFile = new SourceFile(null, sourceText);
        List<Token> tokens = new Scanner(new TokenReader(new ErrorPrinter())).scan(sourceFile);

        assertEquals(List.of(
                new Token(DIRECTIVE, ".segment", DirectiveType.SEGMENT, 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'Z', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'E', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'R', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'O', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'P', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'A', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'G', 1, sourceFile),
                new Token(CHAR, "\"ZEROPAGE\"", (int) 'E', 1, sourceFile),
                new Token(EOL, "\n", null, 1, sourceFile),
                new Token(IDENTIFIER, "NPC_MAX", null, 2, sourceFile),
                new Token(EQUAL, "=", null, 2, sourceFile),
                new Token(NUMBER, "8", 8, 2, sourceFile),
                new Token(EOL, "\n", null, 2, sourceFile),
                new Token(IDENTIFIER, "NPC_OAM_SIZE", null, 3, sourceFile),
                new Token(EQUAL, "=", null, 3, sourceFile),
                new Token(NUMBER, "16", 16, 3, sourceFile),
                new Token(EOL, "\n", null, 3, sourceFile),
                new Token(EOL, "\n", null, 4, sourceFile),

                new Token(DIRECTIVE, ".macro", DirectiveType.MACRO, 5, sourceFile),
                new Token(IDENTIFIER, "ldx_npc_offset", null, 5, sourceFile),
                new Token(EOL, "\n", null, 5, sourceFile),
                new Token(INSTRUCTION, "ldy", InstructionType.LDY, 6, sourceFile),
                new Token(IDENTIFIER, "npc_index", null, 6, sourceFile),
                new Token(EOL, "\n", null, 6, sourceFile),
                new Token(INSTRUCTION, "ldx", InstructionType.LDX, 7, sourceFile),
                new Token(IDENTIFIER, "npc_indexes", null, 7, sourceFile),
                new Token(COMMA, ",", null, 7, sourceFile),
                new Token(Y, "Y", null, 7, sourceFile),
                new Token(EOL, "\n", null, 7, sourceFile),

                new Token(DIRECTIVE, ".endmacro", DirectiveType.MACRO_END, 8, sourceFile),
                new Token(EOL, "\n", null, 8, sourceFile),
                new Token(EOL, "\n", null, 9, sourceFile),
                new Token(EOL, "\n", null, 10, sourceFile),
                new Token(IDENTIFIER, "npcs", null, 11, sourceFile),
                new Token(COLON, ":", null, 11, sourceFile),
                new Token(DIRECTIVE, ".res", DirectiveType.RESERVE, 11, sourceFile),
                new Token(IDENTIFIER, "NPC_TOTAL_BYTES", null, 11, sourceFile),
                new Token(EOL, "\n", null, 11, sourceFile),
                new Token(EOL, "\n", null, 12, sourceFile),

                new Token(IDENTIFIER, "npc_check_for_unbuffering", null, 13, sourceFile),
                new Token(COLON, ":", null, 13, sourceFile),
                new Token(EOL, "\n", null, 13, sourceFile),
                new Token(EOL, "\n", null, 14, sourceFile),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 15, sourceFile),
                new Token(POUND, "#", null, 15, sourceFile),
                new Token(NUMBER, "0", 0, 15, sourceFile),
                new Token(EOL, "\n", null, 15, sourceFile),
                new Token(INSTRUCTION, "sta", InstructionType.STA, 16, sourceFile),
                new Token(IDENTIFIER, "npc_index", null, 16, sourceFile),
                new Token(EOL, "\n", null, 16, sourceFile),
                new Token(IDENTIFIER, "@loop", null, 17, sourceFile),
                new Token(COLON, ":", null, 17, sourceFile),
                new Token(EOL, "\n", null, 17, sourceFile),
                new Token(IDENTIFIER, "ldx_npc_offset", null, 18, sourceFile),
                new Token(EOL, "\n", null, 18, sourceFile),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 19, sourceFile),
                new Token(IDENTIFIER, "npcs", null, 19, sourceFile),
                new Token(PLUS, "+", null, 19, sourceFile),
                new Token(IDENTIFIER, "NPC_STATUS", null, 19, sourceFile),
                new Token(COMMA, ",", null, 19, sourceFile),
                new Token(X, "X", null, 19, sourceFile),
                new Token(EOL, "\n", null, 19, sourceFile),
                new Token(INSTRUCTION, "and", InstructionType.AND, 20, sourceFile),
                new Token(POUND, "#", null, 20, sourceFile),
                new Token(LEFT_PAREN, "(", null, 20, sourceFile),
                new Token(IDENTIFIER, "NPC_STATUS_ACTIVE", null, 20, sourceFile),
                new Token(PIPE, "|", null, 20, sourceFile),
                new Token(IDENTIFIER, "NPC_STATUS_ONSCREEN", null, 20, sourceFile),
                new Token(RIGHT_PAREN, ")", null, 20, sourceFile),
                new Token(EOL, "\n", null, 20, sourceFile),
                new Token(INSTRUCTION, "cmp", InstructionType.CMP, 21, sourceFile),
                new Token(POUND, "#", null, 21, sourceFile),
                new Token(IDENTIFIER, "NPC_STATUS_ACTIVE", null, 21, sourceFile),
                new Token(EOL, "\n", null, 21, sourceFile),
                new Token(INSTRUCTION, "bne", InstructionType.BNE, 22, sourceFile),

                new Token(IDENTIFIER, "@next", null, 22, sourceFile),
                new Token(EOL, "\n", null, 22, sourceFile),
                new Token(EOL, "\n", null, 23, sourceFile),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 24, sourceFile),
                new Token(IDENTIFIER, "npcs", null, 24, sourceFile),
                new Token(PLUS, "+", null, 24, sourceFile),
                new Token(IDENTIFIER, "NPC_X1", null, 24, sourceFile),
                new Token(COMMA, ",", null, 24, sourceFile),
                new Token(X, "X", null, 24, sourceFile),
                new Token(EOL, "\n", null, 24, sourceFile),
                new Token(INSTRUCTION, "and", InstructionType.AND, 25, sourceFile),
                new Token(POUND, "#", null, 25, sourceFile),
                new Token(NUMBER, "$F0", 240, 25, sourceFile),
                new Token(EOL, "\n", null, 25, sourceFile),
                new Token(INSTRUCTION, "eor", InstructionType.EOR, 26, sourceFile),
                new Token(IDENTIFIER, "npc_offscreen_column", null, 26, sourceFile),
                new Token(PLUS, "+", null, 26, sourceFile),
                new Token(NUMBER, "0", 0, 26, sourceFile),
                new Token(EOL, "\n", null, 26, sourceFile),
                new Token(INSTRUCTION, "bne", InstructionType.BNE, 27, sourceFile),
                new Token(IDENTIFIER, "@next", null, 27, sourceFile),
                new Token(EOL, "\n", null, 27, sourceFile),
                new Token(INSTRUCTION, "lda", InstructionType.LDA, 28, sourceFile),
                new Token(IDENTIFIER, "npcs", null, 28, sourceFile),
                new Token(PLUS, "+", null, 28, sourceFile),
                new Token(IDENTIFIER, "NPC_X2", null, 28, sourceFile),
                new Token(COMMA, ",", null, 28, sourceFile),
                new Token(X, "X", null, 28, sourceFile),
                new Token(EOL, "\n", null, 28, sourceFile),
                new Token(INSTRUCTION, "and", InstructionType.AND, 29, sourceFile),
                new Token(POUND, "#", null, 29, sourceFile),
                new Token(NUMBER, "$01", 1, 29, sourceFile),
                new Token(EOL, "\n", null, 29, sourceFile),
                new Token(INSTRUCTION, "eor", InstructionType.EOR, 30, sourceFile),
                new Token(IDENTIFIER, "npc_offscreen_column", null, 30, sourceFile),
                new Token(PLUS, "+", null, 30, sourceFile),
                new Token(NUMBER, "1", 1, 30, sourceFile),
                new Token(EOL, "\n", null, 30, sourceFile),
                new Token(INSTRUCTION, "bne", InstructionType.BNE, 31, sourceFile),
                new Token(IDENTIFIER, "@next", null, 31, sourceFile),
                new Token(EOL, "\n", null, 31, sourceFile),
                new Token(INSTRUCTION, "jsr", InstructionType.JSR, 32, sourceFile),
                new Token(IDENTIFIER, "npc_unload", null, 32, sourceFile),
                new Token(EOF, "", null, 32, sourceFile)
        ), tokens);
    }

    private String readSourceText(String fileName) throws IOException {
        URL url = Resources.getResource(fileName);
        return Resources.toString(url, UTF_8);
    }
}
