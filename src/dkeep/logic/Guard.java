package dkeep.logic;

import java.awt.Point;

public abstract class Guard extends Entity {
	
	protected int stepCounter;
	protected char guardPath[];

	public Guard(Point coords, char representation) {
		super(coords, representation);
		this.stepCounter = 0;
		this.guardPath = new char[]{'a', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a', 'a', 's',
                                    'd', 'd', 'd', 'd', 'd', 'd', 'd', 'w', 'w', 'w', 'w', 'w'};
	}
	
	public Guard(Point coords, char representation, char[] guardPath) {
		super(coords, representation);
		this.stepCounter = 0;
		this.guardPath = guardPath;
	}
	
	public abstract void nextMovement(int index);
}
