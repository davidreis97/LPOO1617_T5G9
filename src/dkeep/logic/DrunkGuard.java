package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class DrunkGuard extends Guard {
	
	private boolean isReversed = false;
	private boolean isSleeping = false;
	
	public DrunkGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	public DrunkGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	public void nextMovement(int index) {
		
		Random rand = new Random();
		int reverse = rand.nextInt(4);
		int sleep = rand.nextInt(4);
		
		switch(sleep) {
		case 1:
			if(isSleeping) {
				isSleeping = false;
				representation = 'G';
				
				if(reverse == 1) {
					if(isReversed) {
						stepCounter++;
					} else stepCounter--;
					
					stepCounter = clamp(stepCounter, 0, guardPath.length - 1);

					isReversed = !isReversed;
				}
				
			} else {
				isSleeping = true;
				representation = 'g';
			}
			break;
		default:
			break;
		}
		
		if(isSleeping) {
			return;
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
