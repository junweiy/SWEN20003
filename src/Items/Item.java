package Items;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;

import GameFrame.GameObject;
import Tools.Angle;
import Tools.Point;

/**
 * An item class inherits from GameObject class representing item objects.
 */
public class Item extends GameObject {
	/**
	 * Item constructor.
	 * 
	 * @param pos
	 *            Position of the item.
	 * @param img
	 *            Image of the item.
	 */
	public Item(Point pos, Image img) {
		super(pos, img);
	}

	/**
	 * Draw image of the item for display.
	 * 
	 * @param x
	 *            X coordinate of item.
	 * @param y
	 *            Y coorindate of Item.
	 * @param theta
	 *            Angle that the image is facing.
	 */
	public void drawImage(double x, double y, Angle theta) {
		img.drawCentered((float) x, (float) y);
		if (theta != null) {
			img.setRotation((float) theta.getDegrees());
		}

	}

}
