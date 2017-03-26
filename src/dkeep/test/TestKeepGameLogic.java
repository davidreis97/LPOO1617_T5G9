package dkeep.test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import dkeep.logic.Game;
import dkeep.logic.KeepMap;
import dkeep.logic.Hero;

public class TestKeepGameLogic {

	static Game game;

	@Before
	public void setUp() throws Exception {
		new Game("Keep", "Rookie", 0);
	}

	@Test
	public void heroMovesAdjacentToOgre() {
		
		new Game("Keep", "Rookie", 1);
		
		Hero hero = (Hero) Game.getEntities().get(Game.getHeroIndex());
		hero.setCoords(new Point(Game.getEntities().get(1).getCoords().x, Game.getEntities().get(1).getCoords().y + 2));
		hero.setArmed(false);
		assertNotEquals("Lose", Game.getState());
		Game.updateGame('w', false);
		assertEquals("Lose", Game.getState());
	}
	
	@Test
	public void heroMovesToKey() {
		Hero hero = (Hero) Game.getEntities().get(Game.getHeroIndex());
		
		hero.setCoords(new Point(7, 1));
		
		assertEquals('k', Game.getMap()[1][8]);
		assertEquals(false, ((KeepMap) Game.getMapObject()).getHeroHasKey());
		Game.updateGame('d', false);
		assertEquals(true, ((KeepMap) Game.getMapObject()).getHeroHasKey());
		assertEquals('K', hero.getRepresentation());
	}
	
	@Test
	public void heroMovesToDoorWithoutKey() {
		
		Point initialPoint = new Point(1,1);
		assertEquals(false,((KeepMap) Game.getMapObject()).getHeroHasKey());
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		Game.updateGame('a', false);
		assertEquals(initialPoint, Game.getEntities().get(Game.getHeroIndex()).getCoords());
		assertEquals('I', Game.getMap()[1][0]);
	}
	
	@Test
	public void heroMovesToDoorWithKey() {
		
		Point initialPoint = new Point(7, 1);
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		assertEquals('k', Game.getMap()[1][8]);
		assertEquals(false,((KeepMap) Game.getMapObject()).getHeroHasKey());
		Game.updateGame('d', false);
		assertEquals(true,((KeepMap) Game.getMapObject()).getHeroHasKey());
		initialPoint = new Point(1,1);
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		Game.updateGame('a', false);
		assertEquals(initialPoint, Game.getEntities().get(Game.getHeroIndex()).getCoords());
		assertEquals('S', Game.getMap()[1][0]);
	}
	
	@Test
	public void heroMovesToOpenExitDoor() {
		
		Point initialPoint = new Point(1, 1);
		KeepMap temp = (KeepMap) Game.getMapObject();
		temp.setHeroHasKey(true);
		Game.setMapObject(temp);
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		assertEquals(initialPoint,Game.getEntities().get(Game.getHeroIndex()).getCoords());
		Game.updateGame('a', false);
		Game.updateGame('a', false);
		assertEquals("Win", Game.getState());
	}
	
	@Test(timeout = 1000)
	public void ogreRandomMovement() {
		
		new Game("Keep", "Rookie", 1);
		
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
		}
	}
	
	@Test
	public void keepMapConstruction() {
		Random random = new Random();
		int width = random.nextInt(6) + 5;
		int height = random.nextInt(6) + 5;
		
		KeepMap keep = new KeepMap(width, height);
		
		assertEquals(width, keep.getWidth());
		assertEquals(height, keep.getHeight());
	}
}
