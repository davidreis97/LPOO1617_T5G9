package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class SuspiciousGuard extends Guard {
	
	private boolean isReversed = false;
	
	public SuspiciousGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	public SuspiciousGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	public void nextMovement(int index) {
		
		Random rand = new Random();
		int reverse = rand.nextInt(4);
		
		switch(reverse) {
		case 1:
			if(isReversed) {
				stepCounter++;
			} else stepCounter--;
			
			stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
			
			isReversed = !isReversed;
			break;
		default:
			break;
		}
		
		char nextMove = 'w';
		
		if(isReversed) {
			nextMove = guardPath[stepCounter];
			
			switch(nextMove) {
			case 'w':
				nextMove = 's';
				break;
			case 's':
				nextMove = 'w';
				break;
			case 'a':
				nextMove = 'd';
				break;
			case 'd':
				nextMove = 'a';
				break;
			default:
				break;
			}
		
			stepCounter--;
		} else {
			nextMove = guardPath[stepCounter];
			stepCounter++;
		}

		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
