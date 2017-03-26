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
	
	private char nextDirection() {
		Random random = new Random();
		int nextMove = random.nextInt(4);
		
		if(nextMove == 0) {
			return 'w';
		} else if(nextMove == 1) {
			return 's';
		} else if(nextMove == 2) {
			return 'a';
		} else if(nextMove == 3) {
			return 'd';
		}
		
		return 'w';
	}
	
	public void nextMovement(int index) {
	
		if(isStunned) {
			stunCounter--;
			if(stunCounter <= 0) {
				isStunned = false;
				representation = '0';
			}
		} else coords = Game.move(coords, nextDirection(), "Ogre", index);
		
		try{
			Game.getEntities().get(index + 1).setCoords(coords); //CAUTION assumes ogre's club is always the next entities element
		}catch (Exception e){
			Game.getEntities().add(new Ogre(new Point(coords), '*'));			
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
