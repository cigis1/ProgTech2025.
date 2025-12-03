 
package hu.amoba;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int cols;
    private final char[][] grid;
    private static final int WIN_COUNT = 5;
    
    public Board(int rows, int cols) {
        if (rows < 5 || cols < 5 || rows > 25 || cols > 25) {
            throw new IllegalArgumentException("A tábla mérete 5-25 között lehet!");
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '.';
            }
        }
    }
    
    public boolean makeMove(Position pos, char symbol) {
        int row = pos.getRow();
        int col = pos.getCol();
        
        if (!isValidPosition(row, col) || grid[row][col] != '.') {
            return false;
        }
        
        grid[row][col] = symbol;
        return true;
    }
    
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    
    public char checkWinner() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] != '.') {
                    char symbol = grid[i][j];
                    
                    if (j <= cols - WIN_COUNT) {
                        boolean win = true;
                        for (int k = 0; k < WIN_COUNT; k++) {
                            if (grid[i][j + k] != symbol) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return symbol;
                    }
                    
                    if (i <= rows - WIN_COUNT) {
                        boolean win = true;
                        for (int k = 0; k < WIN_COUNT; k++) {
                            if (grid[i + k][j] != symbol) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return symbol;
                    }
                    
                    if (i <= rows - WIN_COUNT && j <= cols - WIN_COUNT) {
                        boolean win = true;
                        for (int k = 0; k < WIN_COUNT; k++) {
                            if (grid[i + k][j + k] != symbol) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return symbol;
                    }
                    
                    if (i <= rows - WIN_COUNT && j >= WIN_COUNT - 1) {
                        boolean win = true;
                        for (int k = 0; k < WIN_COUNT; k++) {
                            if (grid[i + k][j - k] != symbol) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return symbol;
                    }
                }
            }
        }
        
        return '.';
    }
    
    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void printBoard() {
        System.out.print("\n  ");
        for (int j = 0; j < cols; j++) {
            System.out.print((char)('a' + j) + " ");
        }
        System.out.println();
        
        for (int i = 0; i < rows; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public List<Position> getValidMoves(char symbol) {
        List<Position> moves = new ArrayList<>();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.') {
                    moves.add(new Position(i, j));
                }
            }
        }
        
        return moves;
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char getCell(int row, int col) { return grid[row][col]; }
    public void setCell(int row, int col, char value) { grid[row][col] = value; }
    public static int getWinCount() { return WIN_COUNT; }
}