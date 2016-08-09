package Items;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Tools.Point;

/**
 * An oilcan class inherited from item class representing oilcan objects.
 */

public class OilCan extends Item {
	public static String PATH = "assets/items/oilcan.png";

	/**
	 * Oilcan constructor.
	 * 
	 * @param point
	 *            The position of oilcan.
	 * @throws SlickException
	 */
	public OilCan(Point point) throws SlickException {
		super(point, new Image(PATH));
	}

}
