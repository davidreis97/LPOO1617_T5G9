package dkeep.logic;

import java.awt.Point;

public interface Map {
	
	public char[][] getMap();

	public boolean doMove(Point coords);
}