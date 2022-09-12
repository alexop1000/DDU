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

	public Button(PApplet sketch, float x, float y, float w, float h, String[] label, int[] SCTemp, int[] DCTemp) {
		this.sketch = sketch;

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.label = label;
		selected = false;
		selectedColor = sketch.color(SCTemp[0], SCTemp[1], SCTemp[2]);
		defaultColor = sketch.color(DCTemp[0], DCTemp[1], DCTemp[2]);
		currentColor = defaultColor;
	}

	public void display() {
		this.sketch.fill(currentColor);
		this.sketch.rect(x, y, w, h);
		this.sketch.fill(0);
		this.sketch.textAlign(PConstants.CENTER);
		this.sketch.text(selected ? label[1] : label[0], x + w / 2, y + (h / 2));
	}

	public boolean clicked(int mx, int my) {
		if (mx > x && mx < x + w && my > y && my < y + h) {
			selected = !selected;
			if (selected) {
				currentColor = selectedColor;
			} else {
				currentColor = defaultColor;
			}
		}
		return selected;
	}

}