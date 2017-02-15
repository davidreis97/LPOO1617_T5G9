
public class Guard {
	int x;
	int y;
	int stepCounter; //Counts how many steps the guard has taken in the current patrol. Resets to 0 when he does a full loop.
	String guardPath[]; //Array of inputs that correspond to the guard's patrol.
	
	public Guard(int startx, int starty){
		x = startx; y = starty;
	}
	
	public Guard(int startx, int starty, String gP[]){
		x = startx; y = starty;
		guardPath = gP;
		stepCounter = 0;
	}
}
