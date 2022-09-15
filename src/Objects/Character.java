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
    public float width = 200;
    public float height = 200;
    public float gravity = 0.5f;
    public int mass = 5;
    public float speed = 0.5f;
    public float jumpPower = -18;

    public boolean onGround = false;
    public boolean inWater = false;
    public boolean isFinished = false;
    public CollisionBox collisionBox;

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

        this.controls = new ObjectControls(this.sketch, (int) this.width, (int) this.height, this.position, false, true, true);
    }

    //Updates all the characters parameters
    public void Update() {

        if (this.isFinished) return;
        for (Object object : Globals.objects) {
           if (object instanceof Goal) {
                Goal g = (Goal) object;
                this.isFinished = g.CheckFinished(this);
                if (this.isFinished) return;
           }
        }

        //Updates position of the collisionbox 
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
		
        //Updates character velocity and position
        this.acceleration.y = gravity * mass;
        this.velocity.x = (float) (this.velocity.x * 0.95);
        this.velocity.add(acceleration);
        this.velocity.x = PApplet.constrain(this.velocity.x, -30, 30);
        this.velocity.y = PApplet.constrain(this.velocity.y, -30, 30);

        //Sets wasGround to false so that it is possible to check if the character is on the ground
		boolean wasGround = false;
        boolean wasWater = false;

        //Checks collision with various objects
		for (Object object : Globals.objects) {
			if (object == this) continue;
            //Figures out which type of object the character is currently touching
            if (object instanceof Water) {
                //Checks if the character is in water and applies waterphysics
                Water water = (Water) object;
                if (water.collisionBox.IsColliding(this.collisionBox)) {
                    this.velocity.x *= 0.95;
                    this.velocity.y *= 0.5;
                    this.inWater = true;
                    wasWater = true;
                } else if (this.inWater && !wasWater) {
                    this.inWater = false;
                }
			}
            if (object instanceof Platform) {
                //Controls character collision with platforms
				Platform platform = (Platform) object;

                if (CheckCollisionX(platform)) {
                    if (this.position.x < platform.position.x) {
                        this.position.x = platform.position.x - platform.width / 2 - this.width / 2;
                    } else {
                        this.position.x = platform.position.x + platform.width / 2 + this.width / 2;
                    }
                    this.velocity.x = 0;
                }

                if (CheckCollisionY(platform)) {
                    // Player on top of platform
                    if (this.position.y < platform.position.y) {
                        this.position.y = platform.position.y - platform.height / 2 - this.height / 2;
                        this.onGround = true;
                        wasGround = true;
                    } else {
                        this.position.y = platform.position.y + platform.height / 2 + this.height / 2;
                        this.onGround = false;
                        wasGround = false;
                    }
                    this.velocity.y = 0;
                }


            } 
            if (object instanceof Coin) {
                Coin coin = (Coin) object;
                coin.Collect(this.collisionBox);
            }
            if (object instanceof Spike) {
                Spike spike = (Spike) object;
                if (spike.drag.IsColliding(this.collisionBox)) {
                    for (int i = 0; i < spike.CollisionBoxes.size(); i++) {
                        if (spike.CollisionBoxes.get(i).IsColliding(this.collisionBox)) {
                            this.Reset();
                            for (Object coin : Globals.objects) {
                                if (coin instanceof Coin) {
                                    ((Coin) coin).Reset();
                                }
                            }
                            return;
                        }
                    }
                }
            }
		}


        //Checks collision with the levels edges
        if (this.position.x > (this.sketch.width - this.width / 2) || this.position.x < this.width / 2) {
            this.position.x = PApplet.constrain(this.position.x, this.width / 2, (this.sketch.width - this.width / 2));
            this.velocity.x = 0;
        }
        if (this.position.y > (this.sketch.height - this.height / 2) || this.position.y - this.height / 2 < 0) {
            this.position.y = PApplet.constrain(this.position.y, this.height/2, (this.sketch.height - this.height / 2));
            this.velocity.y = 0;
        } 

        //Checks if the character is on the ground
        if (this.position.y == this.sketch.height - this.height / 2) {
            this.onGround = true;
        } else if (!wasGround) {
            this.onGround = false;
        }

		if (this.sketch.keyPressed) {
			if (Globals.isLeft)
				this.AddVelocity(new PVector((float) -speed, 0));
			if (Globals.isRight)
				this.AddVelocity(new PVector((float) speed, 0));
			if (Globals.isUp) {
				if (this.onGround) {
					this.AddVelocity(new PVector(0, jumpPower));
				} else if (this.inWater) this.AddVelocity(new PVector(0, -1.5f));
			}
			if (Globals.isDown && this.inWater) this.AddVelocity(new PVector(0, speed));
		}

        this.position.add(this.velocity);

        this.acceleration.mult(0);
	}

    private boolean CheckCollisionX(Platform platform) {
        return (this.position.x + this.velocity.x + this.width / 2 > platform.position.x - platform.width / 2 
                        && this.position.x + this.velocity.x - this.width / 2 < platform.position.x + platform.width / 2)
                        && (this.position.y + this.height / 2 - 10 > platform.position.y - platform.height / 2
                                && this.position.y - this.height / 2 + 10 < platform.position.y + platform.height / 2);
    }

    private boolean CheckCollisionY(Platform platform) {
        return (this.position.x + this.width / 2 > platform.position.x - platform.width / 2
                        && this.position.x - this.width / 2 < platform.position.x + platform.width / 2)
                        && (this.position.y + this.velocity.y + this.height / 2 > platform.position.y - platform.height / 2
                                && this.position.y + this.velocity.y - this.height / 2 < platform.position.y + platform.height / 2);
    }

    //Displays the character
	@Override
	public void Draw() {
        this.sketch.fill(35, 177, 77);
		this.sketch.rect(this.GetPosition().x, this.GetPosition().y, this.width, this.height);
        this.sketch.fill(0,0,0);
        this.sketch.strokeWeight((float)Math.sqrt(this.width*this.height) / 500);
        this.sketch.curve(this.position.x, (float)this.position.y - this.height / 2.5f, 
        this.position.x + this.width / 6 , this.position.y - this.height / 4, 
        this.position.x + this.width / 2 , this.position.y - this.height / 4, 
        this.position.x + this.width, (float)this.position.y - this.height / 2.5f);

        this.sketch.strokeWeight(1);
        this.sketch.circle(this.position.x + this.width/2 * 7/10, this.position.y - this.height/2 * 4/5, (float)Math.sqrt(this.width*this.height)/13);
        this.sketch.circle(this.position.x + this.width/2 * 4/10, this.position.y - this.height/2 * 4/5, (float)Math.sqrt(this.width*this.height)/13);

        this.controls.Draw();
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
    public void UpdateStartPosition(float x, float y, PVector position) {
        this.width = x;
        this.height = y;

        this.position.set(position);
        this.startPosition.set(position);

		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
        
        this.controls.Update((int) this.width, (int) this.height, this.position);
    }

    //Resets the charaters position when editor mode i activated
	@Override
    public void Reset() {
        this.position.set(this.startPosition);
        this.velocity.set(0, 0);
		this.collisionBox.Update(this.position.x, this.position.y, this.width, this.height);
        this.isFinished = false;
    }

    //Checks if the character is being dragged around by the mouse in editormode
    @Override
	public boolean MouseDragged() {
        //Saves the cursors original position 
        if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}

        //Checks if the cursor is hovering over the character and changes the startposition
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
	public PVector getPosition() {
		return position;
	}

	@Override
	public float getWidth() {
		return width;
	}
}