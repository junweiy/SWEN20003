package Items;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Tools.Point;

/**
 * An oilslick class inherited from item class representing oilslick objects.
 */
public class OilSlick extends Item {
	public static String PATH = "assets/items/oilslick.png";

	/**
	 * Oilslick construtor.
	 * 
	 * @param point
	 *            The position of oilslick object.
	 * @throws SlickException
	 */
	public OilSlick(Point point) throws SlickException {
		super(point, new Image(PATH));

	}

}
