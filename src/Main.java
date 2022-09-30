import UI.Activate;
import UI.Button;
import UI.Menu;

import Util.Object;
import Util.Dragger;
import Util.Globals;

import processing.core.*;
import processing.data.*;

import java.sql.Connection;
import java.sql.DriverManager;

import Data.Load;
import Objects.Character;
import Objects.Coin;
import Objects.Goal;
import Objects.Platform;
import Objects.Spike;
import Objects.Water;


public class Main extends PApplet {
	Dragger dragger;
	Menu menu;
	Load loader;
	int score = 0;
	Button loadButton;
	Button newLevel;

	public void settings() {
		fullScreen();

		Globals.objects = new Object[1000];
		Globals.sketch = this;

		dragger = new Dragger(this);
		menu = new Menu(this);

		PApplet self = this;
		loadButton = new Button(this, (float) this.width - 50f, 50f, 60f, 60f, "Load", new int[] { 0, 0, 255 }, 
		new Activate(){
			public boolean clicked(boolean click) {
				loadButton.selected = false;
				if (click) {
					Load.Reader(self);
				}
				return false;
			}
		}, false);
		newLevel = new Button(this, (float) this.width + 20, 50f, 60f, 60f, "New", new int[] { 0, 255, 0 },
		new Activate(){
			public boolean clicked(boolean click) {
				newLevel.selected = false;
				if (click) {
					Globals.PLAY_ONLY = false;
					Globals.IS_EDITOR = true;
					Globals.objects = new Object[1000];
				}
				return false;
			}
		}, false);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Globals.conn = DriverManager.getConnection("jdbc:mysql://aws-eu-west-2.connect.psdb.cloud/ddu?sslMode=VERIFY_IDENTITY", "vj990rpjcr1tx3hbub6o", "pscale_pw_dS2OYenQV4PRACUK3U1PhJZvU73w0ri8ykXpWDDJXD");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw() {
		clear();
		fill(255, 255, 255);
		background(Globals.BG_COLOR);
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

		if (Globals.selected != null && Globals.IS_EDITOR) {
			strokeWeight(7);
			stroke(255, 0, 0);
			fill(0,0,0,0);
			rect(Globals.selected.getPosition().x, Globals.selected.getPosition().y, Globals.selected.getWidth() + 7, Globals.selected.getHeight() + 7);
			strokeWeight(1);
			stroke(0);
		}

		if (!Globals.PLAY_ONLY) {
			menu.Draw();
		} else {
			Globals.IS_EDITOR = false;
			loadButton.Draw();
			newLevel.Draw();
		}

		if (!Globals.IS_EDITOR && characterCount > 0 && characterCount == finishedCount) {
			textSize(50);
			fill(255,255,255);
			text("PRESS ENTER TO RESTART", displayWidth / 2, 120);
			text("Score: " + Globals.Score, displayWidth / 2, 175);
			if (key == ENTER) {
				for (Object object : Globals.objects) {
					if (object != null) {
						object.Reset();
					}
				}
			}
		}
	}
	
	public void mouseClicked() {
		if (Globals.IS_EDITOR) {
			dragger.mouseClicked();
		} else {
			loadButton.OnClick();
			newLevel.OnClick();
		}
		if (!Globals.PLAY_ONLY) {
			menu.mouseClicked();
		}
	}

	public void mouseDragged() {
		if (Globals.IS_EDITOR) {
			dragger.mouseDragged();
		}
	}

	public void mouseReleased() {
		if (Globals.IS_EDITOR) {
			dragger.mouseReleased();
		}
	}


	private boolean IS_CTRL_DOWN = false;
	public void keyPressed() {
		setMove(key, true);
		if (key == PConstants.CODED && keyCode == PConstants.SHIFT) {
			Globals.IS_SHIFT_PRESSED = true;
		}
		if (keyCode == PConstants.CONTROL) {
			IS_CTRL_DOWN = true;
		}
		if (IS_CTRL_DOWN && !Globals.PLAY_ONLY) {
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
			if (key == '1') {
				InsertEmpty(new Platform(this, 200, 200, new PVector(this.mouseX, this.mouseY)));
			} else if (key == '2') {
				InsertEmpty(new Water(this, 200, 200, new PVector(this.mouseX, this.mouseY)));
			} else if (key == '3') {
				InsertEmpty(new Character(this, 100, 100, new PVector(this.mouseX, this.mouseY)));
			} else if (key == '4') {
				InsertEmpty(new Coin(this, false, new PVector(this.mouseX, this.mouseY)));
			} else if (key == '5') {
				InsertEmpty(new Goal(this, new PVector(this.mouseX, this.mouseY)));
			} else if (key == '6') {
				InsertEmpty(new Spike(this, new PVector(this.mouseX, this.mouseY), 50));
			}
		}
	}

	public void InsertEmpty(Object newItem) {
		for (int i = 0; i < Globals.objects.length; i++) {
			if (Globals.objects[i] == null) {
				Globals.objects[i] = newItem;
				break;
			}
		}
	}

	public void keyReleased() {
		setMove(key, false);
		if (key == PConstants.CODED && keyCode == PConstants.SHIFT) {
			Globals.IS_SHIFT_PRESSED = false;
		}
		if (keyCode == PConstants.CONTROL) {
			IS_CTRL_DOWN = false;
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