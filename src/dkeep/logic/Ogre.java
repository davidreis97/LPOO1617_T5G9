package dkeep.logic;

import java.awt.Point;

public class Ogre extends Entity {

	private Club club;
	
	public Ogre(Point coords, char representation) {
		super(coords, representation);
	}
	
	public void nextMovement() {
		this.coords = Game.move(this.coords, 's');
	}
}
