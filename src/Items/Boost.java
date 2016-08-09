package Items;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Tools.Point;

/**
 * A boost class inherited from item class representing boost objects.
 */

public class Boost extends Item {
	public static String PATH = "assets/items/boost.png";
	
	/**
	 * Boost constructor.
	 * 
	 * @param point
	 *            The position of the boost.
	 * @throws SlickException
	 */
	public Boost(Point point) throws SlickException {
		super(point, new Image(PATH));
	}

}
