package Objects;

import Util.CollisionCircle;
import Util.Object;
import Util.Score;
import processing.core.*;

public class Coin extends Object {
    private PApplet sketch;
	private boolean collected;
	public PVector position = new PVector();
    private PVector defaultPosition;
    private CollisionCircle collisionBox;
	private Score updateCoins;

    public Coin(PApplet sketch, boolean collected, PVector position, Score updateCoins) {
        this.sketch = sketch;
        this.collected = collected;
        this.position = position;
        this.defaultPosition = new PVector(position.x, position.y);
        this.collisionBox = new CollisionCircle(this.position.x, this.position.y, 20);
		this.updateCoins = updateCoins;
    }

    public void Reset() {
        this.collected = false;
        this.position.x = this.defaultPosition.x;
        this.position.y = this.defaultPosition.y;

    }

    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.defaultPosition.set(position);
        this.collisionBox.Update(this.position.x, this.position.y);
    }

    public void Draw(PVector position) {
        if (collected == false) {
            this.sketch.circle(position.x, position.y, 20f);
        }    
    }

    public void IsCollected(CollisionCircle c1) {
        if (c1.IsColliding(this.collisionBox)) {
			this.collected = true;
            this.updateCoins.add(1);
		}
    }

	public boolean MouseDragged(PApplet sketch) {
		if (this.collisionBox.IsInOver(new PVector(sketch.mouseX, sketch.mouseY)) || this.isDragging) {
			this.isDragging = true;
			this.UpdateStartPosition(new PVector(sketch.mouseX - (this.width / 2),
			sketch.mouseY - (this.height / 2)));
			return true;
		}
		return false;
	}
}
