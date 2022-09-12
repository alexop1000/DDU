package Objects;

import Util.CollisionBox;
import Util.Object;
import processing.core.*;;

public class Platform extends Object {
	private PApplet sketch;

	public PVector position = new PVector();
	public int width = 200;
	public int height = 200;

	public CollisionBox collisionBox;

	public Platform(PApplet sketch, int w, int h, PVector position) {
		this.sketch = sketch;

		this.position.set(position);
		this.width = w;
		this.height = h;
		this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.width, this.height);
	}

	public void UpdateStartPosition(int width, int height, PVector position) {
		this.width = width;
		this.height = height;
		this.position.set(position);
		this.collisionBox.Update(this.position.x, this.position.y);
	}

	public void Draw() {
		this.sketch.fill(0, 0, 255);
		this.sketch.rect(this.position.x, this.position.y, this.width, this.height);
	}

	public boolean MouseDragged() {
		if (this.collisionBox.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(this.width, this.height, new PVector(this.sketch.mouseX - (this.width / 2), this.sketch.mouseY - (this.height / 2)));
			return true;
		}
		return false;
	}
}
