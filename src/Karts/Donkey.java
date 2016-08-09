package Karts;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import GameFrame.Game;
import GameFrame.World;
import Items.Boost;
import Items.Item;
import Items.OilCan;
import Items.OilSlick;
import Items.Tomato;
import Items.TomatoProjectile;
import Tools.Point;

import java.util.ArrayList;

import org.newdawn.slick.Image;

/**
 * Represents the player in the world. (Designed to be instantiated just once
 * for the whole game).
 */

public class Donkey extends Kart {
	// Frames left for boosting
	protected int boostCountDown;
	// Boost speed
	public static final double BOOST = 0.0008;
	// Starting coordinate of donkey
	public static final double INITX = 1332, INITY = 13086;
	// Boolean variable to keep track of whether cheat has been enabled
	private boolean unlimitedItem;
	// Variable to store its movement direction
	private double move_dir;

	/**
	 * A constructor for donkey object.
	 * 
	 * @throws SlickException
	 */

	public Donkey() throws SlickException {
		super(new Point(INITX, INITY), new Image(Game.ASSETS_PATH + "/karts/donkey.png"), null);
		this.boostCountDown = 0;
		unlimitedItem = false;
		move_dir = 0;
	}

	/**
	 * Getter to get move_dir value.
	 * 
	 * @return The move_dir value.
	 */
	public double getMove_dir() {
		return move_dir;
	}

	/**
	 * use an item and add new items (if generated) to the arraylist.
	 * 
	 * @param item
	 *            Item object.
	 * @param hazards
	 *            Hazards arraylist.
	 * @param map
	 *            The tiled map.
	 * @throws SlickException
	 */

	public void useItem(Item item, ArrayList<Item> hazards, TiledMap map) throws SlickException {
		// Leave an interval between kart and item to avoid the item hits kart
		// itself
		Point slick, tomato;
		slick = new Point(pos.getX() - theta.getXComponent(DIST), pos.getY() - theta.getYComponent(DIST));
		tomato = new Point(pos.getX() + theta.getXComponent(DIST), pos.getY() + theta.getYComponent(DIST));
		// if position of oil slick to be put is within the wall, fix the
		// position to the player itself
		if (World.getMu(slick, map) == 1) {
			slick = pos;
		}
		// Add the item to hazard list when used
		if (item instanceof OilCan) {
			hazards.add(new OilSlick(slick));
		} else if (item instanceof Tomato) {
			hazards.add(new TomatoProjectile(tomato, theta));
		} else if (item instanceof Boost) {
			boostCountDown = THREESECS;
		}
	}

	/**
	 * Update the kart in the world with given rotating direction and moving
	 * direction
	 * 
	 * @param rotate_dir
	 *            The direction of rotating.
	 * @param move_dir
	 *            The direction of moving.
	 * @param use_item
	 *            Whether kart has used any item.
	 * @param hazards
	 *            Arraylist of hazards.
	 * @param cheatCode
	 *            Cheat code if applicable.
	 * @param map
	 *            The tiled map.
	 * @throws SlickException
	 */
	public void update(double rotate_dir, double move_dir, boolean use_item, ArrayList<Item> hazards, int cheatCode,
			TiledMap map) throws SlickException {
		// Record if unlimited item cheat has been enabled
		if (cheatCode == 2) {
			unlimitedItem = true;
		}
		// Calculate the new move_dir using boost rate
		double newMoveDir = BOOST / Kart.ACCERLATION;
		// Activate items with a count down if there's count down remaining
		double newAngleRate = SPINANGLERATE / ANGLERATE;
		if (boostCountDown > 0 && slickEffectCountDown <= 0 && tomatoEffectCountDown <= 0) {
			move(rotate_dir, newMoveDir, map);
			this.move_dir = newMoveDir;
			boostCountDown--;
		} else if (slickEffectCountDown > 0 && tomatoEffectCountDown <= 0) {
			move(newAngleRate, 1, map);
			this.move_dir = 1;
			slickEffectCountDown--;
		} else if (slickEffectCountDown <= 0 && tomatoEffectCountDown > 0) {
			move(newAngleRate, 1, map);
			this.move_dir = 1;
			tomatoEffectCountDown--;
		} else if (slickEffectCountDown > 0 && tomatoEffectCountDown > 0) {
			move(newAngleRate, 1, map);
			this.move_dir = 1;
			slickEffectCountDown--;
			tomatoEffectCountDown--;
		} else {
			move(rotate_dir, move_dir, map);
			this.move_dir = move_dir;
		}
		// Activate or release effect of hazards
		if (this.getHazard() != null) {
			meetHazard(this.getHazard());
			this.setHazard(null);
		} else if (use_item && this.getItem() != null && slickEffectCountDown <= 0 && tomatoEffectCountDown <= 0) {
			useItem(this.getItem(), hazards, map);
			// Do not set item to null when cheat has been enabled
			if (!unlimitedItem) {
				this.setItem(null);
			}

		}
	}

}
