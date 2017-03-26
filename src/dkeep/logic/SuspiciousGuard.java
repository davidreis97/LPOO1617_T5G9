package dkeep.logic;

import java.awt.Point;

public class SuspiciousGuard extends Guard {
	
	private boolean isReversed = false;
	
	public SuspiciousGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	public SuspiciousGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	private void doLogic() {
		
		if(isReversed) {
			stepCounter++;
		} else stepCounter--;
		
		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		isReversed = !isReversed;
	}

	public void nextMovement(int index) {
		
		if(generateChance(0.20f)) {
			doLogic();
		}

		char nextMove = nextDirection(isReversed);

		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
