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


}
