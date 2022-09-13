package Objects;

import processing.core.*;
import Util.*;
import Util.Object;
import Util.Globals;

public class Character extends Object {
    //PApplet object needed to call internal processing methods
	private PApplet sketch;

    //Parameters relating to the characters physics
    public PVector velocity = new PVector();
    public PVector position = new PVector();
    private PVector startPosition = new PVector();
    private PVector acceleration = new PVector();
    public int width = 200;
    public int height = 200;
    private float gravity = 0.4f;
    private int mass = 5;
    public boolean onGround = false;
    public boolean inWater = false;
    public CollisionBox collisionBox;

    //Decorations
    public PImage charImage;

    //Constructor
    public Character(PApplet sketch, int mass, int width, int height, PVector startPos) {
        //Sketch always needs to be first
		this.sketch = sketch;

        //Physics parameters
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.startPosition.set(startPos);
        this.position.set(startPos);
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.width, this.height);

        //Decoration
        this.charImage = sketch.requestImage("./res/Character.png");
    }

    //Updates all the characters parameters
    public void Update() {
		if (this.sketch.keyPressed) {
			if (Globals.isLeft)
				this.AddVelocity(new PVector((float) -0.5, 0));
			if (Globals.isRight)
				this.AddVelocity(new PVector((float) 0.5, 0));
			if (Globals.isUp) {
				if (this.onGround) {
					this.AddVelocity(new PVector(0, -15));
				} else if (this.inWater) this.AddVelocity(new PVector(0, -1.5f));
			}
			if (Globals.isDown && this.inWater) this.AddVelocity(new PVector(0, 0.5f));
		}

        //Updates position of the colissiobox 
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
		
        //Updates character velocity and position
        this.acceleration.y = gravity;
        this.velocity.x = (float) (this.velocity.x * 0.95);
        this.velocity.add(acceleration);
        this.velocity.x = PApplet.constrain(this.velocity.x, -30, 30);
        this.velocity.y = PApplet.constrain(this.velocity.y, -30, 30);

        this.position.add(this.velocity);

        this.acceleration.mult(0);

        //Checks collision with the levels edges
        if (this.position.x > (this.sketch.width - this.width) || this.position.x < 0) {
            this.position.x = PApplet.constrain(this.position.x, 0, (this.sketch.width - this.width));
            this.velocity.x = 0;
        }
        if (this.position.y > (this.sketch.height - this.height) || this.position.y < 0) {
            this.position.y = PApplet.constrain(this.position.y, 0, (this.sketch.height - this.height));
            this.velocity.y = -(this.velocity.y * mass/20);
        } 

        //Sets wasGround to false so that it is possible to check if the character is on the ground
		boolean wasGround = false;

        //Checks collision with various objects
		for (Object object : Globals.objects) {
            //Figures out which type of object the character is currently touching
			if (object == this) continue;
			if (object instanceof Platform) {
                //Controls character collision with platforms
				Platform platform = (Platform) object;
				if (platform.collisionBox.IsColliding(this.collisionBox)) {
                    //Adds a buffer around platforms so that the character does not trigger the collision on the wrong axis
                    float bufferX = 10;//PApplet.constrain(platform.width / 8, 0, 50);
                    float bufferY = 10;//PApplet.constrain(platform.height / 8, 0, 50);
                    //Checks if the character is colliding with a platform on the y-axis, and whether or not it is above or below the platform
                    if (this.position.y + this.height >= platform.position.y && this.position.y < platform.position.y && this.position.x + this.width -bufferX >= platform.position.x && platform.position.x + platform.width >= this.position.x + bufferX) {
                        //Makes the character stay above the platform 
						this.position.y = platform.position.y - this.height;
						this.velocity.y = -(this.velocity.y * mass/20);
						this.onGround = true;
						wasGround = true;
					} else if (platform.position.y + platform.height >= this.position.y && this.position.x + this.width - bufferX >= platform.position.x && platform.position.x + platform.width >= this.position.x + bufferX) {
                        //Makes the character stay below the platform
						this.position.y = platform.position.y + platform.height;
						this.velocity.y = 0;
						this.onGround = false;
						wasGround = false;
					} 
                    
                    //Checks if the character is colliding with a platform on the x-axis, and whether or not it is to the left or right of the platform
					if (this.position.x + this.width >= platform.position.x && platform.position.x + platform.width >= this.position.x && this.position.y + this.height - bufferY >= platform.position.y && platform.position.y + platform.height >= this.position.y + bufferY) {
                        if (this.position.x > platform.position.x){
                            //Makes the character stay on the right 
                            this.position.x = platform.position.x + platform.width;
                        } else {
                            //Makes the character stay on the left
                            this.position.x = platform.position.x - this.width;
                        }
						this.velocity.x = 0;
					}
                    
				}
            //Checks if the character is in water and applies waterphysics
			} else if (object instanceof Water) {
                Water water = (Water) object;
                if (water.collisionBox.IsColliding(this.collisionBox)) {
                    this.velocity.x *= 0.95;
                    this.velocity.y *= 0.5;
                    this.inWater = true;
                } else {
                    this.inWater = false;
                }
            } else if (object instanceof Coin) {
                Coin coin = (Coin) object;
                coin.Collect(this.collisionBox);
            }
		}

        //Checks if the character is on the ground
        if (this.position.y == this.sketch.height - this.height) {
            this.onGround = true;
        } else if (!wasGround) {
            this.onGround = false;
        }
	}

    //Displays the character
	public void Draw() {
		if(this.charImage.width > 0) {
			this.sketch.image(this.charImage, this.position.x, this.position.y);
		} else {
			this.sketch.square(this.GetPosition().x, this.GetPosition().y, 100);
		}
		// this.sketch.fill(255,255,255);
		// this.sketch.circle(this.position.x, this.position.y, 5);
	}

    //get, set and add
	public void SetVelocity(PVector velocity) {
		this.velocity.set(velocity);
	}

	public void AddVelocity(PVector velocity) {
		this.velocity.add(velocity);
	}

    public PVector GetPosition() {
        return this.position;
    }

    //Updates the characters position in editormode
    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.startPosition.set(position);
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
    }

    //Resets the charaters position when editor mode i activated
    public void Reset() {
        this.position.set(this.startPosition);
        this.velocity.set(0, 0);
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
    }

    //Checks if the character is being dragged around by the mouse in editormode
	public boolean MouseDragged() {
        //Saves the cursors original position 
        if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}

        //Checks if the cursor is hovering over the character and changes the startposition
		if (this.collisionBox.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}
}