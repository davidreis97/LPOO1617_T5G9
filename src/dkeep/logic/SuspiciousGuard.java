package dkeep.logic;

import java.awt.Point;

/**
 * Class that extends basic Guard object functionality. SuspiciousGuard randomly reverses patrol path.
 */
public class SuspiciousGuard extends Guard {
	
	private boolean isReversed = false;
	
	/**
	 * Class constructor, uses default patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 */
	public SuspiciousGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	/**
	 * Class constructor, allows setting patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 * @param  guardPath      array of directions Guard will follow
	 */
	public SuspiciousGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	/**
	 * Updates reversed flags and updates current index on patrol path.
	 */
	private void doLogic() {
		
		if(isReversed) {
			stepCounter++;
		} else stepCounter--;
		
		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		isReversed = !isReversed;
	}
	
	/**
	 * Updates SuspiciousGuard entity to new coords using current patrol path index. If reversed patrols in opposite direction.
	 *
	 * @param  index index of current Entity from entities array
	 */
	@Override
	public void nextMovement(int index) {
		
		if(generateChance(0.20f)) {
			doLogic();
		}

		char nextMove = nextDirection(isReversed);

		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
