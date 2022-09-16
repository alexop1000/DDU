package UI;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
import processing.core.PConstants;
import processing.core.PImage;
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
	public PImage checkmark;
	private HashMap<String, List<TextBox>> textBoxes = new HashMap<String, List<TextBox>>();

	public Menu(PApplet sketch) {
		this.sketch = sketch;

		this.height = this.sketch.displayHeight - 100;
		this.halfHeight = this.height / 2f;
		this.position = new PVector(this.sketch.displayWidth - this.halfWidth - 50, this.height / 2 + 50);

		this.checkmark = sketch.requestImage(Menu.class.getClassLoader().getResource("Checkmark.png").toString());

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
					Globals.selected = null;
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
					Load.Reader(sketch);
				}
				return false;
			}
		}, false);

		buttons[5] = new Button(this.sketch, this.position.x - this.halfWidth + 80, this.position.y + this.halfHeight - 115, 130, 60, "Background", new int[] { 255, 255, 255 },
		new Activate(){
			public boolean clicked(boolean click) {
				buttons[5].selected = false;
				if (click) {
					int color = Globals.PickColor(sketch, 0);
					Globals.BG_COLOR = color;
				}
				return false;
			}
		}, false);

		buttons[6] = new Button(this.sketch, this.position.x - this.halfWidth + 220, this.position.y + this.halfHeight - 115, 130, 60, new String[] {"Play only: on", "Play only: off"}, new int[] { 0, 255, 0 }, new int[] { 255, 255, 255},
		new Activate(){
			public boolean clicked(boolean click) {
				if (click) {
					Globals.TEMP_PLAY_ONLY = !Globals.TEMP_PLAY_ONLY;
					return Globals.TEMP_PLAY_ONLY;
				}
				return false;
			}
		}, false);
		
		String[] buttonNames = new String[] {"Platform", "Water", "Character", "Coin", "Goal", "Spike"};
        for (int i = 0; i < buttonNames.length; i++){
			final int newInt = i;
            buttons[i + 7] = new Button(this.sketch, 
			i % 3 == 0 ? this.position.x + 59 - this.halfWidth : (i % 3 == 1 ? this.position.x + 149 - this.halfWidth : this.position.x + 239 - this.halfWidth), 
				this.position.y - this.halfHeight + (int)Math.floor(i / 3) * 90 + 60,
				80, 80, buttonNames[i], new int[] { 150, 150, 150 }, 
				new Activate(){
					public boolean clicked(boolean click) {
						buttons[newInt + 7].selected = false;
						if (click && !hidden) {
							buttons[1].activate.clicked(true);
							buttons[1].selected = true;
							buttons[1].currentColor = buttons[1].selectedColor;
							if (buttonNames[newInt] == "Platform") {
								InsertEmpty(new Platform(sketch, 200, 200, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Water") {
								InsertEmpty(new Water(sketch, 200, 200, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Character") {
								InsertEmpty(new Character(sketch, 100, 100, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Coin") {
								InsertEmpty(new Coin(sketch, false, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Goal") {
								InsertEmpty(new Goal(sketch, new PVector(sketch.mouseX, sketch.mouseY)));
							} else if (buttonNames[newInt] == "Spike") {
								InsertEmpty(new Spike(sketch, new PVector(sketch.mouseX, sketch.mouseY), 50));
							}
						}
						return true;
					}
				}, false
			);
        }

		// Add text fields for each object
		float lastWidth = this.position.x - this.halfWidth + 80;
		float lastHeight = this.position.y - this.halfHeight + 60;

		// A list of all our object class types
		Class<?>[] objectTypes = {Platform.class, Water.class, Character.class, Coin.class, Goal.class, Spike.class};
		for (Class<?> object : objectTypes) {
			if (object == null) continue;
			// Insert a textbox with the name of the class (e.g. Platform)
			textBoxes.put(object.getSimpleName(), new LinkedList<TextBox>());
			// Add a field for each property in the object
			for (Field method : object.getFields()) {
				if (method.getType() != int.class && method.getType() != float.class) continue;
				textBoxes.get(object.getSimpleName()).add(
					new TextBox((int) lastWidth, (int) lastHeight, 130, 60, method.getName(), "Loading")
				);
				lastHeight += 70;
				if (lastHeight > this.position.y + this.halfHeight - 60) {
					lastHeight = this.position.y - this.halfHeight + 60;
					lastWidth += 140;
				}
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

			Globals.sketch.textAlign(PConstants.RIGHT);
			buttons[6].Draw();			
			Globals.sketch.textAlign(PConstants.CENTER);

			textBoxes.forEach((key, value) -> {
				if (Globals.selected != null && Globals.selected.getClass().getSimpleName() == key) {
					float lastWidth = this.position.x - this.halfWidth + 80;
					float lastHeight = this.position.y - 140;
					float lastI = 0;

					for (TextBox textBox : value) {
						lastI += 1;
						textBox.Update((int) lastWidth, (int) lastHeight, 130, 60);
						if (!textBox.isFocused) {
							try {
								textBox.textValue = String.valueOf(Globals.selected.getClass().getField(textBox.name).get(Globals.selected));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						textBox.Draw();
						lastHeight += 90;
						if (lastI > 4) {
							lastWidth += 140;
							lastHeight = this.position.y - 140 + 90 * (lastI - 5);
						}
					}
				}
			});		
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
