package Objects;

import processing.core.*;

import Util.CollisionBox;
import Util.Object;

public class Water extends Object {
	private PApplet sketch;

	public PVector position = new PVector();
    public PVector defaultPosition = new PVector();
	public int width = 200;
	public int height = 200;

	public CollisionBox collisionBox;

	public Water(PApplet sketch, int w, int h, PVector position) {
		this.sketch = sketch;

		this.position.set(position);
		this.width = w;
		this.height = h;
		this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.width, this.height);
        this.defaultPosition = position;
	}

    public void draw() {
        this.sketch.fill(60, 170, 230, 0.4f);
        this.sketch.rect(this.position.x, this.position.y, this.width, this.height);
    }

    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.defaultPosition.set(position);
        this.collisionBox.Update(this.position.x, this.position.y);
    }

    public void Reset() {
        this.position.x = this.defaultPosition.x;
        this.position.y = this.defaultPosition.y;

    }

    public Boolean InWater(CollisionBox charPos) {
        if (charPos.IsColliding(this.collisionBox)) {
			return true;
		}
        return false;
    }

    public boolean MouseDragged() {
		if (this.collisionBox.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(new PVector(this.sketch.mouseX - (this.width / 2), this.sketch.mouseY - (this.height / 2)));
			return true;
		}
		return false;
	}
}
