package Objects;
import Util.CollisionBox;
import Util.Object;
import Util.ObjectControls;
import processing.core.*;
import Util.Globals;

public class Goal extends Object {
    private PApplet sketch;
	public PVector position = new PVector();
    private PVector defaultPosition;
    private CollisionBox collisionBox;

    public float width = 150;
	public float height = 150;
    private float smallWidth;
	private float smallHeight;

    public Goal(PApplet sketch, PVector position) {
        this.sketch = sketch;
        this.position = position;
        this.defaultPosition = new PVector(position.x, position.y);
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.width, this.height);

        this.controls = new ObjectControls(this.sketch, (int)this.width, (int)this.height, this.position, false, true, true);
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

        this.controls.Update((int)this.width, (int)this.height, this.position);
    }

	@Override
    public void Draw() {
        this.sketch.fill(255);
        this.sketch.rect(this.position.x, this.position.y, this.width, this.height);
        this.sketch.fill(0);
        this.smallWidth = 20 + ((this.width % 20) / ((int)this.width / this.smallWidth / 2));
        this.smallHeight = 20 + ((this.height % 20) / ((int)this.height / this.smallHeight));

        // for loop to make all the black squares in the checker
        for (int i = 0; i < (int)this.height / this.smallHeight; i += 2){
            for (int j = 0; j < (int)this.width / this.smallWidth / 2; j++){
				this.sketch.rect(this.position.x + j * this.smallWidth * 2 - this.width / 2 + this.smallWidth / 2, 
				this.position.y + i * this.smallHeight - this.height / 2 + this.smallHeight/ 2, 
				this.smallWidth, this.smallHeight);
            }
        }
        for (int i = 1; i < (int)this.height / this.smallHeight; i += 2){
            for (int j = 0; j < (int)this.width / this.smallWidth / 2 - 1; j++){
				this.sketch.rect(this.position.x + j * this.smallWidth * 2 - this.width / 2 + this.smallWidth / 2 + this.smallWidth, 
				this.position.y + i * this.smallHeight - this.height / 2 + this.smallHeight/ 2, 
				this.smallWidth, this.smallHeight);
            }
        }

        this.controls.Draw();
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
		if (this.controls.scale.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isExtending) {
			super.isExtending = true;
			this.UpdateStartPosition(PApplet.constrain(this.sketch.mouseX - this.position.x + this.width / 2, 102, 1920), PApplet.constrain(this.sketch.mouseY - this.position.y + this.height / 2, 102, 1080), this.position);
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
	public void setHeight(int height) {
		this.height = height;
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
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}
}
