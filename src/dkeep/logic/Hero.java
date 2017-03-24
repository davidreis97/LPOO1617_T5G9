package dkeep.logic;

import java.awt.Point;

public class Hero extends Entity {

	boolean isArmed;
	
	public Hero(Point coords, char representation) {
		super(coords, representation);
		this.isArmed = false;
	}
	
	public Hero(Point coords, char representation, boolean isArmed) {
		super(coords, representation);
		this.isArmed = isArmed;
	}
	
	public void nextMovement(int index) {
		return;
	}
	
	public boolean doCollision(int index, int stage) {
		int adjacent = Game.isAdjacentTo(index);
		
		if(adjacent == -1) {
			return false;
		} 
		
		if(stage == 1) {
			if(Game.getEntities().get(adjacent) instanceof Ogre) {
				
				Ogre ogre = (Ogre) Game.getEntities().get(adjacent);
				if(!ogre.isStunned) {
					ogre.setStunned(true);
				}
				return false;
			} else if(Game.getEntities().get(adjacent) instanceof Club) {
				
				return true;
			} else if(Game.getEntities().get(adjacent) instanceof Guard) {
				
				return true;
			}
			
		} else if(stage == 2) {
			if(Game.getEntities().get(adjacent) instanceof Ogre) {
				Ogre ogre = (Ogre) Game.getEntities().get(adjacent);
				if(ogre.isStunned()) {
					return false;
				} else return true;
				
			} else if(Game.getEntities().get(adjacent) instanceof Club) {
				return true;
				
			} else if(Game.getEntities().get(adjacent) instanceof Guard) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isArmed() {
		return isArmed;
	}

	public void setArmed(boolean isArmed) {
		this.isArmed = isArmed;
		if (isArmed){
			this.representation = 'A';
		}else{
			this.representation = 'H';
		}
	}
}
