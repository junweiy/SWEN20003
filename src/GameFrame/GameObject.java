package GameFrame;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;

import Tools.Point;

/**
 * A base class to represent objects in the world.
 *
 */
public class GameObject {
	// Position of the object
	protected Point pos;
	// The image of the object
	protected Image img;

	/**
	 * GameObject constructor.
	 * 
	 * @param pos
	 *            Position of the game object.
	 * @param img
	 *            Image of the game object.
	 */
	public GameObject(Point pos, Image img) {
		this.pos = pos;
		this.img = img;
	}

	/**
	 * Getter to get position point.
	 * 
	 * @return The point value.
	 */

	public Point getPos() {
		return pos;
	}
}
