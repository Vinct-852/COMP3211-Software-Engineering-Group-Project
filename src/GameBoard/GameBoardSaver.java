package GameBoard;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Scanner;

public class GameBoardSaver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "";

        // Loop until a valid XML file is provided
        while (true) {
            System.out.print("Enter the XML file name/path : ");
            fileName = scanner.nextLine();

            File inputFile = new File(fileName);
            if (inputFile.exists()) {
                break; // Exit the loop if the file exists
            } else {
                System.out.println("File not found: " + inputFile.getAbsolutePath() + ". Please try again.");
            }
        }

        // Call the method to update the game board
        updateGameBoard(fileName);
        scanner.close();
    }

    public static void updateGameBoard(String fileName) {
        try {
            // Load the existing XML file
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Scanner scanner = new Scanner(System.in);

            // Loop indefinitely for updates
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Update square type");
                System.out.println("2. Update square position");
                System.out.println("3. Update property");
                System.out.println("4. Save");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.print("Enter the position of the square to update type: ");
                        int typePosition = scanner.nextInt();
                        updateSquareTypeWithRead(doc, typePosition, scanner);
                        break;

                    case 2:
                        System.out.print("Enter the current position of the square to update: ");
                        int oldPosition = scanner.nextInt();
                        System.out.print("Enter the new position: ");
                        int newPosition = scanner.nextInt();
                        updateSquarePosition(doc, oldPosition, newPosition);
                        break;

                    case 3:
                        System.out.print("Enter the position of the square to update: ");
                        int positionToUpdate = scanner.nextInt();
                        System.out.print("Enter the new name for the square: ");
                        scanner.nextLine(); // Consume the newline
                        String newName = scanner.nextLine();
                        System.out.print("Enter the new price for the square: ");
                        int newPrice = scanner.nextInt();
                        System.out.print("Enter the new rent amount: ");
                        int updatedRent = scanner.nextInt();
                        updateSquareNamePriceAndRent(doc, positionToUpdate, newName, newPrice, updatedRent);
                        break;

                    case 4:
                        System.out.println("Exiting the updater.");
                        // Save the updated XML file to a new file
                        saveXML(doc, "updatedGameBoard.xml"); // Save to a new file
                        return; // Exit the method and end the program

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSquareTypeWithRead(Document doc, int position, Scanner scanner) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                String currentType = square.getAttribute("type");
                System.out.println("Current type for position " + position + " is: " + currentType);

                // Prompt user for new type
                System.out.println("Select a new type for the square:");
                System.out.println("1. Property");
                System.out.println("2. Income Tax");
                System.out.println("3. Jail");
                System.out.println("4. Chance");
                System.out.println("5. Free Parking");
                System.out.println("6. Go Jail");
                System.out.println("7. Go");
                int typeChoice = scanner.nextInt();
                String newType = "";

                switch (typeChoice) {
                    case 1:
                        newType = "Property";
                        break;
                    case 2:
                        newType = "IncomeTax";
                        break;
                    case 3:
                        newType = "Jail";
                        break;
                    case 4:
                        newType = "Chance";
                        break;
                    case 5:
                        newType = "FreeParking";
                        break;
                    case 6:
                        newType = "GoJail";
                        break;
                    case 7:
                        newType = "Go";
                        break;
                    default:
                        System.out.println("Invalid choice. No changes made.");
                        return; // Exit if the choice is invalid
                }

                square.setAttribute("type", newType); // Update square type
                System.out.println("Updated type for position " + position + " to " + newType);
                found = true;
                break; // Exit loop after updating
            }
        }
        if (!found) {
            System.out.println("Position " + position + " not found.");
        }
    }

    public static void updateSquarePosition(Document doc, int oldPosition, int newPosition) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == oldPosition) {
                square.setAttribute("position", String.valueOf(newPosition));
                System.out.println("Updated position from " + oldPosition + " to " + newPosition);
                found = true;
                break; // Exit loop after updating
            }
        }
        if (!found) {
            System.out.println("Old position " + oldPosition + " not found.");
        }
    }

    public static void updateSquareNamePriceAndRent(Document doc, int position, String newName, int newPrice,
            int newRent) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                square.getElementsByTagName("name").item(0).setTextContent(newName);
                square.getElementsByTagName("price").item(0).setTextContent(String.valueOf(newPrice));
                square.getElementsByTagName("rent").item(0).setTextContent(String.valueOf(newRent)); // Update rent
                System.out.println("Updated square at position " + position + " to name: " + newName + ", price: "
                        + newPrice + ", and rent: " + newRent);
                found = true;
                break; // Exit loop after updating
            }
        }
        if (!found) {
            System.out.println("Position " + position + " not found.");
        }
    }

    public static void saveXML(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // for pretty output
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
            System.out.println("Saved updated gameboard to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
