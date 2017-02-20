package dkeep.logic;

import java.awt.Point;

public class Hero extends Entity{
	private char heroChar;
	
	public Hero(Point heroCoords) {
		this.coords = heroCoords;
		this.heroChar = 'H';
	}

	public Point getHeroCoords() {
		return coords;
	}

	public char getHeroChar() {
		return heroChar;
	}

	public void setHeroCoords(Point heroCoords) {
		this.coords = heroCoords;
	}

	public void setHeroChar(char heroChar) {
		this.heroChar = heroChar;
	}
}