package Items;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Tools.Angle;
import Tools.Point;

/**
 * A tomatoProjectile class inherited from item class representing
 * tomatoProjectile objects.
 */
public class TomatoProjectile extends Item {
	public static double SPEED = 1.7;
	public static String PATH = "assets/items/tomato-projectile.png";
	private Angle theta;

	/**
	 * TomatoProjectile constructor.
	 * 
	 * @param point
	 *            Position of tomatoProjectile object.
	 * @param theta
	 *            Angle of tomatoProjectile moving towards.
	 * @throws SlickException
	 */
	public TomatoProjectile(Point point, Angle theta) throws SlickException {
		super(point, new Image(PATH));
		this.theta = theta;

	}

	/**
	 * Update the projectile to new position.
	 */
	public void update() {
		pos = new Point(pos.getX() + theta.getXComponent(SPEED), pos.getY() + theta.getYComponent(SPEED));
	}

}
