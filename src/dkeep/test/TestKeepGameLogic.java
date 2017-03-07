package dkeep.test;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import dkeep.logic.Game;
import dkeep.logic.KeepMap;

public class TestKeepGameLogic {

	static Game game;

	@Before
	public void setUp() throws Exception {
		game = new Game("Keep");
	}
	

	@Test
	public void heroMovesAdjacentToOgre() {
		Point initialPoint = new Point(5,1);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		assertNotEquals("Lose",game.getState());
		game.updateGame('a',true);
		assertEquals("Lose",game.getState());
	}
	
	@Test
	public void heroMovesToKey() {
		Point initialPoint = new Point(7,1);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		assertEquals('k',game.getMap()[1][8]);
		assertEquals(false,((KeepMap) game.getMapObject()).getHeroHasKey());
		game.updateGame('d',true);
		assertEquals(true,((KeepMap) game.getMapObject()).getHeroHasKey());
	}
	
	@Test
	public void heroMovesToDoorWithoutKey() {
		Point initialPoint = new Point(1,1);
		assertEquals(false,((KeepMap) game.getMapObject()).getHeroHasKey());
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		game.updateGame('a',true);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
		assertEquals('I',game.getMap()[1][0]);
	}
	
	@Test
	public void heroMovesToDoorWithKey() {
		Point initialPoint = new Point(7,1);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		assertEquals('k',game.getMap()[1][8]);
		assertEquals(false,((KeepMap) game.getMapObject()).getHeroHasKey());
		game.updateGame('d',true);
		assertEquals(true,((KeepMap) game.getMapObject()).getHeroHasKey());
		initialPoint = new Point(1,1);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		game.updateGame('a',true);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
		assertEquals('S',game.getMap()[1][0]);
	}
	
	@Test
	public void heroMovesToOpenExitDoor() {
		Point initialPoint = new Point(1,1);
		KeepMap temp = (KeepMap) game.getMapObject();
		temp.setHeroHasKey(true);
		game.setMapObject(temp);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
		game.updateGame('a',true);
		game.updateGame('a',true);
		assertEquals("Win",game.getState());
	}
	
	@Test(timeout=1000)
	public void ogreRandomMovement(){ //TODO Remove hero from game
		String outcomes = "";
		String eligible = "RR RL RU RD LR LL LU LD UU UD UR UL DD DU DR DL ";
		while (outcomes.length() != eligible.length()){
			String temp = "";
			for(int i = 1; i <= 2; i++){
				Point initialPoint = Game.getEntities().get(i).getCoords();
				Game.getEntities().get(i).nextMovement(i);	
				Point finalPoint = Game.getEntities().get(i).getCoords();
				if (finalPoint.x - initialPoint.x == 1){
					temp += "R";
				}else if(finalPoint.x - initialPoint.x == -1){
					temp += "L";
				}
				
				if (finalPoint.y - initialPoint.y == 1){
					temp += "D";
				}else if(finalPoint.y - initialPoint.y == -1){
					temp += "U";
				}
				
			}
			
			if(eligible.contains(temp) && !outcomes.contains(temp) && temp.length() == 2){
				outcomes += temp + " ";
			}
			
			System.out.println("Outcomes: " + outcomes);
			System.out.println("Temp: " + temp);
		}
	}


}
