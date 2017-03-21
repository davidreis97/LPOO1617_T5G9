package dkeep.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import dkeep.logic.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SimpleGraphicsPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	int imagex = 32, imagey = 32;
	
	BufferedImage hero;
	BufferedImage wall;
	BufferedImage guard;
	BufferedImage openDoor;
	BufferedImage closedDoor;
	BufferedImage key;
	BufferedImage ogre;
	BufferedImage mace;
	
	public SimpleGraphicsPanel(boolean levelEditor) {
	       
		   if (levelEditor){
			   addMouseListener(this);
			   addMouseMotionListener(this);
		   }else{
			   addKeyListener(this);
		   }
	       
	       try {
	    	   hero = ImageIO.read(new File("resources/crawl-tiles/player/base/centaur_brown_f.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading hero");
	       }
	       
	       try {
	    	   wall = ImageIO.read(new File("resources/crawl-tiles/dc-dngn/wall/brick_dark2.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading wall");
	       }
	       
	       try {
	    	   guard = ImageIO.read(new File("resources/crawl-tiles/player/base/kenku_winged_m.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading guard");
	       }
	       
	       try {
	    	   openDoor = ImageIO.read(new File("resources/crawl-tiles/dc-dngn/dngn_open_door.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading open door");
	       }
	       
	       try {
	    	   closedDoor = ImageIO.read(new File("resources/crawl-tiles/dc-dngn/dngn_closed_door.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading closed door");
	       }
	       
	       try {
	    	   key = ImageIO.read(new File("resources/crawl-tiles/UNUSED/other/key.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading key");
	       }
	       
	       try {
	    	   ogre = ImageIO.read(new File("resources/crawl-tiles/player/base/troll_m.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading ogre");
	       }
	       
	       try {
	    	   mace = ImageIO.read(new File("resources/crawl-tiles/item/weapon/giant_spiked_club.png"));
	       } catch (IOException e) {
	    	   System.out.println("Error loading mace");
	       }
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!Game.getState().equals("Playing")){
			return;
		}
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			Game.updateGame('w', false);
			break;
		case KeyEvent.VK_DOWN:
			Game.updateGame('s', false);
			break;
		case KeyEvent.VK_LEFT:
			Game.updateGame('a', false);
			break;
		case KeyEvent.VK_RIGHT:
			Game.updateGame('d', false);
			break;
		};
		GUI.updateGUIStatus();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		KeepMap map;
		char [][] charMap;
		switch(LevelEditor.getNextChar()){
		case 'H':
			Game.setHeroIndex(Game.getEntities().size());
			Game.getEntities().add(new Hero(new Point((int) (e.getX()/31), (int) (e.getY()/31)), 'H'));
			break;
		case 'k':
			map = (KeepMap)Game.getMapObject();
			charMap = Game.getMapObject().getMap();
			charMap[(int)(e.getY()/31)][(int)(e.getX()/31)] = 'k';
			map.setMap(charMap);
			break;
		case 'X':
			map = (KeepMap)Game.getMapObject();
			charMap = Game.getMapObject().getMap();
			charMap[(int)(e.getY()/31)][(int)(e.getX()/31)] = 'X';
			map.setMap(charMap);
			break;
		case 'I':
			map = (KeepMap)Game.getMapObject();
			charMap = Game.getMapObject().getMap();
			charMap[(int)(e.getY()/31)][(int)(e.getX()/31)] = 'I';
			map.setMap(charMap);
			break;
		case '0':
			Game.getEntities().add(new Ogre(new Point((int) (e.getX()/31), (int) (e.getY()/31)), '0'));
			LevelEditor.askForMace();
			break;
		case '*':
			Game.getEntities().add(new Club(new Point((int) (e.getX()/31), (int) (e.getY()/31)), '*'));
			LevelEditor.finishedMacePlacement();
			break;
		case ' ':
			map = (KeepMap)Game.getMapObject();
			charMap = Game.getMapObject().getMap();
			charMap[(int)(e.getY()/31)][(int)(e.getX()/31)] = ' ';
			map.setMap(charMap);
			break;
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		char [][]mapCopy;
		
		try {
			mapCopy = Game.getMap();
		}catch (NullPointerException npe){
			return;
		}
		ArrayList<Entity> entities = Game.getEntities();
		
		//Fill map with dynamic objects (entities)
		for(int i = entities.size() - 1; i >= 0; i--) {
			mapCopy[entities.get(i).getCoords().y][entities.get(i).getCoords().x] = entities.get(i).getRepresentation();
		}

		//Prints map with static and dynamic objects
		for(int i = 0; i< mapCopy.length; i++) {
			for(int j = 0; j < mapCopy.length; j++) {
				switch(mapCopy[i][j]){
					case 'X':
						g.drawImage(wall, j*imagex, i*imagey, null);
						break;
					case 'H':
						g.drawImage(hero, j*imagex, i*imagey, null);
						break;
					case 'I':
						g.drawImage(closedDoor, j*imagex, i*imagey, null);
						break;
					case 'S':
						g.drawImage(openDoor, j*imagex, i*imagey, null);
						break;
					case 'G':
						g.drawImage(guard, j*imagex, i*imagey, null);
						break;
					case 'k':
						g.drawImage(key, j*imagex, i*imagey, null);
						break;
					case '*':
						g.drawImage(mace, j*imagex, i*imagey, null);
						break;
					case '0':
						g.drawImage(ogre, j*imagex, i*imagey, null);
						break;
					case 'K':
						g.drawImage(hero, j*imagex, i*imagey, null);
						break;
				};
			}
		}
	}
	
}
