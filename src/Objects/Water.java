package Objects;

import processing.core.*;

import Util.Globals;
import Util.CollisionBox;
import Util.Object;
import Util.ObjectControls;

public class Water extends Object {
	private PApplet sketch;

	public PVector position = new PVector();
    public PVector defaultPosition = new PVector();
	public float width = 200;
	public float height = 200;

	public CollisionBox collisionBox;

	public Water(PApplet sketch, int w, int h, PVector position) {
		this.sketch = sketch;

		this.position.set(position);
		this.width = w;
		this.height = h;
		this.collisionBox = new CollisionBox(this.position.x, this.position.y, (int)this.width, (int)this.height);
        this.defaultPosition = position;

		this.controls = new ObjectControls(this.sketch, (int)this.width, (int)this.height, this.position, false, true, true);
	}

	@Override
    public void Draw() {
        this.sketch.fill(this.sketch.color(60, 170, 230), 80f);
        this.sketch.rect(this.position.x, this.position.y, this.width, this.height);

		this.controls.Draw();
    }

    public void UpdateStartPosition(float width, float height, PVector position) {
		this.width = width;
		this.height = height;
        this.position.set(position);
        this.defaultPosition.set(position);
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);

		this.controls.Update((int)this.width, (int)this.height, this.position);
    }

	@Override
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

	@Override
    public boolean MouseDragged() {
		if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}
		if (this.collisionBox.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(this.width, this.height, new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}

	@Override
	public boolean MouseExtending() {
		if (this.controls.scale.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isExtending) {
			super.isExtending = true;
			this.UpdateStartPosition(PApplet.constrain(this.sketch.mouseX - this.position.x + this.width / 2, 5, 1920), PApplet.constrain(this.sketch.mouseY - this.position.y + this.height / 2, 5, 1080), this.position);
			return true;
		}
		return false;
	}
	@Override
	public boolean GetCollision(PVector position) {
		return this.collisionBox.IsInOver(position);
	}
	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public PVector getPosition() {
		return position;
	}

	@Override
	public float getWidth() {
		return width;
	}
}
