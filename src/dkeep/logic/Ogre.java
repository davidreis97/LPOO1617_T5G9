package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Ogre extends Entity {

	boolean isStunned;
	int stunCounter;
	
	public Ogre(Point coords, char representation) {
		super(coords, representation);
		this.isStunned = false;
		this.stunCounter = 0;
	}
	
	public void nextMovement(int index) {
		
		Random rand = new Random();
		int nextMove = rand.nextInt(4);
		
		if(this.isStunned) {
			stunCounter--;
			if(stunCounter <= 0) {
				this.isStunned = false;
				this.representation = '0';
			}
			nextMove = 4;
		}

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
		default:
			break;
		}
		
		try{
			Game.getEntities().get(index + 1).setCoords(this.coords); //CAUTION assumes ogre's club is always the next entities element
		}catch (Exception e){
			Game.getEntities().add(new Ogre(new Point(this.coords), '*'));			
		}
	}

	public boolean isStunned() {
		return isStunned;
	}

	public void setStunned(boolean isStunned) {
		this.stunCounter = 3;
		this.representation = '8';
		this.isStunned = isStunned;
	}
}
