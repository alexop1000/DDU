package UI;

import Util.Globals;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class TextBox {
	public int x;
	public int y;
	public int width;
	public int height;
	public String textValue = "";
	public int textLimit = 9;
	public boolean isFocused = false;
	public String type = "number";
	public String name = "";

	public TextBox(int x, int y, int width, int height, String name, String defaultText) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.textValue = defaultText;
		this.name = name;
		Globals.sketch.registerMethod("keyEvent", this);
		Globals.sketch.registerMethod("mouseEvent", this);
	}

	public void Update(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void Draw() {
		Globals.sketch.fill(255);
		Globals.sketch.rect(this.x, this.y, this.width, this.height, 28);

		Globals.sketch.fill(0);
		Globals.sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
		Globals.sketch.text(this.name.substring(0, 1).toUpperCase() + this.name.substring(1), this.x, this.y - this.height / 2 - 15);

		Globals.sketch.textAlign(PConstants.LEFT, PConstants.CENTER);
		Globals.sketch.text(this.textValue + (this.isFocused ? (Globals.sketch.frameCount % 60 <= 35 ? "|" : "") : ""), this.x - this.width / 3, this.y);
	}

	public void keyEvent(KeyEvent event) {
		if (event.isAutoRepeat() || !this.isFocused) return;
		switch (event.getAction()) {
			case KeyEvent.TYPE:
				if (event.getKey() == PConstants.BACKSPACE && this.textValue.length() > 0) {
					this.textValue = textValue.substring(0, this.textValue.length() - 1);
				} else if (this.type == "number" ? ((event.getKey() >= '0' && event.getKey() <= '9') || event.getKey() == '.' || event.getKey() == '-') : (event.getKey() >= ' ')) {
					this.textValue += PApplet.str(event.getKey());
				}
				if (this.textValue.length() > this.textLimit) {
					this.textValue = this.textValue.substring(0, this.textLimit);
				}
				if (Globals.selected != null && this.type == "number") {
					try {
						if (this.textValue != "") {
							Globals.selected.getClass().getField(this.name).set(Globals.selected, Float.parseFloat(this.textValue));
						}
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						// e.printStackTrace();
					}
				} else {
					Globals.NEXT_LEVEL_NAME = this.textValue;
				}
				break;
		
			default:
				break;
		}
	}

	public void mouseEvent(MouseEvent event) {
		switch (event.getAction()) {
			case MouseEvent.CLICK:
				if (event.getX() >= this.x - (this.width / 2) && event.getX() <= this.x + (this.width / 2) 
					&& event.getY() >= this.y - (this.height / 2) && event.getY() <= this.y + (this.height / 2)) {
					this.isFocused = true;
				} else {
					this.isFocused = false;
				}
				break;
		
			default:
				break;
		}
	}
}
