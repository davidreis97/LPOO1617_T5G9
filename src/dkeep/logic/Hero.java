package dkeep.logic;

import java.awt.Point;

public class Hero extends Entity {
	private char heroChar;
	private boolean isArmed;
	
	public Hero(Point heroCoords, boolean armed) {
		this.coords = heroCoords;
		this.heroChar = 'H';
		this.isArmed = armed;
	}

	public Point getHeroCoords() {
		return coords;
	}

	public char getHeroChar() {
		if(this.heroChar != 'K') {
			if(this.isArmed) {
				return 'A';
			} else return 'H';
		}
		return heroChar;
	}

	public boolean isArmed() {
		return isArmed;
	}
	
	public void setHeroCoords(Point heroCoords) {
		this.coords = heroCoords;
	}

	public void setHeroChar(char heroChar) {
		this.heroChar = heroChar;
	}

	public void setArmed(boolean isArmed) {
		this.isArmed = isArmed;
	}
}