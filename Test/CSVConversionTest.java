import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CSVConversionTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void FileToString() {
        String line = CSVConversion.fileToString( "TestData/File-to-String-CSV-Test.txt" );
        assertEquals( "This is a document, to test our CSVConversion", line);
    }

    @Test
    void stringToFile() {
        CSVConversion.stringToFile("TestData/String-to-File-CSV-Test.txt", "Hey, am I written in the file?");
        String line = CSVConversion.fileToString("TestData/String-to-File-CSV-Test.txt");
        assertEquals("Hey, am I written in the file?", line );
    }
}