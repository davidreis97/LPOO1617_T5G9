package dkeep.logic;

import java.awt.Point;
import java.util.Random;

/**
 * Class that implements basic Guard object functionality, to be extended by other Guard types.
 */
public abstract class Guard extends Entity {
	
	protected int stepCounter;
	protected char guardPath[];
	protected boolean isSleeping = false;

	/**
	 * Class constructor, uses default patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 */
	public Guard(Point coords, char representation) {
		super(coords, representation);
		this.stepCounter = 0;
		this.guardPath = new char[]{'a', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a', 'a', 's',
                                    'd', 'd', 'd', 'd', 'd', 'd', 'd', 'w', 'w', 'w', 'w', 'w'};
	}
	
	/**
	 * Class constructor, allows setting patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 * @param  guardPath      array of directions Guard will follow
	 */
	public Guard(Point coords, char representation, char[] guardPath) {
		super(coords, representation);
		this.stepCounter = 0;
		this.guardPath = guardPath;
	}
	
	/**
	 * Clamps given value between min and max values.
	 *
	 * @param  val value to clamp
	 * @param  min minimum value allowed
	 * @param  max maximum value allowed
	 * @return     clamped value
	 */
	protected static int clamp(int val, int min, int max) {
	    if(val < min) {
	    	return max;
	    } else if(val > max) {
	    	return min;
	    } else return val;
	}
	
	/**
	 * Generates a boolean with given probability (of being true).
	 *
	 * @param  probability chance, from 0.00 to 1.00, of returning true
	 * @return             true/false
	 */
	protected static boolean generateChance(float probability) {
		Random random = new Random();
		return random.nextFloat() < probability;
	}
	
	/**
	 * Reverses movement direction.
	 *
	 * @param  nextMove movement direction to reverse
	 * @return          reversed direction
	 */
	private char reverseDirection(char nextMove) {
		
		switch(nextMove) {
		case 'w':
			return 's';
		case 's':
			return 'w';
		case 'a':
			return 'd';
		case 'd':
			return 'a';
		default:
			return 'w';
		}
	}
	
	/**
	 * Gets next direction of movement from patrol path and reversed status. Updates stepCounter.
	 *
	 * @param  isReversed whether Guard has reversed movement
	 * @return            movement direction
	 */
	protected char nextDirection(boolean isReversed) {
		
		char nextMove = guardPath[stepCounter];
		
		if(isReversed) {
			nextMove = reverseDirection(nextMove);
			stepCounter--;
		} else {
			stepCounter++;
		}
		
		return nextMove;
	}
	
	@Override
	public abstract void nextMovement(int index);

	/**
	 * @return value of current index in patrol path
	 */
	public int getStepCounter(){
		return stepCounter;
	}
	
	/**
	 * @return whether Guard is sleeping
	 */
	public boolean isSleeping() {
		return isSleeping;
	}
}