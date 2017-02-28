package dkeep.logic;

import java.awt.Point;

public abstract class Entity {
	protected Point coords;
	protected char representation;
	
	public Point getCoords() {
		return coords;
	}
	public char getRepresentation() {
		return representation;
	}
}