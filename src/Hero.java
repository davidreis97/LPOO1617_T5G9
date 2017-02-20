import java.awt.Point;

public class Hero {
	private Point heroCoords;
	
	public Hero(Point heroCoords) {
		this.heroCoords = heroCoords;
	}

	public Point getHeroCoords() {
		return heroCoords;
	}

	public void setHeroCoords(Point heroCoords) {
		this.heroCoords = heroCoords;
	}
}