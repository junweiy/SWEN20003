package Karts;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import GameFrame.Game;
import GameFrame.World;
import Items.Item;
import Tools.Angle;
import Tools.Point;

/**
 * A dog class inherited from enemy class representing dog object in the world.
 */
public class Dog extends Enemy {
	// Two acceleration rate in different cases
	public static final double SLOWER = 0.9, FASTER = 1.1;
	// Starting coordinate of elephant
	public static final double INITX = 1404, INITY = 13086;

	/**
	 * Constructor of dog object.
	 * 
	 * @param start
	 *            Starting point.
	 * @param dest
	 *            First destination (waypoint).
	 * @throws SlickException
	 */
	public Dog(Point start, Point dest) throws SlickException {
		super(start, new Image(Game.ASSETS_PATH + "/karts/dog.png"), null, dest, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Karts.Enemy#update(java.util.ArrayList, GameFrame.World)
	 */
	public void update(ArrayList<Item> hazards, World world) throws SlickException {
		int rotate_dir = 0;
		// Get waypoints arraylist
		ArrayList<Point> waypoints = world.getWaypoints();
		// Move towards next waypoint when it's close enough to the current
		// waypoint
		if (getDist(pos, dest) < WAYPTDIST && currIndex < waypoints.size()) {
			dest = waypoints.get(currIndex++);
		}
		// Angle form by kart and dest, rotate according to the difference
		Angle distDeg = Angle.fromCartesian(dest.getX() - pos.getX(), dest.getY() - pos.getY());
		Angle deg = distDeg.subtract(theta);
		if (deg.getDegrees() < 0) {
			rotate_dir = -1;
		} else if (deg.getDegrees() > 0) {
			rotate_dir = 1;
		}
		// Use the hazard when possible
		if (this.getHazard() != null) {
			meetHazard(this.getHazard());
			this.setHazard(null);
		}
		// Make different decisions on speed
		if (pos.getY() <= world.getDonkey().getPos().getY()) {
			moveWithItem(rotate_dir, SLOWER, world.getMap());
		} else {
			moveWithItem(rotate_dir, FASTER, world.getMap());
		}

	}

}