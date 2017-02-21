package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Guard extends Entity {
	private int stepCounter;   //Current index on guard path
	private char guardPath[];  //Array of inputs that correspond to the guard's patrol
	private String guardType;
	private String guardState;
	private char guardChar;
	private boolean isReversed;
	
	public Guard(Point guardCoords, char path[]) {
		this.coords = guardCoords;
		this.guardPath = path;
		this.stepCounter = 0;
		this.guardType = chooseType();
		this.setGuardState("Normal");
		this.setGuardChar('G');
		this.isReversed = false;
	}
	
	//Choose a guard type
	public String chooseType() {
		
		Random rand = new Random();
		int n = rand.nextInt(3);
		
		switch(n) {
		case 0:
			return "Rookie";
		case 1:
			return "Drunken";
		case 2:
			return "Suspicious";
		default:
			return " ";
		}
	}
	
	//Handle logic for guard types, sleeping, reversing path, etc
	public void guardTick() {
		
		//Chance of sleeping/waking if Drunken type
		if(this.guardType.equals("Drunken")) {
			Random rand = new Random();
			int n = rand.nextInt(3);
			
			if(n == 2) {
				if(this.guardState.equals("Sleeping")) {
					this.guardState = "SleepToWake";
					this.guardChar = 'G';
				} else {
					this.guardState = "Sleeping";
					this.guardChar = 'g';
				}
			}
		}
		
		//Chance of reversing if Suspicious type or just woke up
		if(this.guardState.equals("SleepToWake") || this.guardType.equals("Suspicious")) {
			Random rand = new Random();
			int n = rand.nextInt(3);
			
			if(n == 2) {
				this.isReversed = !this.isReversed;
				this.updateStepCounter();
			}
			
			this.guardState = "Normal";
		}
	}

	//Updates step counter and verifies end of path conditions
	public void updateStepCounter() {
		if(isReversed) {
			this.stepCounter--;
		} else this.stepCounter++;
		
		if(this.stepCounter >= this.guardPath.length) {
			this.stepCounter = 0;
		} else if(this.stepCounter < 0) {
			this.stepCounter = this.guardPath.length - 1;
		}
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
	
	public String getGuardType() {
		return guardType;
	}

	public String getGuardState() {
		return guardState;
	}
	
	public char getGuardChar() {
		return guardChar;
	}

	public boolean isReversed() {
		return isReversed;
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
	
	public void setGuardType(String guardType) {
		this.guardType = guardType;
	}

	public void setGuardState(String guardState) {
		this.guardState = guardState;
	}

	public void setGuardChar(char guardChar) {
		this.guardChar = guardChar;
	}
	
	public void setReversed(boolean isReversed) {
		this.isReversed = isReversed;
	}
}
