import java.awt.Point;

public class Hero {
	private Point heroCoords;
	private char heroChar;
	
	public Hero(Point heroCoords) {
		this.heroCoords = heroCoords;
		this.heroChar = 'H';
	}

	public Point getHeroCoords() {
		return heroCoords;
	}

	public char getHeroChar() {
		return heroChar;
	}

	public void setHeroCoords(Point heroCoords) {
		this.heroCoords = heroCoords;
	}

	public void setHeroChar(char heroChar) {
		this.heroChar = heroChar;
	}
}