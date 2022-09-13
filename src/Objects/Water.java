package Objects;

import processing.core.*;

import Util.Globals;
import Util.CollisionBox;
import Util.Object;

public class Water extends Object {
	private PApplet sketch;

	public PVector position = new PVector();
    public PVector defaultPosition = new PVector();
	public float width = 200;
	public float height = 200;

	private CollisionBox corner;
	public CollisionBox collisionBox;
	public CollisionBox delete;

	public PImage scaleImage;
	public PImage trashImage;

	public Water(PApplet sketch, int w, int h, PVector position) {
		this.sketch = sketch;

		this.position.set(position);
		this.width = w;
		this.height = h;
		this.collisionBox = new CollisionBox(this.position.x, this.position.y, (int)this.width, (int)this.height);
        this.defaultPosition = position;

		this.corner = new CollisionBox(this.position.x + this.width, this.position.y + this.height - 30, 30, 30);
		this.delete = new CollisionBox(this.position.x + this.width + 5, this.position.y + this.height - 65, 30, 30);

		this.scaleImage = sketch.requestImage("./res/ScalingIcon.png");
		this.trashImage = sketch.requestImage("./res/TrashCan.png");
	}

    public void Draw() {
        this.sketch.fill(this.sketch.color(60, 170, 230), 80f);
        this.sketch.rect(this.position.x, this.position.y, this.width, this.height);

		if (Globals.IS_EDITOR && Globals.IS_SHIFT_PRESSED) {
			this.sketch.fill(255, 255, 255);
			this.sketch.rect(this.position.x + this.width + 5, this.position.y + this.height - 30, 30, 30);
			this.sketch.image(this.scaleImage, this.position.x + this.width + 5, this.position.y + this.height - 30);

			this.sketch.rect(this.position.x + this.width + 5, this.position.y + this.height - 65, 30, 30);
			this.sketch.image(this.trashImage, this.position.x + this.width + 5, this.position.y + this.height - 65);
		}
    }

    public void UpdateStartPosition(float width, float height, PVector position) {
		this.width = width;
		this.height = height;
        this.position.set(position);
        this.defaultPosition.set(position);
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);

		this.corner.Update(this.position.x + this.width, this.position.y + this.height - 30, 30, 30);
		this.delete.Update(this.position.x + 5 + this.width, this.position.y + this.height - 65, 30, 30);
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

	public boolean MouseExtending() {
		if (this.corner.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isExtending) {
			super.isExtending = true;
			this.UpdateStartPosition(PApplet.constrain(this.sketch.mouseX - this.position.x, 102, 1920), PApplet.constrain(this.sketch.mouseY - this.position.y, 102, 1080), this.position);
			return true;
		}
		return false;
	}
}
