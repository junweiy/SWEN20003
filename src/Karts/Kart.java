package Karts;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import GameFrame.GameObject;
import GameFrame.World;
import Items.Item;
import Items.OilSlick;
import Items.TomatoProjectile;
import Tools.Angle;
import Tools.Point;

/**
 * Kart class inherited from GameObject class representing Kart objects in the
 * world.
 * 
 */
public class Kart extends GameObject {
	// Three seconds
	public static final int THREESECS = 3000;
	// Rotation speed and accerlation */
	public static final double ANGLERATE = 0.004, ACCERLATION = 0.0005;
	// 0.7 seconds for spinning
	public static final int SPINSECS = 700;
	// spinning rotating rate
	public static final double SPINANGLERATE = 0.008;
	// Distance of oil slick from player, leave one more pixel to prevent hit
	// player itself
	public static final double DIST = 41;
	// Initialise variables karts, including position coordinates, velocity,
	// friction
	// factor
	protected double v;
	// Whether the car has crashed
	private boolean crashed;
	// Angle that kart is facing
	protected Angle theta;
	// the frames left when kart reached an used oil can
	protected int slickEffectCountDown;
	protected int tomatoEffectCountDown;
	// Item that human player holds
	private Item item;
	// Item that prevents player from moving
	private Item hazard;

	/**
	 * A constructor for kart object.
	 * 
	 * @param start
	 *            Starting point.
	 * @param img
	 *            Image object.
	 * @param item
	 *            Item that kart currently holds.
	 */
	public Kart(Point start, Image img, Item item) {
		super(start, img);
		this.v = 0;
		this.setCrashed(false);
		this.theta = new Angle(0);
		this.slickEffectCountDown = 0;
		this.tomatoEffectCountDown = 0;
		this.setItem(item);
	}

	/**
	 * Get the distance between two coordinates.
	 * 
	 * @param x
	 *            Point x.
	 * @param y
	 *            Point y.
	 * @return Distance from x to y.
	 */

	public static double getDist(Point x, Point y) {
		return Math.sqrt((x.getX() - y.getX()) * (x.getX() - y.getX()) + (x.getY() - y.getY()) * (x.getY() - y.getY()));
	}

	/**
	 * Getter to get theta.
	 * 
	 * @return Angle theta.
	 */
	public Angle getTheta() {
		return this.theta;
	}

	/**
	 * Getter to get crashed value.
	 * 
	 * @return The crashed value.
	 */
	public boolean isCrashed() {
		return crashed;
	}

	/**
	 * Setter to set crashed value.
	 * 
	 * @param crashed
	 *            The crashed value to set.
	 */
	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}

	/**
	 * Getter to get item value.
	 * 
	 * @return The item value.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Setter to set item value.
	 * 
	 * @param item
	 *            The item value to set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Getter to get hazard value.
	 * 
	 * @return The hazard value.
	 */
	public Item getHazard() {
		return hazard;
	}

	/**
	 * Setter to set hazard value.
	 * 
	 * @param hazard
	 *            The hazard value to set.
	 */
	public void setHazard(Item hazard) {
		this.hazard = hazard;
	}

	/**
	 * Detect if there's a blocking tile nearby.
	 * 
	 * @param point
	 *            Position of the tile.
	 * @param map
	 *            The tiled map.
	 * @return The status of the player (stuck or not).
	 */
	public boolean stuck(Point point, TiledMap map) {
		if (World.getMu(point, map) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Move the kart with a given rotating direction and moving direction.
	 * 
	 * @param rotate_dir
	 *            The direction of rotating.
	 * @param move_dir
	 *            The direction of moving.
	 * @param map
	 *            The tiled map.
	 */
	public void move(double rotate_dir, double move_dir, TiledMap map) {
		// Movement in x/y direction within the time of delta
		double deltaX, deltaY;
		// Get the friction factor value
		double mu = World.getMu(pos, map);
		// Starting to modify on karts now
		// Set the velocity for next frame
		v = (v + ACCERLATION * move_dir) * (1 - mu);
		// Compute the deltaX and deltaY
		theta = theta.add(new Angle(ANGLERATE * rotate_dir));
		deltaX = this.theta.getXComponent(v);
		deltaY = this.theta.getYComponent(v);
		// if the next movement is about to get stuck or crash, prevent moving
		if (stuck(new Point(pos.getX() + deltaX, pos.getY() + deltaY), map) || isCrashed()) {
			v = 0;
			if (isCrashed()) {
				setCrashed(false);
			}
		} else {
			// Update the absolute position
			pos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
		}
	}

	/**
	 * Move the kart with an effect caused by hazards.
	 * 
	 * @param rotate_dir
	 *            The direction of rotating.
	 * @param move_dir
	 *            The direction of moving.
	 * @param map
	 *            The tiled map.
	 */
	public void moveWithItem(double rotate_dir, double move_dir, TiledMap map) {
		double newAngleRate = SPINANGLERATE / ANGLERATE;
		if (slickEffectCountDown <= 0 && tomatoEffectCountDown <= 0) {
			move(rotate_dir, move_dir, map);
		} else if (slickEffectCountDown > 0 && tomatoEffectCountDown <= 0) {
			move(newAngleRate, 1, map);
			slickEffectCountDown--;
		} else if (slickEffectCountDown <= 0 && tomatoEffectCountDown > 0) {
			move(newAngleRate, 1, map);
			tomatoEffectCountDown--;
		} else {
			slickEffectCountDown--;
			tomatoEffectCountDown--;
		}
	}

	/**
	 * Update count down when meet hazard.
	 * 
	 * @param item
	 *            Item object.
	 */
	public void meetHazard(Item item) {
		if (item instanceof OilSlick) {
			slickEffectCountDown = SPINSECS;
		} else if (item instanceof TomatoProjectile) {
			tomatoEffectCountDown = SPINSECS;
		}
	}

	public void render(double cameraX, double cameraY) {
		img.drawCentered((float) (pos.getX() - cameraX), (float) (pos.getY() - cameraY));
		img.setRotation((float) this.theta.getDegrees());
	}
}
