
package hu.amoba;

import java.io.*;
import java.util.*;

public class DatabaseManager {
    private static final String SCORES_FILE = "amoba_scores.txt";
    
    public DatabaseManager() {
        File file = new File(SCORES_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Nem sikerült létrehozni a scores fájlt: " + e.getMessage());
            }
        }
    }
    
    public void saveWin(String playerName) {
        Map<String, Integer> scores = loadScores();
        
        int currentWins = scores.getOrDefault(playerName, 0);
        scores.put(playerName, currentWins + 1);
        
        saveScores(scores);
        System.out.println("Győzelem mentve: " + playerName + " (" + (currentWins + 1) + ". győzelem)");
    }
    
    private Map<String, Integer> loadScores() {
        Map<String, Integer> scores = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int wins = Integer.parseInt(parts[1].trim());
                    scores.put(name, wins);
                }
            }
        } catch (IOException e) {
        } catch (NumberFormatException e) {
            System.err.println("Hibás adatformátum a scores fájlban");
        }
        
        return scores;
    }
    
    private void saveScores(Map<String, Integer> scores) {
        try (PrintWriter writer = new PrintWriter(SCORES_FILE)) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Hiba a scores mentésekor: " + e.getMessage());
        }
    }
    
    public void printHighScores() {
        Map<String, Integer> scores = loadScores();
        
        System.out.println("\n=== HIGH SCORE ===\n");
        
        if (scores.isEmpty()) {
            System.out.println("Még nincsenek eredmények!");
            return;
        }
        
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        System.out.println("Hely  | Játékos         | Győzelmek");
        System.out.println("------|-----------------|----------");
        
        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedScores) {
            if (rank > 10) break;
            System.out.printf("%-5d | %-15s | %-9d%n", 
                rank++, 
                entry.getKey(), 
                entry.getValue());
        }
    }
}