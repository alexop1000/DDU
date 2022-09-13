
import UI.Menu;

import Util.Object;
import Util.Dragger;
import Util.Globals;

import processing.core.*;
import processing.data.*;

import Objects.Character;
import Objects.Coin;
import Objects.Platform;
import Objects.Water;


public class Main extends PApplet {
	JSONObject editorData = new JSONObject();
	Dragger dragger;
	Menu menu;
	int score = 0;

	public void settings() {
		fullScreen(1);
		editorData.setString("background", "080808");

		delay(1000);

		dragger = new Dragger(this);
		menu = new Menu(this);

		Globals.objects = new Object[1000];
	}

	public void draw() {
		clear();
		fill(255, 255, 255);
		background(unhex(editorData.getString("background")));

		// ! UPDATE CORE LOOPS
		for (Object object : Globals.objects) {
			if (object instanceof Platform) {
				Platform platform = (Platform) object;
				platform.Draw();
			} else if (object instanceof Character) {
				Character character = (Character) object;
				if (!Globals.IS_EDITOR) character.Update();
				character.Draw();
			} else if (object instanceof Coin) {
				Coin coin = (Coin) object;
				coin.Draw();
			} else if (object instanceof Water) {
				Water water = (Water) object;
				water.Draw();
			}
		}

		menu.Draw();
	}
	
	public void mouseClicked() {
		dragger.mouseClicked();
		menu.mouseClicked();
	}

	public void mouseDragged() {
		dragger.mouseDragged();
	}

	public void mouseReleased() {
		dragger.mouseReleased();
	}

	public void keyPressed() {
		setMove(key, true);
		if (key == PConstants.CODED && keyCode == PConstants.SHIFT) {
			Globals.IS_SHIFT_PRESSED = true;
		}
	}

	public void keyReleased() {
		setMove(key, false);
		if (key == PConstants.CODED && keyCode == PConstants.SHIFT) {
			Globals.IS_SHIFT_PRESSED = false;
		}
	}

	private boolean setMove(int k, boolean b) {
		switch (k) {
			case 'w':
				return Globals.isUp = b;
			case 's':
				return Globals.isDown = b;
			case 'a':
				return Globals.isLeft = b;
			case 'd':
				return Globals.isRight = b;
			default:
				return b;
		}
	}

	public static void main(String[] args) {
		PApplet.runSketch(new String[] { "Main" }, new Main());
	}	
}