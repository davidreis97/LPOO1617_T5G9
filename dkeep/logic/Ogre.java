package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Ogre extends Entity {

	public Ogre(Point coords, char representation) {
		super(coords, representation);
	}
	
	public void nextMovement(int index) {
		Random rand = new Random();
		int nextMove = rand.nextInt(4);

		switch(nextMove) {
		case 0:
			this.coords = Game.move(this.coords, 'w', "Ogre", index);
			break;
		case 1:
			this.coords = Game.move(this.coords, 's', "Ogre", index);
			break;
		case 2:
			this.coords = Game.move(this.coords, 'a', "Ogre", index);
			break;
		case 3:
			this.coords = Game.move(this.coords, 'd', "Ogre", index);
			break;
		}
		
		Game.getEntities().get(index + 1).setCoords(this.coords); //CAUTION assumes ogre's club is always the next entities element
	}
}
