import Game.GameController;
import java.io.File;
import Player.player
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            GameController gameController = new GameController();
            System.out.println("*****************************");
            System.out.println("Welcome to the Monopoly Game!");
            System.out.println("1. Start a new game");
            System.out.println("2. Resume a game");
            System.out.println("3. Design game board");
            System.out.println("4. Quit");
            System.out.println("*****************************");
            System.out.print("Choice: ");

            try {
                int function = scanner.nextInt();

                switch (function) {
                    case 1:
                        // Start a new game
                        gameController.initializeGameBoard();
                        gameController.initializeGamePlayer();
                        gameController.startGame();
                        break;

                    case 2:
                        // Resume a game
                        gameController.setisNewGame(false);
                        boolean success = false;

                        while (!success) {
                            System.out.print("Please input the game data file path: ");
                            String filePath = scanner.next();

                            // Error checking for file existence and loading game data
                            File file = new File(filePath);
                            if (!file.exists() || !file.isFile()) {
                                System.out.println("\u001B[31mError: The specified file does not exist or is not a valid file. Please try again.\u001B[0m\n");
                                continue; // Retry the loop
                            }

                            try {
                                gameController.loadGameData(filePath);
                                gameController.startGame();
                                success = true; // Exit the loop after successful loading
                            } catch (Exception e) {
                                System.out.println("Error: Failed to load game data. " + e.getMessage());
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 3:
                        // Design game board
                        break;
                    case 4:
                        // Quit
                        System.out.println("Thank you for playing! Goodbye.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("\u001B[31m\nInvalid choice. Please select a valid option.\n\u001B[0m");

                        break;
                }

            } catch (Exception e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number.\u001B[0m\n");
                scanner.next(); // Clear the invalid input
            }
        }
    }
}
