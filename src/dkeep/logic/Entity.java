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
	
	public void setCoords(Point coords) {
		this.coords = coords;
	}

	public void setRepresentation(char representation) {
		this.representation = representation;
	}
	
	public abstract void nextMovement(int index);
}