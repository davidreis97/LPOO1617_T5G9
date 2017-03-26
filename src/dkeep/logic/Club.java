package dkeep.logic;

import java.awt.Point;
import java.util.Random;

/**
 * Class that extends basic Entity object functionality. Club stomps randomly on a position adjacent to corresponding Ogre.
 */
public class Club extends Entity {

	boolean active = true;
	
	/**
	 * Class constructor, defaults to active.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 */
	public Club(Point coords, char representation) {
		super(coords, representation);
	}
	
	/**
	 * Randomly decides next movement direction for Club.
	 *
	 * @return movement direction
	 */
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
	
	/**
	 * Updates Club entity to new coords using a randomly generated movement direction. Only moves if no entity collision detected, if Hero is detected game is lost.<br>
	 * Sets Club to not active if the new coords are the same as the old (Club is on top of Ogre).
	 *
	 * @param  index index of current Entity from entities array
	 */
	public void nextMovement(int index) {
		int result;
		Point oldCoords = coords;
		char nextMove = nextDirection();
		result = Game.entityCol(index, nextMove);
		
		if(result == -1) {
			coords = Game.move(coords, nextMove, "Club", index);
		} else if(Game.getEntities().get(result) instanceof Hero) {
			coords = Game.move(coords, nextMove, "Club", index);
			Game.setState("Lose");
		}
		
		if(oldCoords == coords) {
			active = false;
		} else active = true;
	}

	/**
	 * @return whether Club is active (collision happens or not)
	 */
	public boolean isActive() {
		return active;
	}
}
