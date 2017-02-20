package dkeep.logic;

import java.awt.Point;

public class Guard extends Entity{
	private int stepCounter; //Counts how many steps the guard has taken in the current patrol. Resets to 0 when he does a full loop
	private char guardPath[]; //Array of inputs that correspond to the guard's patrol
	
	public Guard(Point guardCoords, char path[]) {
		this.coords = guardCoords;
		this.guardPath = path;
		this.stepCounter = 0;
	}

	public Point getGuardCoords() {
		return coords;
	}

	public int getStepCounter() {
		return stepCounter;
	}

	public char[] getGuardPath() {
		return guardPath;
	}

	public void setGuardCoords(Point guardCoords) {
		this.coords = guardCoords;
	}

	public void setStepCounter(int stepCounter) {
		this.stepCounter = stepCounter;
	}

	public void setGuardPath(char[] guardPath) {
		this.guardPath = guardPath;
	}
	
	//Increments step counter and verifies end of path conditions
	public void incrementStepCounter() {
		this.stepCounter++;
		if(this.stepCounter >= this.guardPath.length) {
			this.stepCounter = 0;
		}
	}
}
