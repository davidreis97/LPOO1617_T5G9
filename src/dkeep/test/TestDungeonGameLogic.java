package dkeep.test;


import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import dkeep.cli.CLI;
import dkeep.logic.*;

import org.junit.Before;
import org.junit.Test;

public class TestDungeonGameLogic {
	
	static Game game;

	@Before
	public void setUp() throws Exception {
		game = new Game("Dungeon");
	}
	

	@Test
	public void heroMovesToFreeCell() {
		Point initialPoint = new Point(1,1);
		Point finalPoint = new Point(2,1);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
		game.updateGame('d',true);
		assertEquals(finalPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
	}
	
	@Test
	public void heroMovesToWall() {
		Point initialPoint = new Point(1,1);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
		game.updateGame('w',true);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
	}
	
	@Test
	public void heroMovesAdjacentToGuard() {
		Point initialPoint = new Point(7,3);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		assertNotEquals("Lose",game.getState());
		game.updateGame('w',true);
		assertEquals("Lose",game.getState());
	}
	
	@Test
	public void heroMovesToClosedExitDoor() {
		Point initialPoint = new Point(1,5);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		game.updateGame('a',true);
		assertEquals(initialPoint,game.getEntities().get(game.getHeroIndex()).getCoords());
	}
	
	@Test
	public void heroMovesToLever() {
		Point initialPoint = new Point(8,8);
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		assertEquals('I',game.getMap()[5][0]);
		assertEquals('I',game.getMap()[6][0]);
		assertEquals('I',game.getMap()[3][2]);
		game.updateGame('a',true);
		assertEquals('S',game.getMap()[5][0]);
		assertEquals('S',game.getMap()[6][0]);
		assertEquals('I',game.getMap()[3][2]);
	}
	
	@Test
	public void heroMovesToOpenExitDoor() {
		Point initialPoint = new Point(1,5);
		game.getMapObject().openDoors();
		game.getEntities().get(game.getHeroIndex()).setCoords(initialPoint);
		game.updateGame('a',true);
		assertEquals("KeepMap",game.getMapObject().getClass().getSimpleName());
	}

}