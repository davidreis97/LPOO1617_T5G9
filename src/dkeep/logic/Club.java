package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Club extends Entity {

	public Club(Point coords, char representation) {
		super(coords, representation);
	}
	
	public void nextMovement(int index) {
		Random rand = new Random();
		int nextMove = rand.nextInt(4);
		
		switch(nextMove) {
		case 0:
			this.coords = Game.move(this.coords, 'w', "Club", index);
			break;
		case 1:
			this.coords = Game.move(this.coords, 's', "Club", index);
			break;
		case 2:
			this.coords = Game.move(this.coords, 'a', "Club", index);
			break;
		case 3:
			this.coords = Game.move(this.coords, 'd', "Club", index);
			break;
		}
	}
}
