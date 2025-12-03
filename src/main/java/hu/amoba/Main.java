package hu.amoba;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== AMŐBA JÁTÉK === Készítette: Soós Roland (TSEQ51) ");
            System.out.println("1. Új játék indítása");
            System.out.println("2. Játék betöltése fájlból");
            System.out.println("3. High Score táblázat");
            System.out.println("4. Kilépés");
            System.out.print("Választás: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    game.startNewGame(scanner);
                    break;
                case "2":
                    game.loadGameFromFile(scanner);
                    break;
                case "3":
                    game.showHighScores();
                    break;
                case "4":
                    exit = true;
                    System.out.println("Kilépés...");
                    break;
                default:
                    System.out.println("Érvénytelen választás!");
            }
        }
        
        scanner.close();
    }
}