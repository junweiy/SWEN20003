package GameFrame;
/* SWEN20003 Object Oriented Software Development
 * Shadow Kart
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

/**
 * Main class for the Shadow Kart Game engine. Handles initialisation, input and
 * rendering.
 */
public class Game extends BasicGame {
	/** Location of the "assets" directory. */
	public static final String ASSETS_PATH = "assets";

	/** The game state. */
	private World world;

	/** Screen width, in pixels. */
	public static final int SCREENWIDTH = 800;
	/** Screen height, in pixels. */
	public static final int SCREENHEIGHT = 600;
	// Variable to enable cheat mode
	// cheatCode and lastCheatCode are used to separate display and usage of
	// items
	private int cheatCount, cheatCode, lastCheatCode;
	private boolean cheatEnabled;
	// Static variable for time units and variable for countdown
	public static final int ONESEC = 1000;
	public static final int TWOSEC = 2000;
	public static final int THREESEC = 3000;
	private int countDown;
	// Variable for start countdown
	private int startCountDown;

	/** Create a new Game object. */
	public Game() {
		super("Shadow Kart");
		cheatCount = 0;
		lastCheatCode = 0;
		cheatCode = 0;
		cheatEnabled = false;
		countDown = 0;
		startCountDown = THREESEC;
	}

	/**
	 * Initialise the game state.
	 * 
	 * @param gc
	 *            The Slick game container object.
	 */
	@Override
	public void init(GameContainer gc) throws SlickException {
		world = new World();
	}

	/**
	 * Update the game state for a frame.
	 * 
	 * @param gc
	 *            The Slick game container object.
	 * @param delta
	 *            Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// Get data about the current input (keyboard state).
		Input input = gc.getInput();

		// Update the player's rotation and position based on key presses.
		double rotate_dir = 0;
		double move_dir = 0;
		if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S))
			move_dir -= 1;
		if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W))
			move_dir += 1;
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A))
			rotate_dir -= 1;
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D))
			rotate_dir += 1;
		boolean use_item = input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL);
		// Press JAVA in order to enable cheat mode while count down
		if (startCountDown > 0) {
			if (input.isKeyDown(Input.KEY_J)) {
				if (cheatCount == 0) {
					cheatCount++;
				}
			}
			if (input.isKeyDown(Input.KEY_A)) {
				if (cheatCount == 1) {
					cheatCount++;
				}
				if (cheatCount == 3) {
					cheatCount++;
					cheatEnabled = true;
					cheatCode = lastCheatCode = 1;
					// Set a timer for displaying cheat enabled words
					countDown = ONESEC;
				}
			}
			if (input.isKeyDown(Input.KEY_V) && cheatCount == 2) {
				cheatCount++;
			}
		}

		// Count down decreases by delta every time upon update
		if (countDown > 0) {
			countDown -= delta;
		}
		if (startCountDown > 0) {
			startCountDown -= delta;
		}
		// Achieve different functions with different cheatCode
		// Details of functions can be found below in the render function
		if (cheatEnabled && input.isKeyDown(Input.KEY_U)) {
			cheatCode = lastCheatCode = 2;
			countDown = ONESEC;
		}
		if (cheatEnabled && input.isKeyDown(Input.KEY_I)) {
			cheatCode = lastCheatCode = 3;
			countDown = ONESEC;
		}
		if (cheatEnabled && input.isKeyDown(Input.KEY_O)) {
			cheatCode = lastCheatCode = 4;
			countDown = ONESEC;
		}
		if (cheatEnabled && input.isKeyDown(Input.KEY_P)) {
			cheatCode = lastCheatCode = 5;
			countDown = ONESEC;
		}
		// Let World.update decide what to do with this data.
		if (startCountDown <= 0) {
			for (int i = 0; i < delta; i++)
				world.update(rotate_dir, move_dir, use_item, cheatCode);
		}
		cheatCode = 0;
	}

	/**
	 * Render the entire screen, so it reflects the current game state.
	 * 
	 * @param gc
	 *            The Slick game container object.
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// Let World.render handle the rendering.
		world.render(g);
		// Draw countdown numbers at the beginning
		if (startCountDown > TWOSEC) {
			g.drawString("3", (float) 0.5 * SCREENWIDTH, (float) 0.3 * SCREENHEIGHT);
		} else if (startCountDown > ONESEC) {
			g.drawString("2", (float) 0.5 * SCREENWIDTH, (float) 0.3 * SCREENHEIGHT);
		} else if (startCountDown > 0) {
			g.drawString("1", (float) 0.5 * SCREENWIDTH, (float) 0.3 * SCREENHEIGHT);
		}
		if (countDown > 0) {
			switch (lastCheatCode) {
			case 1:
				g.drawString("Cheat Enabled", 0, 0);
				break;
			case 2:
				g.drawString("Unlimited Items", 0, 0);
				break;
			case 3:
				g.drawString("Change item to boost", 0, 0);
				break;
			case 4:
				g.drawString("Change item to oil can", 0, 0);
				break;
			case 5:
				g.drawString("Change item to tomato", 0, 0);
				break;
			}
			countDown--;
		}
	}

	/**
	 * Start-up method. Creates the game and runs it.
	 * 
	 * @param args
	 *            Command-line arguments (ignored).
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Game());
		// setShowFPS(true), to show frames-per-second.
		app.setShowFPS(false);
		app.setDisplayMode(SCREENWIDTH, SCREENHEIGHT, false);
		app.start();
	}
}
