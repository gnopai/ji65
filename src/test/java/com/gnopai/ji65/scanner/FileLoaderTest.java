package com.gnopai.ji65.scanner;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileLoaderTest {
    private static final String TEST_FILE = "file-load-sample.txt";
    private final FileLoader testClass = new FileLoader();

    @Test
    void testLoadSourceFile() {
        String expectedText = "This is a sample input file.";
        String fileName = getFullTestFileName();
        Optional<SourceFile> sourceFile = testClass.loadSourceFile(fileName);
        assertTrue(sourceFile.isPresent());
        assertEquals(new SourceFile(fileName, expectedText), sourceFile.get());
    }

    @Test
    void testLoadSourceFile_error() {
        Optional<SourceFile> sourceFile = testClass.loadSourceFile("nope");
        assertFalse(sourceFile.isPresent());
    }

    @Test
    void testLoadBinaryFile() {
        List<Byte> expectedBytes = List.of(
                (byte) 0x54, (byte) 0x68, (byte) 0x69, (byte) 0x73,
                (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
                (byte) 0x61, (byte) 0x20, (byte) 0x73, (byte) 0x61,
                (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x65,
                (byte) 0x20, (byte) 0x69, (byte) 0x6e, (byte) 0x70,
                (byte) 0x75, (byte) 0x74, (byte) 0x20, (byte) 0x66,
                (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x2e
        );

        Optional<List<Byte>> bytes = testClass.loadBinaryFile(getFullTestFileName());
        assertTrue(bytes.isPresent());
        assertEquals(expectedBytes, bytes.get());
    }

    @Test
    void testLoadBinaryFile_error() {
        Optional<List<Byte>> bytes = testClass.loadBinaryFile("nope");
        assertFalse(bytes.isPresent());
    }

    private String getFullTestFileName() {
        URL url = Resources.getResource(TEST_FILE);
        return url.getFile();
    }
}