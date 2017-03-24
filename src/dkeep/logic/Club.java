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
			if(Game.entityCol(index, 'w') == -1) {
				this.coords = Game.move(this.coords, 'w', "Club", index);
			}
			break;
		case 1:
			if(Game.entityCol(index, 's') == -1) {
				this.coords = Game.move(this.coords, 's', "Club", index);
			}
			break;
		case 2:
			if(Game.entityCol(index, 'a') == -1) {
				this.coords = Game.move(this.coords, 'a', "Club", index);
			}
			break;
		case 3:
			if(Game.entityCol(index, 'd') == -1) {
				this.coords = Game.move(this.coords, 'd', "Club", index);
			}
			break;
		}
	}
}
