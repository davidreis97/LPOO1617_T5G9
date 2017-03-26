package dkeep.logic;

import java.awt.Point;

public interface Map{
	
	public char[][] getMap();

	public boolean doMove(Point coords, String entityType, int index);

	public void initMap(String guardtype);
	
	public void nextMap();
	
	public void checkDoors(String entityType);
}