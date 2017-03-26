package dkeep.logic;

import java.awt.Point;

public class DrunkenGuard extends Guard { 
	
	private boolean isReversed = false;
	
	public DrunkenGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	public DrunkenGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
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
	
	private char nextDirection() {
		
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
		
		return nextMove;
	}
	
	public void nextMovement(int index) {

		doLogic(generateChance(0.20f), generateChance(0.20f));

		if(isSleeping) {
			return;
		}
		
		char nextMove = nextDirection();

		stepCounter = clamp(stepCounter, 0, guardPath.length - 1);
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
