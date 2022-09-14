import UI.Menu;

import Util.Object;
import Util.Dragger;
import Util.Globals;

import processing.core.*;
import processing.data.*;
import Data.Load;
import Objects.Character;
import Objects.Coin;
import Objects.Goal;
import Objects.Platform;
import Objects.Spike;
import Objects.Water;


public class Main extends PApplet {
	JSONObject editorData = new JSONObject();
	Dragger dragger;
	Menu menu;
	Load loader;
	int score = 0;

	public void settings() {
		fullScreen(1);
		editorData.setString("background", "080808");

		Globals.objects = new Object[1000];

		dragger = new Dragger(this);
		menu = new Menu(this);
		// loader = new Load(this);
	}

	public void draw() {
		clear();
		fill(255, 255, 255);
		background(unhex(editorData.getString("background")));
        rectMode(PApplet.CENTER);
        imageMode(PApplet.CENTER);

		// ! UPDATE CORE LOOPS
		int characterCount = 0;
		int finishedCount = 0;
		for (Object object : Globals.objects) {
			if (object instanceof Character) {
				Character character = (Character) object;
				if (!Globals.IS_EDITOR) character.Update();
				character.Draw();
				characterCount ++;
				if (character.isFinished) finishedCount ++;
			} else if (object != null) {
				object.Draw();
			}
		}

		menu.Draw();

		if (!Globals.IS_EDITOR && characterCount > 0 && characterCount == finishedCount) {
			textSize(50);
			fill(255,255,255);
			text("GG Win", displayWidth / 2, 120);
			text("Score: " + Globals.Score, displayWidth / 2, 170);

		}
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
		if (keyCode == PConstants.ENTER) {
			Globals.IS_EDITOR = !Globals.IS_EDITOR; 
			if (Globals.IS_EDITOR == true) {
				for (Object object : Globals.objects) {
					if (object != null) {
						object.Reset();
					}
				}
			}
			menu.buttons[0].selected = Globals.IS_EDITOR;
			if (Globals.IS_EDITOR) {
				menu.buttons[0].currentColor = menu.buttons[0].selectedColor;
			} else {
				menu.buttons[0].currentColor = menu.buttons[0].defaultColor;
			}
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