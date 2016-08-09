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
 * Octopus inherited from Enemy class representing octopus objects in the world.
 *
 */
public class Octopus extends Enemy {
	// two dists values from donkey
	public static final double LOWERDIST = 100;
	public static final double UPPERDIST = 250;
	// Starting coordinate of elephant
	public static final double INITX = 1476, INITY = 13086;
	// Variable indicating if it was just chasing donkey
	private boolean justChasingDonkey;

	/**
	 * A constructor for Octopus object.
	 * 
	 * @param start
	 *            Starting point.
	 * @param dest
	 *            First destination (waypoint).
	 * @throws SlickException
	 */
	public Octopus(Point start, Point dest) throws SlickException {
		super(start, new Image(Game.ASSETS_PATH + "/karts/octopus.png"), null, dest, 0);
		this.currIndex = 0;
		this.dest = dest;
		this.setItem(null);
		this.justChasingDonkey = false;
	}

	/**
	 * Function to find the next way point when octopus is not following donkey
	 * 
	 * @param waypoints
	 *            Waypoints arraylist.
	 * @return The index of next way point.
	 */

	private int nextWayPoint(ArrayList<Point> waypoints) {
		// point index to record the next waypoint index
		int pointIndex = 0;
		// Current dist from octopus to waypoint iterating, min dist from
		// octopus to all way points
		double currDist, minDist;
		minDist = getDist(pos, waypoints.get(0));
		// Iterate through all way points to find the index nearest to octopus
		for (Point point : waypoints) {
			currDist = getDist(pos, point);
			if (currDist < minDist) {
				pointIndex = waypoints.indexOf(point);
				minDist = currDist;
			}
		}
		justChasingDonkey = false;
		return pointIndex;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Karts.Enemy#update(java.util.ArrayList, GameFrame.World)
	 */
	public void update(ArrayList<Item> hazards, World world) throws SlickException {
		int rotate_dir = 0;
		// Waypoint arraylist
		ArrayList<Point> waypoints = world.getWaypoints();
		// Donkey object
		Donkey donkey = world.getDonkey();
		// Flag to indicate whether it's chasing dongkey
		boolean chaseDonkey = false;
		double dist = getDist(pos, donkey.getPos());
		// If it's within the range then chasing donkey and set the dest
		if (dist < UPPERDIST && dist > LOWERDIST) {
			chaseDonkey = true;
			justChasingDonkey = true;
			dest = donkey.getPos();
		} else {
			chaseDonkey = false;
		}
		// If it's not chasing donkey heading to the next way point
		if (!chaseDonkey) {
			// If it just chased donkey, update the currDest
			if (justChasingDonkey) {
				currIndex = nextWayPoint(waypoints);
				dest = waypoints.get(currIndex);
			}
			if (getDist(pos, dest) < WAYPTDIST && currIndex < waypoints.size()) {
				dest = waypoints.get(currIndex++);
			}
		}
		// Degree form by kart and dest
		Angle distDeg = Angle.fromCartesian(dest.getX() - pos.getX(), dest.getY() - pos.getY());
		Angle deg = distDeg.subtract(theta);
		// Rotate according to the angle
		if (deg.getDegrees() < 0) {
			rotate_dir = -1;
		} else if (deg.getDegrees() > 0) {
			rotate_dir = 1;
		}
		// Check if meet any hazard
		if (this.getHazard() != null) {
			meetHazard(this.getHazard());
			this.setHazard(null);
		}
		moveWithItem(rotate_dir, 1, world.getMap());
	}

}