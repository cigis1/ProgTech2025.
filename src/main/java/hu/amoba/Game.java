
package hu.amoba;

import java.io.*;
import java.util.Scanner;

public class Game {
    private Board board;
    private boolean gameActive;
    private DatabaseManager dbManager;
    private String playerName;
    private ComputerPlayer computerPlayer;
    
    public Game() {
        this.dbManager = new DatabaseManager();
        this.computerPlayer = new ComputerPlayer("Számítógép", 'O');
    }
    
    public void startNewGame(Scanner scanner) {
        System.out.print("Add meg a neved: ");
        this.playerName = scanner.nextLine();
        
        this.board = new Board(10, 10);
        this.gameActive = true;
        
        System.out.println("\n=== AMŐBA JÁTÉK ===");
        System.out.println("Üdvözöllek " + playerName + "!");
        System.out.println("Te vagy 'X', a számítógép 'O'.");
        System.out.println("5 azonos jel kell egy sorban, oszlopban vagy átlóban a győzelemhez!");
        System.out.println("Bárhol léphetsz az üres táblán!");
        
        playGame(scanner);
    }
    
    private void playGame(Scanner scanner) {
        boolean humanTurn = true;
        
        while (gameActive) {
            board.printBoard();
            
            if (humanTurn) {
                boolean validInput = false;
                
                while (!validInput && gameActive) {
                    System.out.print("\n" + playerName + " lépése (pl: 'a1', 'mentes', 'kilep'): ");
                    
                    if (!scanner.hasNextLine()) {
                        gameActive = false;
                        break;
                    }
                    
                    String input = scanner.nextLine();
                    
                    if (input == null || input.trim().isEmpty()) {
                        continue;
                    }
                    
                    input = input.trim();
                    
                    if (input.equalsIgnoreCase("mentes")) {
                        saveGameToFile(scanner);
                        continue;
                    } else if (input.equalsIgnoreCase("kilep")) {
                        System.out.println("Játék megszakítva.");
                        gameActive = false;
                        break;
                    }
                    
                    try {
                        Position pos = parsePosition(input);
                        if (board.makeMove(pos, 'X')) {
                            validInput = true;
                            checkGameStatus();
                            if (gameActive) {
                                humanTurn = false;
                            }
                        } else {
                            System.out.println("Érvénytelen lépés! Próbáld újra.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Hibás formátum! Használj pl: 'a1' (betű, majd szám)");
                    }
                }
            } else {
                System.out.println("\nA számítógép gondolkozik...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                Position computerMove = computerPlayer.getMove(board);
                
                if (computerMove == null) {
                    System.out.println("A számítógépnek nincs érvényes lépése! Váltás a játékosra.");
                    humanTurn = true;
                    continue;
                }
                
                System.out.println("Számítógép lépése: " + getPositionString(computerMove));
                
                boolean moveSuccessful = board.makeMove(computerMove, 'O');
                
                if (moveSuccessful) {
                    checkGameStatus();
                    if (gameActive) {
                        humanTurn = true;
                    }
                } else {
                    System.out.println("Hiba: a számítógép lépése nem sikerült.");
                    humanTurn = true;
                }
            }
        }
    }
    
    private String getPositionString(Position pos) {
        return "" + (char)('a' + pos.getCol()) + (pos.getRow() + 1);
    }
    
    private void checkGameStatus() {
        char winner = board.checkWinner();
        
        if (winner == 'X') {
            board.printBoard();
            System.out.println("\n=== GYŐZELEM ===");
            System.out.println("GRATULÁLOK " + playerName + "!");
            System.out.println("5 'X' lett egy sorban, oszlopban vagy átlóban!");
            dbManager.saveWin(playerName);
            gameActive = false;
        } else if (winner == 'O') {
            board.printBoard();
            System.out.println("\n=== GYŐZELEM ===");
            System.out.println("A számítógép nyert!");
            System.out.println("5 'O' lett egy sorban, oszlopban vagy átlóban!");
            gameActive = false;
        } else if (board.isFull()) {
            board.printBoard();
            System.out.println("\n=== DÖNTETLEN ===");
            System.out.println("A tábla megtelt, senki sem nyert!");
            gameActive = false;
        }
    }
    
    private Position parsePosition(String input) {
        if (input.length() < 2) {
            throw new IllegalArgumentException();
        }
        
        char colChar = Character.toLowerCase(input.charAt(0));
        int col = colChar - 'a';
        
        String rowStr = input.substring(1);
        int row;
        try {
            row = Integer.parseInt(rowStr) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        
        return new Position(row, col);
    }
    
    public void saveGameToFile(Scanner scanner) {
        System.out.print("Fájlnév (pl: jatek.txt): ");
        String filename = scanner.nextLine();
        
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println(board.getRows());
            writer.println(board.getCols());
            
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    writer.print(board.getCell(i, j));
                }
                writer.println();
            }
            
            System.out.println("Játék sikeresen mentve: " + filename);
        } catch (IOException e) {
            System.out.println("Hiba történt a mentés során: " + e.getMessage());
        }
    }
    
    public void loadGameFromFile(Scanner scanner) {
        System.out.print("Fájlnév: ");
        String filename = scanner.nextLine();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int rows = Integer.parseInt(reader.readLine());
            int cols = Integer.parseInt(reader.readLine());
            
            board = new Board(rows, cols);
            
            for (int i = 0; i < rows; i++) {
                String line = reader.readLine();
                for (int j = 0; j < cols; j++) {
                    board.setCell(i, j, line.charAt(j));
                }
            }
            
            System.out.println("Játék sikeresen betöltve!");
            gameActive = true;
            playGame(scanner);
            
        } catch (IOException e) {
            System.out.println("Hiba történt a betöltés során: " + e.getMessage());
        }
    }
    
    public void showHighScores() {
        System.out.println("\n=== HIGH SCORE ===\n");
        dbManager.printHighScores();
    }
}