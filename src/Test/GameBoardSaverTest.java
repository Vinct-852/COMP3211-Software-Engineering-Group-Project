package Test;

import GameBoard.GameBoardSaver;
import org.junit.jupiter.api.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameBoardSaverTest {

    private static final String TEST_XML_PATH = "testGameBoard.xml";

    @BeforeEach
    void setUp() {
        // Clean up any previous test files
        File testFile = new File(TEST_XML_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
        File testFile = new File(TEST_XML_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testCreateEmptyGameBoardXml() {
        // Act
        GameBoardSaver.createEmptyGameBoardXml();

        // Assert
        File createdFile = new File("MonopolyGame.xml");
        assertTrue(createdFile.exists(), "XML file should be created.");

        try {
            // Parse and verify content
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(createdFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            assertEquals("root", root.getNodeName(), "Root element should be 'root'.");

            Element gameBoard = (Element) root.getElementsByTagName("GameBoard").item(0);
            assertNotNull(gameBoard, "GameBoard element should exist.");

            assertEquals(20, gameBoard.getElementsByTagName("squares").getLength(), "There should be 20 squares.");
        } catch (Exception e) {
            fail("Failed to parse or validate the XML file: " + e.getMessage());
        }
    }

    @Test
    void testUpdateGameBoardInvalidFile() {
        // Act and Assert
        File invalidFile = new File("invalid.xml");
        assertFalse(GameBoardSaver.isValidXmlFile(invalidFile), "Invalid XML file should not pass validation.");
    }

    @Test
    void testUpdateSquareNameAndPrice() {
        // Arrange
        GameBoardSaver.createEmptyGameBoardXml();
        File createdFile = new File("MonopolyGame.xml");

        try {
            // Parse the file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(createdFile);

            // Act
            GameBoardSaver.updateSquareNameAndPrice(doc, 1, "Park Place", 350);

            // Assert
            Element square = (Element) doc.getElementsByTagName("squares").item(0);
            assertEquals("Park Place", square.getElementsByTagName("name").item(0).getTextContent(), "Square name should be updated.");
            assertEquals("350", square.getElementsByTagName("price").item(0).getTextContent(), "Square price should be updated.");

        } catch (Exception e) {
            fail("Test failed due to unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testUpdateSquareType() {
        // Arrange
        GameBoardSaver.createEmptyGameBoardXml();
        File createdFile = new File("MonopolyGame.xml");

        try {
            // Parse the file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(createdFile);

            // Act
            GameBoardSaver.updateSquareType(doc, 1, "IncomeTax");

            // Assert
            Element square = (Element) doc.getElementsByTagName("squares").item(0);
            assertEquals("IncomeTax", square.getAttribute("type"), "Square type should be updated.");
        } catch (Exception e) {
            fail("Test failed due to unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testValidateGameBoard() {
        // Arrange
        GameBoardSaver.createEmptyGameBoardXml();
        File createdFile = new File("MonopolyGame.xml");

        try {
            // Parse the file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(createdFile);

            // Act
            boolean isValid = GameBoardSaver.validateGameBoard(doc);

            // Assert
            assertFalse(isValid, "Game board should not be valid as there is no 'Go' square.");
        } catch (Exception e) {
            fail("Test failed due to unexpected exception: " + e.getMessage());
        }
    }
}
