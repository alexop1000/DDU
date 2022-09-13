package Objects;

import Util.CollisionBox;
import Util.CollisionCircle;
import Util.Object;
import processing.core.*;
import Util.Globals;

public class Coin extends Object {
    private PApplet sketch;
	private boolean collected;
	public PVector position = new PVector();
    private PVector defaultPosition;
    private CollisionBox collisionBox;

    public Coin(PApplet sketch, boolean collected, PVector position) {
        this.sketch = sketch;
        this.collected = collected;
        this.position = position;
        this.defaultPosition = new PVector(position.x, position.y);
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, 50, 50);
    }

    public void Reset() {
        this.collected = false;
        this.position.x = this.defaultPosition.x;
        this.position.y = this.defaultPosition.y;

    }

    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.defaultPosition.set(position);
        this.position = position;
        this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
    }

    public void Draw() {
        if (this.collected == false) {
            this.sketch.fill(253, 218, 13);
            this.sketch.circle(this.position.x, this.position.y, 50f);
        }    
    }

    public void Collect(CollisionBox c1) {
        if (c1.IsColliding(this.collisionBox) && !this.collected) {
			this.collected = true;
            Globals.Score += 1;
            System.out.println(Globals.Score);
		}
    }

	public boolean MouseDragged() {
		if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}
		if (this.collisionBox.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}
}
