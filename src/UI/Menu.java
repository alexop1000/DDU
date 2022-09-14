package UI;

import Data.Load;
import Data.Save;
import Objects.Character;
import Objects.Coin;
import Objects.Goal;
import Objects.Platform;
import Objects.Water;
import Objects.Spike;
import Util.Globals;
import Util.Object;
import processing.core.PApplet;
import processing.core.PVector;

public class Menu {
	public float width = 300f;
	public float halfWidth = this.width / 2f;
	public float height;
	public float halfHeight;
	public PApplet sketch;
	public PVector position;
	public Button[] buttons = new Button[40];
	public boolean hidden = false;

	public Menu(PApplet sketch) {
		this.sketch = sketch;

		this.height = this.sketch.displayHeight - 100;
		this.halfHeight = this.height / 2f;
		this.position = new PVector(this.sketch.displayWidth - this.halfWidth - 50, this.height / 2 + 50);

		// Play Button
		buttons[0] = new Button(this.sketch, this.position.x + 30, this.position.y + this.halfHeight - 90, 60, 60, new String[] {"Start", "Stop" }, new int[] { 0, 255, 0 },
		new int[] { 255, 0, 0 }, new Activate(){
			public boolean clicked(boolean click) {
				if (Globals.IS_EDITOR == true && click == false && hidden == true) return true;
				Globals.IS_EDITOR = click; 
				if (Globals.IS_EDITOR == true) {
					for (Object object : Globals.objects) {
						if (object != null) {
							object.Reset();
						}
					}
				}
				return Globals.IS_EDITOR;
			}
		}, Globals.IS_EDITOR);

		// Hide Button
		buttons[1] = new Button(this.sketch, this.position.x - this.halfWidth - 40, this.position.y, 30, 30, new String[] {"+", "-" }, new int[] { 0, 255, 0 }, new int[] { 255, 0, 0 },
		new Activate(){
			public boolean clicked(boolean click) {
				hidden = click;
				return hidden;
			}
		}, false);

		// Clear Button
		buttons[2] = new Button(this.sketch, this.position.x - this.halfWidth + 115, this.position.y + this.halfHeight - 45, 60, 60, "Clear", new int[] { 255, 0, 0 },
		new Activate(){
			public boolean clicked(boolean click) {
				buttons[2].selected = false;
				if (click) {
					for (int i = 0; i < Globals.objects.length; i++) {
						Globals.objects[i] = null;
					}
				}
				return false;
			}
		}, false);

		buttons[3] = new Button(this.sketch, this.position.x - this.halfWidth + 185, this.position.y + this.halfHeight - 45, 60, 60, "Save", new int[] { 4, 157, 191 },
		new Activate(){
			public boolean clicked(boolean click) {
				buttons[3].selected = false;
				if (click) {
					new Save(sketch);
				}
				return false;
			}
		}, false);
		buttons[4] = new Button(this.sketch, this.position.x - this.halfWidth + 255, this.position.y + this.halfHeight - 45, 60, 60, "Load", new int[] { 4, 157, 191 },
		new Activate(){
			public boolean clicked(boolean click) {
				buttons[4].selected = false;
				if (click) {
					new Load(sketch);
				}
				return false;
			}
		}, false);

		String[] buttonNames = new String[] {"Platform", "Water", "Character", "Coin", "Goal", "Spike"};
        for (int i = 0; i < buttonNames.length; i++){
			final int newInt = i;
            buttons[i + 5] = new Button(this.sketch, 
			i % 3 == 0 ? this.position.x + 59 - this.halfWidth : (i % 3 == 1 ? this.position.x + 149 - this.halfWidth : this.position.x + 239 - this.halfWidth), 
				this.position.y - this.halfHeight + (int)Math.floor(i / 3) * 90 + 60,
				80, 80, buttonNames[i], new int[] { 150, 150, 150 }, 
				new Activate(){
					public boolean clicked(boolean click) {
						buttons[newInt + 5].selected = false;
						if (click && !hidden) {
							buttons[1].activate.clicked(true);
							buttons[1].selected = true;
							buttons[1].currentColor = buttons[1].selectedColor;
							if (buttonNames[newInt] == "Platform") {
								InsertEmpty(new Platform(sketch, 200, 200, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Water") {
								InsertEmpty(new Water(sketch, 200, 200, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Character") {
								InsertEmpty(new Character(sketch, 1, 100, 100, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Coin") {
								InsertEmpty(new Coin(sketch, false, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Goal") {
								InsertEmpty(new Goal(sketch, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Spike") {
								InsertEmpty(new Spike(sketch, new PVector(sketch.mouseX, sketch.mouseY)));
							}
						}
						return true;
					}
				}, false
			);
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

    public void Draw(){
		if (!Globals.IS_EDITOR) {
			this.buttons[0].Draw();
			this.buttons[0].x = this.sketch.displayWidth / 2;
			this.buttons[0].y = 50;
		} else {
			if (hidden) {
				buttons[1].Draw();
				buttons[1].x = this.sketch.displayWidth - 20;
				buttons[1].y = this.sketch.displayHeight / 2;

				this.position.x = this.sketch.displayWidth * 2;
				return;
			} else {
				buttons[1].Draw();
				buttons[1].x = this.position.x - this.halfWidth - 20;
				buttons[1].y = this.position.y;
				this.position.x = this.sketch.displayWidth - this.halfWidth - 50;
			}
			// Cool start button
			this.buttons[0].x = this.position.x - this.halfWidth + 45;
			this.buttons[0].y = this.position.y + this.halfHeight - 45;

			// The rest of the menu
			this.sketch.fill(140, 153, 173);
			this.sketch.rect(position.x, position.y, width, height, 28);
			if (buttons != null) {
				for (Button button : this.buttons) {
					if (button == null) continue;
					button.Draw();
				}
			}
		}
    }

	public void mouseClicked() {
		if (buttons != null) {
			for (Button button : this.buttons) {
				if (button == null) continue;
				button.OnClick();
			}
		}
	}
}
