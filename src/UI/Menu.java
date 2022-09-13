package UI;

import Objects.Character;
import Objects.Coin;
import Objects.Platform;
import Objects.Water;
import Util.Globals;
import processing.core.PApplet;
import processing.core.PVector;

public class Menu {
	public float width = 300f;
	public float height;
	public PApplet sketch;
	public PVector position;
	public Button[] buttons = new Button[15];
	public boolean hidden = false;

	public Menu(PApplet sketch) {
		this.sketch = sketch;

		this.height = this.sketch.displayHeight - 100;
		this.position = new PVector(this.sketch.displayWidth - width - 50, 50);

		// Play Button
		buttons[0] = new Button(this.sketch, this.position.x + 30, this.position.y + this.height - 90, 60, 60, new String[] {"Start", "Stop" }, new int[] { 0, 255, 0 },
		new int[] { 255, 0, 0 }, new Activate(){
			public boolean clicked(boolean click) {
				Globals.IS_EDITOR = click; 
				for (Object object : Globals.objects) {
					if (object instanceof Character) {
						Character character = (Character) object;
						character.Reset();
					} else if (object instanceof Coin) {
						Coin coin = (Coin) object;
						coin.Reset();
					} else if (object instanceof Water) {
						Water water = (Water) object;
						water.Reset();
					}
				}
				return Globals.IS_EDITOR;
			}
		}, Globals.IS_EDITOR);

		// Hide Button
		buttons[1] = new Button(this.sketch, this.position.x - 40, this.position.y + (this.height / 2), 30, 30, new String[] {"+", "-" }, new int[] { 0, 255, 0 }, new int[] { 255, 0, 0 },
		new Activate(){
			public boolean clicked(boolean click) {
				hidden = click;
				return hidden;
			}
		}, false);

		// Clear Button
		buttons[2] = new Button(this.sketch, this.position.x + 95, this.position.y + this.height - 90, 60, 60, "Delete", new int[] { 255, 0, 0 },
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

		String[] buttonNames = new String[] {"Platform", "Water", "Character", "Coin"};
        for (int i = 0; i < buttonNames.length; i++){
			final int newInt = i;
            buttons[i + 3] = new Button(this.sketch, 
			i % 3 == 0 ? this.position.x + 19 : (i % 3 == 1 ? this.position.x + 109 : this.position.x + 199), 
				this.position.y + (int)Math.floor(i / 3) * 90 + 30,
				80, 80, buttonNames[i], new int[] { 150, 150, 150 }, 
				new Activate(){
					public boolean clicked(boolean click) {
						buttons[newInt + 3].selected = false;
						if (click) {
							buttons[1].activate.clicked(true);
							buttons[1].selected = true;
							buttons[1].currentColor = buttons[1].selectedColor;
							if (buttonNames[newInt] == "Platform") {
								Platform newPlatform = new Platform(sketch, 200, 200, new PVector(sketch.mouseX, sketch.mouseY));
								for (int i = 0; i < Globals.objects.length; i++) {
									if (Globals.objects[i] == null) {
										Globals.objects[i] = newPlatform;
										break;
									}
								}
							} else if (buttonNames[newInt] == "Water") {
								Water newWater = new Water(sketch, 200, 200, new PVector(sketch.mouseX, sketch.mouseY));
								for (int i = 0; i < Globals.objects.length; i++) {
									if (Globals.objects[i] == null) {
										Globals.objects[i] = newWater;
										break;
									}
								}
							} else if (buttonNames[newInt] == "Character") {
								Character newChar = new Character(sketch, 1, 100, 100, new PVector(sketch.mouseX, sketch.mouseY));
								for (int i = 0; i < Globals.objects.length; i++) {
									if (Globals.objects[i] == null) {
										Globals.objects[i] = newChar;
										break;
									}
								}
							} else if (buttonNames[newInt] == "Coin") {
								Coin newCoin = new Coin(sketch, false, new PVector(sketch.mouseX, sketch.mouseY));
								for (int i = 0; i < Globals.objects.length; i++) {
									if (Globals.objects[i] == null) {
										Globals.objects[i] = newCoin;
										break;
									}
								}
							}
						}
						return true;
					}
				}, false
			);
        }
	}

    public void Draw(){
		if (!Globals.IS_EDITOR) {
			this.buttons[0].Draw();
			this.buttons[0].x = this.sketch.displayWidth - 90;
			this.buttons[0].y = 30;
		} else {
			if (hidden) {
				buttons[1].Draw();
				buttons[1].x = this.sketch.displayWidth - 40;
				buttons[1].y = this.sketch.displayHeight / 2;
				return;
			} else {
				buttons[1].Draw();
				buttons[1].x = this.position.x - 40;
				buttons[1].y = this.position.y + (this.height / 2);
			}
			// Cool start button
			this.buttons[0].x = this.position.x + 30;
			this.buttons[0].y = this.position.y + this.height - 90;

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
