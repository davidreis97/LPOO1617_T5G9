package dkeep.logic;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Class that implements Hero object functionality, considered the Player character.
 */
public class Hero extends Entity {

	boolean isArmed;
	
	/**
	 * Class constructor, assumes Hero is not armed.
	 *
	 * @param  coords         coords for Hero
	 * @param  representation char representation of Hero
	 */
	public Hero(Point coords, char representation) {
		super(coords, representation);
		this.isArmed = false;
	}
	
	/**
	 * Class constructor, allows setting if Hero is armed.
	 *
	 * @param  coords         coords for Hero
	 * @param  representation char representation of Hero
	 * @param  isArmed        whether Hero is armed
	 */
	public Hero(Point coords, char representation, boolean isArmed) {
		super(coords, representation);
		this.isArmed = isArmed;
	}
	
	/**
	 * Stub implementation. Game implements Hero movement in updateGame method.
	 * 
	 * @param  index index of current Entity from entities array
	 */
	@Override
	public void nextMovement(int index) { 
		return;
	}
	
	/**
	 * Handles collision between Hero and adjacent entities. Stage 1 (Hero moves) has the Hero attacking with the sword.
	 * 
	 * @param  adjacent array of indices of adjacent entities from entities array
	 * @return          whether Hero has collided and as such lost the game
	 */
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
	
	/**
	 * Handles collision between Hero and adjacent entities. Stage 2 (other entities move) has no Hero attack so more entities can defeat the Hero.
	 * 
	 * @param  adjacent array of indices of adjacent entities from entities array
	 * @return          whether Hero has collided and as such lost the game
	 */
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
	
	/**
	 * @return whether Hero is armed
	 */
	public boolean isArmed() {
		return isArmed;
	}

	/**
	 * Set whether Hero is armed, also changes representation.
	 * 
	 * @param  isArmed whether Hero is armed
	 */
	public void setArmed(boolean isArmed) {
		this.isArmed = isArmed;
		if (isArmed) {
			this.representation = 'A';
		} else {
			this.representation = 'H';
		}
	}
}
