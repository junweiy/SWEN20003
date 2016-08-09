package GameFrame;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import Items.Boost;
import Items.Item;
import Items.OilCan;
import Items.Tomato;
import Items.TomatoProjectile;
import Karts.Dog;
import Karts.Donkey;
import Karts.Elephant;
import Karts.Kart;
import Karts.Octopus;
import Tools.Angle;
import Tools.Point;

/**
 * Represents the entire game world. (Designed to be instantiated just once for
 * the whole game).
 */

public class World {
	// Y value of finish line
	public static double ENDY = 1026;
	// Nearest distance for collision
	public static double NEARDIST = 40;
	// 90 degrees
	public static double NINETYDEG = 90;
	// 180 degrees
	public static double ONEEIGHTYDEG = 180;
	private Donkey donkey;
	private Elephant elephant;
	private Dog dog;
	private Octopus octopus;
	private Camera camera;
	private Panel panel;
	private ArrayList<Kart> players;
	private ArrayList<Item> items;
	private ArrayList<Item> hazards;
	private ArrayList<Point> waypoints;
	private int currRanking;
	private TiledMap map;

	/**
	 * Create a new World object.
	 * 
	 * @throws SlickException
	 * @throws NumberFormatException
	 */
	public World() throws SlickException, NumberFormatException {
		// Read item info into array
		this.items = new ArrayList<Item>();
		try {
			String line;
			FileReader fileReader = new FileReader("data/items.txt");
			BufferedReader br = new BufferedReader(fileReader);
			Point tempPoint;
			while ((line = br.readLine()) != null) {
				String props[] = line.split(",");
				tempPoint = new Point(Double.parseDouble(props[1]), Double.parseDouble(props[2]));
				if (props[0].equals("Oil can")) {
					items.add(new OilCan(tempPoint));
				} else if (props[0].equals("Tomato")) {
					items.add(new Tomato(tempPoint));
				} else if (props[0].equals("Boost")) {
					items.add(new Boost(tempPoint));
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Read waypoints from txt file
		this.waypoints = new ArrayList<Point>();
		try {
			String line;
			FileReader fileReader2 = new FileReader("data/waypoints.txt");
			BufferedReader br2 = new BufferedReader(fileReader2);
			while ((line = br2.readLine()) != null) {
				String nums[] = line.split(",");
				waypoints.add(new Point(Double.parseDouble(nums[0]), Double.parseDouble(nums[1])));
			}
			fileReader2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Initialise the map
		this.map = new TiledMap(Game.ASSETS_PATH + "/map.tmx", Game.ASSETS_PATH);
		// Initialise a panel in the world
		this.panel = new Panel();
		// Initialise a player in the world
		this.donkey = new Donkey();
		// Initialise other players in the world
		this.elephant = new Elephant(new Point(Elephant.INITX, Elephant.INITY), waypoints.get(0));
		this.dog = new Dog(new Point(Dog.INITX, Dog.INITY), waypoints.get(0));
		this.octopus = new Octopus(new Point(Octopus.INITX, Octopus.INITY), waypoints.get(0));
		this.players = new ArrayList<Kart>(Arrays.asList(donkey, elephant, dog, octopus));
		// Initialise the camera with the map loaded
		// 0 for donkey, 1 for elephant, 2 for dog, 3 for octopus
		this.camera = new Camera(donkey, map);
		// Initialise the ranking
		this.currRanking = 1;
		this.hazards = new ArrayList<Item>();

	}

	/**
	 * Get all waypoints.
	 * 
	 * @return Waypoints arraylist.
	 */
	public ArrayList<Point> getWaypoints() {
		return waypoints;
	}

	/**
	 * Getter to get donkey object.
	 * 
	 * @return Donkey object.
	 */
	public Donkey getDonkey() {
		return donkey;
	}

	/**
	 * Getter to get map object.
	 * 
	 * @return Map object.
	 */

	public TiledMap getMap() {
		return map;
	}

	/**
	 * Get the friction factor for a given tile.
	 * 
	 * @param point
	 *            Point object.
	 * @param map
	 *            The tile map.
	 * @return Friction of the tile.
	 */
	public static double getMu(Point point, TiledMap map) {
		// Unique ID for each tile
		int tileId;
		// String that stores the friction value for a specific tile
		String tileFriction;
		tileId = map.getTileId((int) point.getX() / map.getTileWidth(), (int) point.getY() / map.getTileHeight(), 0);
		tileFriction = map.getTileProperty(tileId, "friction", null);
		return Double.parseDouble(tileFriction);
	}

	/**
	 * Find if kart a crashes into kart b.
	 * 
	 * @param a
	 *            Kart a object.
	 * @param b
	 *            Kart b object.
	 * @return Whether the kart has crashed.
	 */

	public boolean isCrashed(Kart a, Kart b) {
		// Variables for comparing two karts
		double dist, deg;
		Angle thetaA, distAngle;
		thetaA = a.getTheta();
		distAngle = Angle.fromCartesian(b.getPos().getX() - a.getPos().getX(), b.getPos().getY() - a.getPos().getY());
		dist = Kart.getDist(a.getPos(), b.getPos());
		// Compute the difference of angle that kart a is facing and the angle
		// formed by vector from a to b
		deg = Math.abs(thetaA.getDegrees() - distAngle.getDegrees());
		// If a is donkey, moving forward and backward would affect the crashed
		// attribute
		if (a instanceof Donkey) {
			if (((Donkey) (a)).getMove_dir() == -1) {
				// Modify the deg with thetaA = thetaA +- 180
				deg = Math.abs(thetaA.getDegrees() + ONEEIGHTYDEG - distAngle.getDegrees());
				// Fix the range into (-180,180)
				deg = Angle.fromDegrees(deg).getDegrees();
			}
			if (deg < NINETYDEG && dist < NEARDIST) {
				return true;
			} else {
				return false;
			}
		}
		// If kart a is chasing kart b and dist less than 40 return true
		if (deg < NINETYDEG && dist < NEARDIST) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Whether there's any item near the player.
	 * 
	 * @param player
	 *            Player object.
	 * @param items
	 *            All items in a hashmap.
	 * @return The item object if there's any item nearby.
	 */

	public Item nearItem(Kart player, ArrayList<Item> items) {
		for (Item item : items) {
			if (Kart.getDist(item.getPos(), player.getPos()) < NEARDIST) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Update the game state for a frame.
	 * 
	 * @param rotate_dir
	 *            The player's direction of rotation (-1 for anti-clockwise, 1
	 *            for clockwise, or 0).
	 * @param move_dir
	 *            The player's movement in the car's axis (-1, 0 or 1).
	 * @param use_item
	 *            Whether player has controlled to use item
	 * @param cheatCode
	 *            Cheat code entered if applicable.
	 * @throws SlickException
	 */
	public void update(double rotate_dir, double move_dir, boolean use_item, int cheatCode) throws SlickException {
		// Find if any car has crashed
		for (Kart kart1 : players) {
			for (Kart kart2 : players) {
				if (!kart1.equals(kart2) && isCrashed(kart1, kart2)) {
					kart1.setCrashed(true);
				}
			}
		}
		// Update items that can be picked up
		Item nearestItem = nearItem(donkey, items);
		// Remove the item from the world and pass it to player
		if (nearestItem != null) {
			donkey.setItem(items.remove(items.indexOf(nearestItem)));
		}
		// If cheat enabled, generate a new item to player
		if (cheatCode == 3) {
			donkey.setItem(new Boost(new Point(0, 0)));
		}
		if (cheatCode == 4) {
			donkey.setItem(new OilCan(new Point(0, 0)));
		}
		if (cheatCode == 5) {
			donkey.setItem(new Tomato(new Point(0, 0)));
		}
		// Remove the tomatoProjectile & oilslick and release its effect to any
		// player
		for (Kart kart : players) {
			Item nearestUsedItem = nearItem(kart, hazards);
			if (nearestUsedItem != null) {
				kart.setHazard(hazards.remove(hazards.indexOf(nearestUsedItem)));
			}
		}

		// Update player according to whether player has arrived
		if (donkey.getPos().getY() > ENDY) {
			donkey.update(rotate_dir, move_dir, use_item, hazards, cheatCode, map);
		} else {
			donkey.update(0, 0, false, null, cheatCode, map);
		}
		// Update enemy karts
		elephant.update(hazards, this);
		dog.update(hazards, this);
		octopus.update(hazards, this);
		// Update the camera
		camera.update();
		// Then deal with the moving hazards
		Item itemToRemove = null;
		// Update tomato projectile
		for (Item item : hazards) {
			if (item instanceof TomatoProjectile) {
				((TomatoProjectile) item).update();
				if (getMu(item.getPos(), map) == 1) {
					itemToRemove = item;
				}
			}
		}
		// Remove item now to avoid concurrent modification exception
		if (itemToRemove != null) {
			hazards.remove(itemToRemove);
		}
		// Update ranking while player hasn't arrived
		if (donkey.getPos().getY() > ENDY) {
			currRanking = 1;
			for (Kart kart : players) {
				if (!kart.equals(donkey) && kart.getPos().getY() < donkey.getPos().getY()) {
					currRanking++;
				}
			}
		}
	}

	/**
	 * Render the entire screen, so it reflects the current game state.
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 * @throws SlickException
	 */
	public void render(Graphics g) throws SlickException {
		camera.render();
		// Render items using original position
		for (Item item : items) {
			item.drawImage(item.getPos().getX() - camera.getCameraX(), item.getPos().getY() - camera.getCameraY(),
					null);
		}
		for (Item item : hazards) {
			item.drawImage(item.getPos().getX() - camera.getCameraX(), item.getPos().getY() - camera.getCameraY(),
					null);
		}
		// Render players using relative position to player
		for (Kart player : players) {
			player.render(camera.getCameraX(), camera.getCameraY());
		}
		panel.render(g, currRanking, donkey.getItem());
		// Process the ranking of player
		String msg = "You came " + currRanking;
		if (donkey.getPos().getY() <= ENDY) {
			switch (currRanking) {
			case 1:
				msg += "st!";
				break;
			case 2:
				msg += "nd!";
				break;
			case 3:
				msg += "rd!";
				break;
			case 4:
				msg += "th!";
				break;
			}
			g.drawString(msg, (float) 0.5 * Game.SCREENWIDTH, (float) 0.5 * Game.SCREENHEIGHT);
		}

	}

}
