
package Tools;
/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Junwei Yang <junweiy>
 */

/**
 * Point class used to represent point data.
 */
public class Point {
	// X, y coordinates
	double x, y;

	/**
	 * Constructor to create a point object with (x,y) coordinate
	 * 
	 * @param x
	 *            x value
	 * @param y
	 *            y value
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter to get X value
	 * 
	 * @return X value
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Getter to get Y value
	 * 
	 * @return Y value
	 */
	public double getY() {
		return this.y;
	}
}
