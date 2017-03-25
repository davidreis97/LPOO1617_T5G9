package dkeep.test;


import static org.junit.Assert.*;

import java.awt.Point;
import dkeep.logic.*;

import org.junit.Before;
import org.junit.Test;

public class TestDungeonGameLogic {
	
	static Game game;

	@Before
	public void setUp() throws Exception {
		game = new Game("Dungeon", "Rookie", 1);
	}
	

	@Test
	public void heroMovesToFreeCell() {
		Point initialPoint = new Point(1,1);
		Point finalPoint = new Point(2,1);
		assertEquals(initialPoint,Game.getEntities().get(Game.getHeroIndex()).getCoords());
		Game.updateGame('d',true);
		assertEquals(finalPoint,Game.getEntities().get(Game.getHeroIndex()).getCoords());
	}
	
	@Test
	public void heroMovesToWall() {
		Point initialPoint = new Point(1,1);
		assertEquals(initialPoint,Game.getEntities().get(Game.getHeroIndex()).getCoords());
		Game.updateGame('w',true);
		assertEquals(initialPoint,Game.getEntities().get(Game.getHeroIndex()).getCoords());
	}
	
	@Test
	public void heroMovesAdjacentToGuard() {
		Point initialPoint = new Point(7,3);
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		assertNotEquals("Lose",Game.getState());
		Game.updateGame('w',true);
		assertEquals("Lose",Game.getState());
	}
	
	@Test
	public void heroMovesToClosedExitDoor() {
		Point initialPoint = new Point(1,5);
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		Game.updateGame('a',true);
		assertEquals(initialPoint,Game.getEntities().get(Game.getHeroIndex()).getCoords());
	}
	
	@Test
	public void heroMovesToLever() {
		Point initialPoint = new Point(8,8);
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		assertEquals('I',Game.getMap()[5][0]);
		assertEquals('I',Game.getMap()[6][0]);
		assertEquals('I',Game.getMap()[3][2]);
		Game.updateGame('a',true);
		assertEquals('S',Game.getMap()[5][0]);
		assertEquals('S',Game.getMap()[6][0]);
		assertEquals('I',Game.getMap()[3][2]);
	}
	
	@Test
	public void heroMovesToOpenExitDoor() {
		Point initialPoint = new Point(1,5);
		Game.getMapObject().openDoors();
		Game.getEntities().get(Game.getHeroIndex()).setCoords(initialPoint);
		Game.updateGame('a',true);
		assertEquals("KeepMap",Game.getMapObject().getClass().getSimpleName());
	}
	
	@Test(timeout = 1000)
	public void drunkGuardSleepsReverses(){
		game = new Game("Dungeon", "Drunken", 1);
		
		int currentStep, oldStep;
		boolean walksForward = false, walksBackwards = false, sleeps = false;
		
		Guard guard = (Guard) Game.getEntities().get(Game.getHeroIndex()+1);
		oldStep = guard.getStepCounter();
		
		while (!walksForward || !walksBackwards || !sleeps){
			Game.updateGame('a', true);
			currentStep = guard.getStepCounter();
			
			if (oldStep < currentStep){
				walksForward = true;
			}else if(oldStep > currentStep){
				walksBackwards = true;
			}else{ //oldStep == currentStep
				sleeps = true;
			}
			
			oldStep = currentStep;
		}
	}
	
	@Test(timeout = 1000)
	public void suspiciousGuardReverses(){
		game = new Game("Dungeon", "Suspicious", 1);
		
		int currentStep, oldStep;
		boolean walksForward = false, walksBackwards = false;
		
		Guard guard = (Guard) Game.getEntities().get(Game.getHeroIndex() + 1);
		oldStep = guard.getStepCounter();
		
		while (!walksForward || !walksBackwards){
			Game.updateGame('a', true);
			currentStep = guard.getStepCounter();
			
			if (oldStep < currentStep){
				walksForward = true;
			}else if(oldStep > currentStep){
				walksBackwards = true;
			}else{
				fail("Suspicious guard stopped");
			}
			
			oldStep = currentStep;
		}
	}

}
