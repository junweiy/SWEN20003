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
 * Elephant class inherited from enemy class representing the elephant in the
 * world.
 *
 */
public class Elephant extends Enemy {
	// Starting coordinate of elephant
	public static final double INITX = 1260, INITY = 13086;

	/**
	 * A consturcor of elephant object.
	 * 
	 * @param start
	 *            Starting point.
	 * @param dest
	 *            First destination (waypoint).
	 * @throws SlickException
	 */
	public Elephant(Point start, Point dest) throws SlickException {
		super(start, new Image(Game.ASSETS_PATH + "/karts/elephant.png"), null, dest, 0);
		this.setItem(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Karts.Enemy#update(java.util.ArrayList, GameFrame.World)
	 */
	public void update(ArrayList<Item> hazards, World world) throws SlickException {
		int rotate_dir = 0;
		// Waypoints arraylist
		ArrayList<Point> waypoints = world.getWaypoints();
		if (getDist(pos, dest) < WAYPTDIST && currIndex < waypoints.size()) {
			dest = waypoints.get(currIndex++);
		}
		// Degree form by kart and dest
		Angle distDeg = Angle.fromCartesian(dest.getX() - pos.getX(), dest.getY() - pos.getY());
		Angle deg = distDeg.subtract(theta);
		if (deg.getDegrees() < 0) {
			rotate_dir = -1;
		} else if (deg.getDegrees() > 0) {
			rotate_dir = 1;
		}
		if (this.getHazard() != null) {
			meetHazard(this.getHazard());
			this.setHazard(null);
		}
		moveWithItem(rotate_dir, 1, world.getMap());
	}

}
