package dkeep.logic;

import java.awt.Point;

/**
 * Class that extends basic Guard object functionality. DrunkenGuard randomly sleeps and may reverse patrol path upon waking up.
 */
public class DrunkenGuard extends Guard { 
	
	private boolean isReversed = false;
	
	/**
	 * Class constructor, uses default patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 */
	public DrunkenGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	/**
	 * Class constructor, allows setting patrol path.
	 *
	 * @param  coords         coords for Guard
	 * @param  representation char representation of Guard
	 * @param  guardPath      array of directions Guard will follow
	 */
	public DrunkenGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	/**
	 * Updates sleeping/reversed flags and updates current index on patrol path.
	 *
	 * @param  toggleSleep   whether to toggle isSleeping flag
	 * @param  toggleReverse whether to toggle isReversed flag
	 */
	private void doLogic(boolean toggleSleep, boolean toggleReverse) {
		
		if(isSleeping && toggleSleep) {
			isSleeping = false;
			representation = 'G';

			if(toggleReverse && isReversed) {
				stepCounter++;
				stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
				isReversed = !isReversed;
			} else if(toggleReverse && !isReversed) {
				stepCounter--;
				stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
				isReversed = !isReversed;
			}
			
		} else if(!isSleeping && toggleSleep){
			isSleeping = true;
			representation = 'g';
		}
	}

	/**
	 * Updates DrunkenGuard entity to new coords using current patrol path index. If sleeping no movement happens, if reversed patrols in opposite direction.
	 *
	 * @param  index index of current Entity from entities array
	 */
	@Override
	public void nextMovement(int index) {

		doLogic(generateChance(0.20f), generateChance(0.20f));

		if(isSleeping) {
			return;
		}
		
		char nextMove = nextDirection(isReversed);

		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
