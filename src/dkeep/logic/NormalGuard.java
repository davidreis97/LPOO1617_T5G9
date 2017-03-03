package dkeep.logic;

import java.awt.Point;

public class NormalGuard extends Guard {
	
	public NormalGuard(Point coords, char representation) {
		super(coords, representation);
	}
	
	public NormalGuard(Point coords, char representation, char[] guardPath) {
		super(coords, representation, guardPath);
	}
	
	public void nextMovement(int index) {
		
		char nextMove = guardPath[stepCounter];
		
		stepCounter++;
		if(stepCounter >= guardPath.length) {
			stepCounter = 0;
		}
		
		coords = Game.move(coords, nextMove, "Guard", index);
	}
}
