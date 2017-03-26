package dkeep.logic;

import java.awt.Point;
import java.util.ArrayList;

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
	
	public boolean doCollisionStage1(ArrayList<Integer> adjacent) {
		
		for(int i = 0; i < adjacent.size(); i++) {
				if(Game.getEntities().get(adjacent.get(i)) instanceof Ogre) {
					
					Ogre ogre = (Ogre) Game.getEntities().get(adjacent.get(i));
					if(!ogre.isStunned && this.isArmed) {
						ogre.setStunned(true);
					}

				} else if(Game.getEntities().get(adjacent.get(i)) instanceof Club) {
					
					if(((Club) Game.getEntities().get(adjacent.get(i))).isActive()) return true;
				} else if(Game.getEntities().get(adjacent.get(i)) instanceof Guard) {
					
					Guard guard = (Guard) Game.getEntities().get(adjacent.get(i));
					if(!guard.isSleeping())	return true;
				}
			}
		
		return false;
	}
	
	public boolean doCollisionStage2(ArrayList<Integer> adjacent) {
		
		for(int i = 0; i < adjacent.size(); i++) {
			if(Game.getEntities().get(adjacent.get(i)) instanceof Ogre) {
				
				Ogre ogre = (Ogre) Game.getEntities().get(adjacent.get(i));
				if(!ogre.isStunned()) {
					return true;
				}
			} else if(Game.getEntities().get(adjacent.get(i)) instanceof Club) {
				
				if(((Club) Game.getEntities().get(adjacent.get(i))).isActive()) return true;
			} else if(Game.getEntities().get(adjacent.get(i)) instanceof Guard) {
				
				Guard guard = (Guard) Game.getEntities().get(adjacent.get(i));
				if(!guard.isSleeping())	return true;
			}
		}
		
		return false;
	}
		
	public boolean isArmed() {
		return isArmed;
	}

	public void setArmed(boolean isArmed) {
		this.isArmed = isArmed;
		if (isArmed) {
			this.representation = 'A';
		} else {
			this.representation = 'H';
		}
	}
}
