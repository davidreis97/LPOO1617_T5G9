
public class Guard {
	private int x;
	private int y;
	private int stepCounter; //Counts how many steps the guard has taken in the current patrol. Resets to 0 when he does a full loop.
	private String guardPath[]; //Array of inputs that correspond to the guard's patrol.
	
	public Guard(int startx, int starty){
		x = startx; y = starty;
	}
	
	public Guard(int startx, int starty, String gP[]){
		x = startx; y = starty;
		guardPath = gP;
		stepCounter = 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getStepCounter() {
		return stepCounter;
	}

	public void setStepCounter(int stepCounter) {
		this.stepCounter = stepCounter;
	}

	public String[] getGuardPath() {
		return guardPath;
	}

	public void setGuardPath(String guardPath[]) {
		this.guardPath = guardPath;
	}
}
