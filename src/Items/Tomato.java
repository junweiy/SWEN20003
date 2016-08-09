package Items;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Tools.Point;

/**
 * A tomato class inherited from item class representing tomato objects.
 */
public class Tomato extends Item {
	public static String PATH = "assets/items/tomato.png";

	/**
	 * Tomato constructor.
	 * 
	 * @param point
	 *            Position of the tomato.
	 * @throws SlickException
	 */
	public Tomato(Point point) throws SlickException {
		super(point, new Image(PATH));
	}

}
