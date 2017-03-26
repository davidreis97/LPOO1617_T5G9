package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Club extends Entity {

	public Club(Point coords, char representation) {
		super(coords, representation);
	}
	
	private char nextDirection() {
		Random random = new Random();
		int nextMove = random.nextInt(4);
		
		if(nextMove == 0) {
			return 'w';
		} else if(nextMove == 1) {
			return 's';
		} else if(nextMove == 2) {
			return 'a';
		} else if(nextMove == 3) {
			return 'd';
		}
		
		return 'w';
	}
	
	public void nextMovement(int index) {
		int result;
		
		char nextMove = nextDirection();
		result = Game.entityCol(index, nextMove);
		
		if(result == -1) {
			coords = Game.move(coords, nextMove, "Club", index);
		} else if(Game.getEntities().get(result) instanceof Hero) {
			coords = Game.move(coords, nextMove, "Club", index);
			Game.setState("Lose");
		}
	}
}
