import java.awt.Point;
import java.util.Random;

public class Ogre {
	private Point ogreCoords;
	private Point ogreClubCoords;
	private char ogreChar;
	private char ogreClubChar;
	
	public Ogre(Point ogreCoords, Point ogreClubCoords){
		this.ogreCoords = ogreCoords;
		this.ogreClubCoords = ogreClubCoords;
		this.ogreChar = ' ';
		this.ogreClubChar = ' ';
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

	public Point getOgreCoords() {
		return ogreCoords;
	}

	public Point getOgreClubCoords() {
		return ogreClubCoords;
	}

	public char getOgreChar() {
		return ogreChar;
	}

	public char getOgreClubChar() {
		return ogreClubChar;
	}

	public void setOgreCoords(Point ogreCoords) {
		this.ogreCoords = ogreCoords;
	}

	public void setOgreClubCoords(Point ogreClubCoords) {
		this.ogreClubCoords = ogreClubCoords;
	}

	public void setOgreChar(char ogreChar) {
		this.ogreChar = ogreChar;
	}

	public void setOgreClubChar(char ogreClubChar) {
		this.ogreClubChar = ogreClubChar;
	}
}