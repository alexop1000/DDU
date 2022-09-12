package Objects;

import processing.core.*;
import Util.*;
import Util.Object;
import Util.Globals;

public class Character extends Object {
	private PApplet sketch;

    public PVector velocity = new PVector();
    public PVector position = new PVector();
    private PVector startPosition = new PVector();
    private PVector acceleration = new PVector();
    public PImage charImage;
    public int width = 200;
    public int height = 200;
    private float gravity = 0.4f;
    private int mass = 5;
    public boolean onGround = false;

    public CollisionBox collisionBox;
    

    public Character(PApplet sketch, int mass, int width, int height, PVector startPos) {
		this.sketch = sketch;

        this.mass = mass;
        this.width = width;
        this.height = height;
        this.startPosition.set(startPos);
        this.position.set(startPos);
		this.charImage = sketch.requestImage("./Images/Character.png");
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.width, this.height);
    }

    public void Update() {
		this.collisionBox.Update(this.position.x, this.position.y);
		
        this.acceleration.y = gravity;
        this.velocity.x = (float) (this.velocity.x * 0.95);
        this.velocity.add(acceleration);
        this.velocity.x = PApplet.constrain(this.velocity.x, -30, 30);
        this.velocity.y = PApplet.constrain(this.velocity.y, -30, 30);

        this.position.add(this.velocity);

        this.acceleration.mult(0);

        if (this.position.x > (this.sketch.width - this.width) || this.position.x < 0) {
            this.position.x = PApplet.constrain(this.position.x, 0, (this.sketch.width - this.width));
            this.velocity.x = 0;
        }
        if (this.position.y > (this.sketch.height - this.height) || this.position.y < 0) {
            this.position.y = PApplet.constrain(this.position.y, 0, (this.sketch.height - this.height));
            this.velocity.y = -(this.velocity.y * mass/20);
        } 

		boolean wasGround = false;
		for (Object object : Globals.objects) {
			if (object == this) continue;
			if (object instanceof Platform) {
				Platform platform = (Platform) object;
				if (platform.collisionBox.IsColliding(this.collisionBox)) {
					if (this.position.x <= platform.position.x && this.position.x >= platform.position.x - this.width && this.position.y + (this.height / 2) > platform.position.y - (platform.height / 2) && this.position.y - (this.height / 2) < platform.position.y + (platform.height / 2) - 5) {
						this.position.x = platform.position.x - this.width;
						this.velocity.x = 0;
					} else if (this.position.y <= platform.position.y) {
						this.position.y = platform.position.y - platform.height;
						this.velocity.y = -(this.velocity.y * mass/20);
						this.onGround = true;
						wasGround = true;
					} else if (this.position.y >= platform.position.y) {
						this.position.y = platform.position.y + platform.height;
						this.velocity.y = 0;
						this.onGround = false;
						wasGround = false;
					} 
				}
			}
		}

        if (this.position.y == this.sketch.height - this.height) {
            this.onGround = true;
        } else if (!wasGround) {
            this.onGround = false;
        }
	}

	public void Draw() {
		if(this.charImage.width > 0) {
			this.sketch.image(this.charImage, this.position.x, this.position.y);
		} else {
			this.sketch.square(this.GetPosition().x, this.GetPosition().y, 100);
		}
	}

	public void SetVelocity(PVector velocity) {
		this.velocity.set(velocity);
	}

	public void AddVelocity(PVector velocity) {
		this.velocity.add(velocity);
	}

    public PVector GetPosition() {
        return this.position;
    }

    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.startPosition.set(position);
        this.collisionBox.Update(this.position.x, this.position.y);
    }

    public void Reset() {
        this.position.set(this.startPosition);
        this.velocity.set(0, 0);
        this.collisionBox.Update(this.position.x, this.position.y);
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