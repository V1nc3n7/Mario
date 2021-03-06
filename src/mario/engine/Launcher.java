package mario.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import mario.game.Game;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;

public class Launcher {

	public static final String TITLE = "PROTOTYPE";
	public static final int MILLISECONDS = (int) Math.pow(10, 3);
	public static final int NANOSECONDS = (int) Math.pow(10, 9);
	public static final double UPDATE_RATE = NANOSECONDS / 60.0;

	public static boolean running = false;
	public static int width = 824;
	public static int height = 900;

	public Game game;

	public Launcher() {
		display();
		game = new Game();
	}

	public static int okcancel(String theMessage) {
		int result = JOptionPane.showConfirmDialog(null, theMessage,
				"alert", JOptionPane.OK_CANCEL_OPTION);
		return result;
	}

	public static void main(String[] args) {
		Launcher main = new Launcher();
		int i = okcancel("Start the game ?");
		if(i == 0)
			main.start();
		else
			main.exit();
	}

	public static void stop() {
		running = false;
	}

	public void start() {
		running = true;
		loop();
	}

	public void exit() {
		Display.destroy();
        AL.destroy();
		System.exit(0);
	}

	public void loop() {

		double elapsed;
        int frames = 0;
		int ticks = 0;
		long before = System.nanoTime();
		long timer = System.currentTimeMillis();

		while(running) {

			if(Display.isCloseRequested())
				stop();

			Display.update();

			width = Display.getWidth();
			height = Display.getHeight();
			elapsed = System.nanoTime() - before;

			if(elapsed > UPDATE_RATE) {
				before += UPDATE_RATE;
				update();
				ticks++;
			} else {
				render();
				frames++;
			}

			if(System.currentTimeMillis() - timer > MILLISECONDS) {
				timer += MILLISECONDS;
				Display.setTitle(TITLE + " - Ticks : " + ticks + ", FPS : " + frames);
				ticks = 0;
				frames = 0;
			}
		}

		exit();

	}

	public void update() {
		game.update();
	}

	public void render() {
		view2D(width, height);
		glClear(GL_COLOR_BUFFER_BIT);
		game.render();
	}

	public void display() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(true);
			Display.setFullscreen(false);
			Display.setTitle(TITLE);
			Display.create();
            //Display.setVSyncEnabled(true);
			view2D(width, height);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private void view2D(int width, int height) {
        glEnable(GL_TEXTURE_2D);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // enable alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluOrtho2D(0, width, height, 0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

}
