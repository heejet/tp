package seedu.clialgo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.clialgo.file.CS2040CFile;
import seedu.clialgo.file.Code;

import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BufferTest {

    private Buffer buffer;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final String path = ".\\export";

    /**
     * Deletes folder at <code>pathToFolder</code> and all the files within.
     * @param pathToFolder The <code>File</code> representing the folder to delete.
     */
    public void deleteAll(File pathToFolder) {
        File[] files = pathToFolder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.delete()) {
                    System.out.println("Delete failed");
                }
            }
        }
        if (!pathToFolder.delete()) {
            System.out.println("Delete failed");
        }
    }

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
        buffer = Buffer.getInstance();
        outputStream.reset();
    }

    @Test
    public void isAddFileToBufferErrorMessagePrinted_expectTrue() {
        ArrayList<CS2040CFile> files = new ArrayList<>();
        CS2040CFile file = new Code("name", "path", "topic");
        files.add(file);
        buffer.updateBuffer(files);
        deleteAll(new File(path));
        try {
            buffer.exportBuffer();
        } catch (HeadlessException e) {
            assert true;
        }

        String os = System.getProperty("os.name");
        String expectedOutput;

        if (os.contains("Windows")) {
            expectedOutput = "======================================================\r\n" +
                    "File missing from root directory.\r\n" +
                    "======================================================\r\n" +
                    "======================================================\r\n" +
                    "The export folder seems to be missing.\r\n" +
                    "The export folder has been recreated.\r\n" +
                    "Try the `export` command again.\r\n" +
                    "======================================================\r\n";
        } else {
            expectedOutput = "======================================================\n" +
                    "File missing from root directory.\n" +
                    "======================================================\n" +
                    "======================================================\n" +
                    "The export folder seems to be missing.\n" +
                    "The export folder has been recreated.\n" +
                    "Try the `export` command again.\n" +
                    "======================================================\n";
        }
        assertEquals(expectedOutput, outputStream.toString());
        deleteAll(new File(path));
    }

    @Test
    public void isEmptyReturnsCorrectly_expectTrue() {
        ArrayList<CS2040CFile> files = new ArrayList<>();
        buffer.updateBuffer(files);
        boolean isEmpty = buffer.isEmpty();
        assertTrue(isEmpty);
        deleteAll(new File(path));
    }
}
