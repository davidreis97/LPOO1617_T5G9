package dkeep.logic;

import java.awt.Point;

/**
 * Class that extends basic Guard object functionality. RookieGuard follows his patrol path gingerly.
 */
public class RookieGuard extends Guard {
	
	/**
	 * Class constructor, uses default patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 */
	public RookieGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	/**
	 * Class constructor, allows setting patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 * @param  guardPath      array of directions Guard will follow
	 */
	public RookieGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	/**
	 * Updates RookieGuard entity to new coords using current patrol path index.
	 *
	 * @param  index index of current Entity from entities array
	 */
	@Override
	public void nextMovement(int index) {
		
		char nextMove = guardPath[stepCounter];
		
		stepCounter++;
		if(stepCounter >= guardPath.length) {
			stepCounter = 0;
		}
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
