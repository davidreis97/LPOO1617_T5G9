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
		int result;
		
		switch(nextMove) {
		case 0:
			result = Game.entityCol(index, 'w');
			
			if(result == -1) {
				this.coords = Game.move(this.coords, 'w', "Club", index);
			} else if(Game.getEntities().get(result) instanceof Hero) {
				this.coords = Game.move(this.coords, 'w', "Club", index);
				Game.setState("Lose");
			}
			break;
		case 1:
			result = Game.entityCol(index, 's');
			
			if(result == -1) {
				this.coords = Game.move(this.coords, 's', "Club", index);
			} else if(Game.getEntities().get(result) instanceof Hero) {
				this.coords = Game.move(this.coords, 's', "Club", index);
				Game.setState("Lose");
			}
			break;
		case 2:
			result = Game.entityCol(index, 'a');
			
			if(result == -1) {
				this.coords = Game.move(this.coords, 'a', "Club", index);
			} else if(Game.getEntities().get(result) instanceof Hero) {
				this.coords = Game.move(this.coords, 'a', "Club", index);
				Game.setState("Lose");
			}
			break;
		case 3:
			result = Game.entityCol(index, 'd');
			
			if(result == -1) {
				this.coords = Game.move(this.coords, 'd', "Club", index);
			} else if(Game.getEntities().get(result) instanceof Hero) {
				this.coords = Game.move(this.coords, 'd', "Club", index);
				Game.setState("Lose");
			}
			break;
		}
	}
}
