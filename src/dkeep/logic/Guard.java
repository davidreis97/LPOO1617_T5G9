package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public abstract class Guard extends Entity {
	
	protected int stepCounter;
	protected char guardPath[];
	protected boolean isSleeping = false;

	public Guard(Point coords, char representation) {
		super(coords, representation);
		this.stepCounter = 0;
		this.guardPath = new char[]{'a', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a', 'a', 's',
                                    'd', 'd', 'd', 'd', 'd', 'd', 'd', 'w', 'w', 'w', 'w', 'w'};
	}
	
	public Guard(Point coords, char representation, char[] guardPath) {
		super(coords, representation);
		this.stepCounter = 0;
		this.guardPath = guardPath;
	}
	
	protected static int clamp(int val, int min, int max) {
	    if(val < min) {
	    	return max;
	    } else if(val > max) {
	    	return min;
	    } else return val;
	}
	
	protected static boolean generateChance(float probability) {
		Random random = new Random();
		return random.nextFloat() < probability;
	}
	
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
	
	public abstract void nextMovement(int index);

	public int getStepCounter(){
		return stepCounter;
	}
	
	public boolean isSleeping() {
		return isSleeping;
	}
}