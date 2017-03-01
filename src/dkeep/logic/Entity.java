package dkeep.logic;

import java.awt.Point;

public abstract class Entity {
	protected Point coords;
	protected char representation;
	
	Entity(Point coords, char representation) {
		this.coords = coords;
		this.representation = representation;
	}
	
	public Point getCoords() {
		return coords;
	}
	
	public char getRepresentation() {
		return representation;
	}
	
	//TODO maybe implement on all entities and reuse?
	public abstract void nextMovement();
}