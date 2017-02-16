import java.util.Random;

public class Ogre {
	private int x;
	private int y;
	private int ogreClubX;
	private int ogreClubY;
	
	public Ogre(int startx, int starty, int clubx, int cluby){
		x = startx; y = starty; ogreClubX = clubx; ogreClubY = cluby;
	}
	
	public String generateNewDirection(){
		Random rand = new Random();

		int  n = rand.nextInt(4);
		
		if (n == 0){
			return "w";
		}else if (n == 1){
			return "a";
		}else if (n == 2){
			return "s";
		}else if (n == 3){
			return "d";
		}
		System.out.println("Error!");
		return "";
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

	public int getOgreClubX() {
		return ogreClubX;
	}

	public void setOgreClubX(int ogreClubX) {
		this.ogreClubX = ogreClubX;
	}

	public int getOgreClubY() {
		return ogreClubY;
	}

	public void setOgreClubY(int ogreClubY) {
		this.ogreClubY = ogreClubY;
	}
}
