package Karts;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import GameFrame.World;
import Items.Item;
import Tools.Point;

/**
 * Emeny class inherited from Kart class representing enemy objects in the
 * world.
 */

public abstract class Enemy extends Kart {
	// Min dist between kart and waypoint
	public static final double WAYPTDIST = 250;
	// Current waypoint coordinates
	protected Point dest;
	// Current waypoint index the kart is heading
	protected int currIndex;

	/**
	 * A constructor of Enemy class.
	 * 
	 * @param start
	 *            Starting point.
	 * @param player
	 *            Player image.
	 * @param item
	 *            Item that enemy currently holds.
	 * @param dest
	 *            First destination (waypoint).
	 * @param currIndex
	 *            Current index of waypoints.
	 */
	public Enemy(Point start, Image player, Item item, Point dest, int currIndex) {
		super(start, player, item);
		this.dest = dest;
		this.currIndex = currIndex;
	}

	/**
	 * Update the kart in the world with given rotating direction and moving
	 * direction.
	 * 
	 * @param hazards
	 *            Arraylist of hazard items.
	 * @param world
	 *            The world object.
	 * @throws SlickException
	 */
	public abstract void update(ArrayList<Item> hazards, World world) throws SlickException;
}
