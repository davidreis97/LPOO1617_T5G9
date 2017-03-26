package dkeep.logic;

import java.awt.Point;
import java.util.Random;

/**
 * Class that extends basic Entity object functionality. Ogre moves randomly and smashes his Club randomly on adjacent squares.
 */
public class Ogre extends Entity {

	boolean isStunned;
	int stunCounter;
	
	/**
	 * Class constructor, defaults to not stunned.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 */
	public Ogre(Point coords, char representation) {
		super(coords, representation);
		this.isStunned = false;
		this.stunCounter = 0;
	}
	
	/**
	 * Randomly decides next movement direction for Ogre.
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
	 * Updates Ogre entity to new coords using a randomly generated movement direction. If stunned no movement happens and stun counter is decremented.<br>
	 * Sets corresponding Club position to same as the Ogre. Club movement always happens next.
	 *
	 * @param  index index of current Entity from entities array
	 */
	public void nextMovement(int index) {
	
		if(isStunned) {
			stunCounter--;
			if(stunCounter <= 0) {
				isStunned = false;
				representation = '0';
			}
		} else coords = Game.move(coords, nextDirection(), "Ogre", index);
		
		try {
			Game.getEntities().get(index + 1).setCoords(coords); //CAUTION assumes ogre's club is always the next entities element
		} catch (Exception e) {
			Game.getEntities().add(new Ogre(new Point(coords), '*'));			
		}
	}
	
	/**
	 * @return whether Ogre is stunned
	 */
	public boolean isStunned() {
		return isStunned;
	}

	/**
	 * Sets Ogre stunned flag, also sets stun counter accordingly.
	 *
	 * @param  isStunned whether Ogre is stunned
	 */
	public void setStunned(boolean isStunned) {
		this.stunCounter = 3;
		this.representation = '8';
		this.isStunned = isStunned;
	}
}
