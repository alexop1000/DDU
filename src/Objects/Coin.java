package Objects;

import Util.CollisionBox;
import Util.Object;
import Util.ObjectControls;
import processing.core.*;
import Util.Globals;

public class Coin extends Object {
    private PApplet sketch;
	private boolean collected;
	public PVector position = new PVector();
    public int coinReward = 1;
    private PVector defaultPosition;
    private CollisionBox collisionBox;

    public Coin(PApplet sketch, boolean collected, PVector position) {
        this.sketch = sketch;
        this.collected = collected;
        this.position = position;
        this.defaultPosition = new PVector(position.x, position.y);
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, 50, 50);

        this.controls = new ObjectControls(this.sketch, 50, 50, this.position, false, true, false);
    }

	@Override
    public void Reset() {
        this.collected = false;
        this.position.x = this.defaultPosition.x;
        this.position.y = this.defaultPosition.y;
        Globals.Score = 0;
    }

    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.defaultPosition.set(position);
        this.position = position;
        this.collisionBox.Update(this.position.x, this.position.y, 50, 50);

        this.controls.Update(50, 50, this.position);
    }

	@Override
    public void Draw() {
        if (this.collected == false) {
            this.sketch.fill(253, 218, 13);
            this.sketch.circle(this.position.x, this.position.y, 50f);

            this.controls.Draw();
        }    
    }

    public void Collect(CollisionBox c1) {
        if (c1.IsColliding(this.collisionBox) && !this.collected) {
			this.collected = true;
            Globals.Score += coinReward;
		}
    }

    @Override
	public boolean MouseDragged() {
		if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}
		if (this.collisionBox.IsInOver(new PVector(this.sketch.mouseX, this.sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}

    @Override
	public boolean MouseExtending() { return false; }

	@Override
	public boolean GetCollision(PVector position) {
		return this.collisionBox.IsInOver(position);
	}
	@Override
	public float getHeight() {
		return 50;
	}
	@Override
	public PVector getPosition() {
		return position;
	}

	@Override
	public void setPosition(PVector position) {
		this.position = position;
	}
	@Override
	public float getWidth() {
		return 50;
	}
}
