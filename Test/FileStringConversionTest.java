import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class FileStringConversionTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void FileToString() {
        String line = FileStringConversion.fileToString( "TestData/File-to-String-CSV-Test.txt" );
        assertEquals( "This is a document to test our CSVConversion\n", line);
    }

    @Test
    void stringToFile() {
        FileStringConversion.stringToFile("TestData/String-to-File-CSV-Test.txt", "Hey, am I written in the file?");
        String line = FileStringConversion.fileToString("TestData/String-to-File-CSV-Test.txt");
        assertEquals("Hey, am I written in the file?\n", line );
    }
}