package UI;
import processing.core.PApplet;
import processing.core.PConstants;

public class Button {
	private PApplet sketch;
	float x, y;
	float w, h;
	boolean selected;
	int selectedColor, defaultColor, currentColor;
	String[] label;
	Activate activate;

	public Button(PApplet sketch, float x, float y, float w, float h, String[] label, int[] SCTemp, int[] DCTemp, Activate activate, boolean selected) {
		this.sketch = sketch;

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.label = label;
		this.activate = activate;
		this.selected = selected;
		selectedColor = sketch.color(SCTemp[0], SCTemp[1], SCTemp[2]);
		defaultColor = sketch.color(DCTemp[0], DCTemp[1], DCTemp[2]);
		currentColor = defaultColor; 
		if (this.selected) {
			currentColor = selectedColor;
		} else {
			currentColor = defaultColor;
		}
	}
	public Button(PApplet sketch, float x, float y, float w, float h, String label, int[] color, Activate activate, boolean selected) {
		this.sketch = sketch;

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.label = new String[]{label, label};
		this.activate = activate;
		this.selected = selected;
		selectedColor = sketch.color(color[0], color[1], color[2]);
		defaultColor = sketch.color(color[0], color[1], color[2]);
		currentColor = defaultColor; 
		if (this.selected) {
			currentColor = selectedColor;
		} else {
			currentColor = defaultColor;
		}
	}

	public boolean OnClick() {
		boolean didClick = clicked(this.sketch.mouseX, this.sketch.mouseY);
		if (this.activate != null) {
			this.activate.clicked(didClick);
		}
		return didClick;
	}

	public void Draw() {
		this.sketch.fill(currentColor);
		this.sketch.rect(x, y, w, h, 10);
		this.sketch.fill(0);
		this.sketch.textAlign(PConstants.CENTER);
		this.sketch.textSize(20);
		this.sketch.text(this.selected ? label[0] : label[1], x + w / 2, y + (h / 2) + 5);
	}

	public boolean clicked(int mx, int my) {
		if (mx > x && mx < x + w && my > y && my < y + h) {
			this.selected = !this.selected;
			if (this.selected) {
				currentColor = selectedColor;
			} else {
				currentColor = defaultColor;
			}
		}
		return this.selected;
	}

}