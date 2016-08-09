package GameFrame;

/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

import org.newdawn.slick.tiled.TiledMap;

import Karts.Kart;
import Tools.Point;

/**
 * Represents the camera object that used to focusing on one player throughout
 * the whole game.
 */

public class Camera {
	// Extra lines to render at the screen border
	private static final int EXTRALINE = 2;
	// The starting top-left coordinates that camera is fixing at
	private double cameraX, cameraY;
	// Indices of tiles to render at
	private int indexX, indexY;
	// Offset of tile
	private int offsetX, offsetY;
	// Player that camera is focusing on
	private Kart player;
	// Map for the camera to fix on
	private TiledMap map;

	/**
	 * Create a new camera with map loaded.
	 * 
	 * @param player
	 *            Player kart object.
	 * @param map
	 *            Map of the world.
	 */

	public Camera(Kart player, TiledMap map) {
		this.cameraX = player.getPos().getX() - Game.SCREENWIDTH / 2;
		this.cameraY = player.getPos().getY() - Game.SCREENHEIGHT / 2;
		this.map = map;
		this.indexX = (int) (cameraX / map.getTileWidth());
		this.indexY = (int) (cameraY / map.getTileWidth());
		offsetX = (int) (-cameraX % map.getTileWidth());
		offsetY = (int) (-cameraY % map.getTileWidth());
		this.player = player;
	}

	/**
	 * Getter to get cameraX value.
	 * 
	 * @return The cameraX value.
	 */
	public double getCameraX() {
		return cameraX;
	}

	/**
	 * Getter to get cameraY value.
	 * 
	 * @return The cameraY value.
	 */
	public double getCameraY() {
		return cameraY;
	}

	/**
	 * Fix the camera on a point.
	 * 
	 * @param point
	 *            Point to fix camera at.
	 */
	public void setCentre(Point point) {
		// Get the map width and height
		double mapWidth, mapHeight;
		mapWidth = map.getWidth() * map.getTileWidth();
		mapHeight = map.getHeight() * map.getTileHeight();
		cameraX = point.getX() - Game.SCREENWIDTH / 2;
		cameraY = point.getY() - Game.SCREENHEIGHT / 2;
		// Stop moving the camera when reached the left or right edge of map
		if (cameraX < 0) {
			cameraX = 0;
		} else if (cameraX + Game.SCREENWIDTH > mapWidth) {
			cameraX = mapWidth - Game.SCREENWIDTH;
		}
		// Stop moving the camera when reached the top of bottom edge of map
		if (cameraY < 0) {
			cameraY = 0;
		} else if (cameraY + Game.SCREENHEIGHT > mapHeight) {
			cameraY = mapHeight - Game.SCREENHEIGHT;
		}
	}

	/**
	 * Update the game state for a frame. Move with the car.
	 * 
	 */
	public void update() {
		// Get the tile width and height
		double tileWidth, tileHeight;
		tileWidth = map.getTileWidth();
		tileHeight = map.getTileHeight();
		// Set the centre of camera at the coordinates of player
		setCentre(player.getPos());
		// Calculate the offset caused by each tile
		offsetX = (int) (-cameraX % tileWidth);
		offsetY = (int) (-cameraY % tileHeight);
		// Update the indices for camera
		indexX = (int) (cameraX / tileWidth);
		indexY = (int) (cameraY / tileHeight);
	}

	/**
	 * Render the map for an area slightly larger than the screen size with the
	 * tile starting from (indexX, indexY) with an offset of (offsetX, offsetY)
	 */
	public void render() {
		// Get the tile width and height
		double tileWidth, tileHeight;
		tileWidth = map.getTileWidth();
		tileHeight = map.getTileHeight();
		// Render the map
		map.render(offsetX, offsetY, indexX, indexY, (int) (Game.SCREENWIDTH / tileWidth + EXTRALINE),
				(int) (Game.SCREENHEIGHT / tileHeight + EXTRALINE));

	}
}
