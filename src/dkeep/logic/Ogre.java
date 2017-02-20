package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Ogre extends Entity{
	private Point ogreClubCoords;
	private char ogreChar;
	private char ogreClubChar;
	
	public Ogre(Point ogreCoords, Point ogreClubCoords){
		this.coords = ogreCoords;
		this.ogreClubCoords = ogreClubCoords;
		this.ogreChar = ' ';
		this.ogreClubChar = ' ';
	}
	
	//Generate random direction for ogre
	public char generateNewDirection() {
		
		Random rand = new Random();
		int n = rand.nextInt(4);
		
		switch(n) {
		case 0:
			return 'w';
		case 1:
			return 's';
		case 2:
			return 'a';
		case 3:
			return 'd';
		default:
			return ' ';
		}
	}

	public Point getOgreCoords() {
		return coords;
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
		this.coords = ogreCoords;
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