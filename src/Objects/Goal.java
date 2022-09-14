package Objects;
import Util.CollisionBox;
import Util.Object;
import processing.core.*;
import Util.Globals;

public class Goal extends Object {
    private PApplet sketch;
	public PVector position = new PVector();
    private PVector defaultPosition;
    private CollisionBox collisionBox;
    private CollisionBox corner;

    public float width = 150;
	public float height = 150;
    public float smallWidth;
	public float smallHeight;
    public PImage scaleImage;

    public Goal(PApplet sketch, PVector position) {
        this.sketch = sketch;
        this.position = position;
        this.defaultPosition = new PVector(position.x, position.y);
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.width, this.height);

        this.scaleImage = sketch.requestImage("./res/ScalingIcon.png");
        float halfWidth = this.width / 2;
		float halfHeight = this.height / 2;
        this.corner = new CollisionBox(this.position.x + halfWidth + 20, this.position.y + halfHeight - 20, 30, 30);
    }

	public boolean CheckFinished(Character c1) {
        if (c1.collisionBox.IsColliding(this.collisionBox) && !c1.isFinished) {
			return true;
		}
		return false;
    }

	@Override
    public void Reset() {
        this.position.x = this.defaultPosition.x;
        this.position.y = this.defaultPosition.y;
    }

    public void UpdateStartPosition(float width, float height, PVector position) {
        this.width = width;
		this.height = height;
        this.position.set(position);
        this.defaultPosition.set(position);
		
        this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);

        float halfWidth = this.width / 2;
		float halfHeight = this.height / 2;
        this.corner.Update(this.position.x + 20 + halfWidth, this.position.y + halfHeight - 20, 30, 30);
    }

	@Override
    public void Draw() {
        this.sketch.fill(255);
        this.sketch.rect(this.position.x, this.position.y, this.width, this.height);
        this.sketch.fill(0);
        this.smallWidth = 20 + ((this.width % 20) / (float)Math.floor(this.width / 20));
        this.smallHeight = 20 + ((this.height % 20) / (float)Math.floor(this.height / 20));

        // for loop to make all the black squares in the checker
        for (int i = 0; i < (int)this.height / this.smallHeight; i++){
            for (int j = 0; j < (int)this.width / this.smallWidth / 2; j++){
                if (i % 2 != 0 && j + 1 == (int)this.width / this.smallWidth / 2){
                    break;
                }
				this.sketch.rect(this.position.x + j * this.smallWidth * 2 - this.width / 2 + this.smallWidth / 2 + (i % 2) * this.smallWidth, 
				this.position.y + i * this.smallHeight - this.height / 2 + this.smallHeight/ 2, 
				this.smallWidth, this.smallHeight);
            }
        }

        if (Globals.IS_EDITOR && Globals.IS_SHIFT_PRESSED) {
			float halfWidth = this.width / 2;
			float halfHeight = this.height / 2;
			this.sketch.fill(255, 255, 255);
			this.sketch.rect(this.position.x + halfWidth + 20, this.position.y + halfHeight - 20, 30, 30);
			this.sketch.image(this.scaleImage, this.position.x + halfWidth + 20, this.position.y + halfHeight - 20);
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
			this.UpdateStartPosition(this.width,this.height, new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}

	@Override
    public boolean MouseExtending() {
		if (this.corner.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isExtending) {
			super.isExtending = true;
			this.UpdateStartPosition(PApplet.constrain(this.sketch.mouseX - this.position.x + this.width / 2, 102, 1920), PApplet.constrain(this.sketch.mouseY - this.position.y + this.height / 2, 102, 1080), this.position);
			return true;
		}
		return false;
	}
}
