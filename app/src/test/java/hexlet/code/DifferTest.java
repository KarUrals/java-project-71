package hexlet.code;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static hexlet.code.Formatter.JSON;
import static hexlet.code.Formatter.PLAIN;
import static hexlet.code.Formatter.STYLISH;
import static hexlet.code.Formatter.IDENTICAL_FILES_MESSAGE;
import static hexlet.code.Formatter.EMPTY_FILES_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DifferTest {

    private static String expectedPlain;
    private static String expectedStylish;
    private static String expectedJson;
    private final String nestedJsonFilePath1 = "./src/test/resources/nestedFile1.json";
    private final String nestedJsonFilePath2 = "./src/test/resources/nestedFile2.json";
    private final String nestedYmlFilePath1 = "./src/test/resources/nestedFile1.yml";
    private final String nestedYmlFilePath2 = "./src/test/resources/nestedFile2.yml";
    private final String emptyYmlFilePath = "./src/test/resources/expected/emptyFile.yml";
    private final String emptyJsonFilePath = "./src/test/resources/expected/emptyFile.json";

    @BeforeAll
    static void beforeAll() throws IOException {
        expectedPlain = Files.readString(Paths.get("./src/test/resources/expected/plain.txt"));
        expectedStylish = Files.readString(Paths.get("./src/test/resources/expected/nestedStylish"));
        expectedJson = Files.readString(Paths.get("./src/test/resources/expected/json.json"));
    }

    @Test
    void defaultStyleTest() throws Exception {
        String actualJson = Differ.generate(nestedJsonFilePath1, nestedJsonFilePath2);
        assertEquals(actualJson, expectedStylish);

        String actualYml = Differ.generate(nestedYmlFilePath1, nestedYmlFilePath2);
        assertEquals(actualYml, expectedStylish);
    }

    @Test
    void jsonStyleTest() throws Exception {
        String actualJson = Differ.generate(nestedJsonFilePath1, nestedJsonFilePath2, JSON);
        assertEquals(actualJson, expectedJson);

        String actualYml = Differ.generate(nestedYmlFilePath1, nestedYmlFilePath2, JSON);
        assertEquals(actualYml, expectedJson);

        String compareSameFiles = Differ.generate(nestedJsonFilePath1, nestedJsonFilePath1, JSON);
        assertEquals(compareSameFiles, IDENTICAL_FILES_MESSAGE);

        String compareEmptyFiles = Differ.generate(emptyYmlFilePath, emptyJsonFilePath, JSON);
        assertEquals(compareEmptyFiles, EMPTY_FILES_MESSAGE);
    }

    @Test
    void plainStyleTest() throws Exception {
        String actualJson = Differ.generate(nestedJsonFilePath1, nestedJsonFilePath2, PLAIN);
        assertEquals(actualJson, expectedPlain);

        String actualYml = Differ.generate(nestedYmlFilePath1, nestedYmlFilePath2, PLAIN);
        assertEquals(actualYml, expectedPlain);

        String compareSameFiles = Differ.generate(nestedYmlFilePath1, nestedYmlFilePath1, PLAIN);
        assertEquals(compareSameFiles, IDENTICAL_FILES_MESSAGE);

        String compareEmptyFiles = Differ.generate(emptyYmlFilePath, emptyJsonFilePath, PLAIN);
        assertEquals(compareEmptyFiles, EMPTY_FILES_MESSAGE);
    }

    @Test
    void stylishStyleTest() throws Exception {
        String actualJson = Differ.generate(nestedJsonFilePath1, nestedJsonFilePath2, STYLISH);
        assertEquals(actualJson, expectedStylish);

        String actualYml = Differ.generate(nestedYmlFilePath1, nestedYmlFilePath2, STYLISH);
        assertEquals(actualYml, expectedStylish);

        String compareSameFiles = Differ.generate(nestedJsonFilePath2, nestedJsonFilePath2, STYLISH);
        assertEquals(compareSameFiles, IDENTICAL_FILES_MESSAGE);

        String compareEmptyFiles = Differ.generate(emptyYmlFilePath, emptyJsonFilePath, STYLISH);
        assertEquals(compareEmptyFiles, EMPTY_FILES_MESSAGE);
    }

    @Test
    void exceptionTest() {
        String fileWithWrongExtension = "./src/test/resources/expected/plain.txt";
        String errorMessage1 = Parser.ERROR_MESSAGE + "txt";
        Throwable thrown1 = assertThrows(IOException.class, () ->
                Differ.generate(fileWithWrongExtension, nestedYmlFilePath2)
        );
        assertEquals(thrown1.getMessage(), errorMessage1);

        String fileWithoutExtension = "./src/test/resources/expected/nestedStylish";
        String errorMessage2 = Differ.ERROR_MESSAGE_WO_EXT + fileWithoutExtension;
        Throwable thrown2 = assertThrows(IOException.class, () ->
                Differ.generate(fileWithoutExtension, nestedYmlFilePath2)
        );
        assertEquals(thrown2.getMessage(), errorMessage2);

        String wrongFormat = "yaml";
        String errorMessage3 = Formatter.ERROR_MESSAGE + wrongFormat;
        Throwable thrown3 = assertThrows(IOException.class, () ->
                Differ.generate(nestedYmlFilePath1, nestedYmlFilePath2, wrongFormat)
        );
        assertEquals(thrown3.getMessage(), errorMessage3);

        String nonExistingFile = "someFile.json";
        String errorMessage4 = Differ.ERROR_MESSAGE_FILE_NF + nonExistingFile;
        Throwable thrown4 = assertThrows(IOException.class, () ->
                Differ.generate(nestedYmlFilePath1, nonExistingFile)
        );
        assertEquals(thrown4.getMessage(), errorMessage4);
    }
}
