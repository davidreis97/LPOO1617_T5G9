package dkeep.logic;

import java.awt.Point;
import java.io.Serializable;

/**
 * Class that stores information common to all dynamic game objects.<br>
 * Stores coords in Point format and object representation in char format.
 */
public abstract class Entity implements Serializable {
	
	protected Point coords;
	protected char representation;
	
	Entity(Point coords, char representation) {
		this.coords = coords;
		this.representation = representation;
	}
	
	/**
	 * @return Entity coords
	 */
	public Point getCoords() {
		return coords;
	}
	
	/**
	 * @return Entity char representation
	 */
	public char getRepresentation() {
		return representation;
	}
	
	/**
	 * @param coords new coords for Entity
	 */
	public void setCoords(Point coords) {
		this.coords = coords;
	}

	/**
	 * @param representation new char representation of Entity
	 */
	public void setRepresentation(char representation) {
		this.representation = representation;
	}
	
	/**
	 * Implemented by derived classes. Handles movement of entities and updates game data accordingly.
	 * 
	 * @param  index index of current Entity from entities array
	 */
	public abstract void nextMovement(int index);
}