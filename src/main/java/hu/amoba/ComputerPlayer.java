package hu.amoba;

import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {
    private final Random random;
    
    public ComputerPlayer(String name, char symbol) {
        super(name, symbol);
        this.random = new Random();
    }
    
    public Position getMove(Board board) {
        List<Position> availableMoves = board.getValidMoves(this.getSymbol());
        
        if (availableMoves.isEmpty()) {
            return null;
        }
        
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}