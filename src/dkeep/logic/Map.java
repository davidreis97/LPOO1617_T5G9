package dkeep.logic;

import java.awt.Point;

/**
 * Interface defining common methods for Map classes.
 */
public interface Map{
	
	/**
	 * Attempts to move an Entity on the map. Checks collision with static map objects and special interactions.
	 * 
	 * @param  coords     current Entity coords
	 * @param  entityType Entity derived class name
	 * @param  index      index of current Entity from entities array
	 * @return            whether movement was possible
	 */
	public boolean doMove(Point coords, String entityType, int index);

	/**
	 * Initializes Game.
	 * 
	 * @param  guardType type of guard to instantiate (Rookie, Drunken, Suspicious)
	 */
	public void initMap(String guardType);
	
	/**
	 * Changes to next map.
	 */
	public void nextMap();
	
	/**
	 * Checks if doors should open. May use entityType to base decision on.
	 * 
	 * @param  entityType Entity derived class name
	 */
	public void checkDoors(String entityType);
	
	/**
	 * @return clone of map array
	 */
	public char[][] getMap();
}