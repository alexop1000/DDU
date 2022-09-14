package Objects;

import processing.core.*;

import java.util.*;

import Util.CollisionBox;
import Util.Globals;
import Util.Object;

public class Spike extends Object {
    public PApplet sketch;
	public float height;
    public float width;
    public PVector position;
    public ArrayList<CollisionBox> CollisionBoxes = new ArrayList<CollisionBox>();
	public CollisionBox delete;
	public CollisionBox drag;

	public PImage trashImage;

    public Spike(PApplet sketch, PVector position) {
        this.sketch = sketch;
        this.height = 50;
        this.width = 50;
        this.position = position;
        if (this.height < 50) {
            for (int i = 0, j = 5; i < 5; i++, j--) {
                if (i == 4) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.height/10 - this.height/5 * i, Inclination()/2 * this.height/5/2 * j, 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.height/10 - this.height/5 * i, Inclination()/2 * this.height/5 * j, this.height/5));
            }
        } else {
            for (int i = 0, j = (int)this.height/10; i < (int)this.height/10; i++, j--) {
                if (i >= (int)this.height/10 - 1) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i + (this.height % 10)/2, Inclination()/2 * this.width/i * j, 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i, Inclination()/2 * 10 * j - 1, 10));
            }
        }     
		
		
		float halfWidth = this.width / 2;
		float halfHeight = this.height / 2;

		this.delete = new CollisionBox(this.position.x + halfWidth + 20, this.position.y + halfHeight - 20, 30, 30);
		this.drag = new CollisionBox(this.position.x, this.position.y, this.width, this.height);

		this.trashImage = sketch.requestImage("./res/TrashCan.png");
    }

	public void UpdateStartPosition(float width, float height, PVector position) {
		this.width = width;
		this.height = height;
		this.position.set(position);
		
		float halfWidth = this.width / 2;
		float halfHeight = this.height / 2;
		this.delete.Update(this.position.x + 20 + halfWidth, this.position.y + halfHeight - 90, 30, 30);
		this.drag.Update(this.position.x, this.position.y, this.width, this.height);

		CollisionBoxes.clear();

        if (this.height < 50) {
            for (int i = 0, j = 5; i < 5; i++, j--) {
                if (i == 4) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.height/10 - this.height/5 * i, Inclination()/2 * this.height/5/2 * j, 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.height/10 - this.height/5 * i, Inclination()/2 * this.height/5 * j, this.height/5));
            }
        } else {
            for (int i = 0, j = (int)this.height/10; i < (int)this.height/10; i++, j--) {
                if (i >= (int)this.height/10 - 1) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i + (this.height % 10)/2, Inclination()/2 * this.width/i * j, 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i, Inclination()/2 * 10 * j - 1, 10));
            }
        }
	}

	@Override
    public void Draw() {
        /*
        //debug hitbox drawing
        this.sketch.fill(0, 0, 255);
        if (this.height < 50) {
            for (int i = 0, j = 5; i < 5; i++, j--) {
                if (i == 4) {
                    this.sketch.rect(this.position.x, this.position.y - this.height/10 - this.height/5 * i, Inclination()/2 * this.height/5/2 * j, 10);
                    break;
                }
                this.sketch.rect(this.position.x, this.position.y - this.height/10 - this.height/5 * i, Inclination()/2 * this.height/5 * j, this.height/5);
            }
        } else {
            for (int i = 0, j = (int)this.height/10; i < (int)this.height/10; i++, j--) {
                if (i >= (int)this.height/10 - 1) {
                    this.sketch.rect(this.position.x, this.position.y - 10 * i + (this.height % 10)/2, Inclination()/2 * this.width/i * j, 10);
                    break;
                }
                this.sketch.rect(this.position.x, this.position.y - 10 * i, Inclination()/2 * 10 * j - 1, 10);
            }
        }
        */
        //triangle
        this.sketch.fill(255, 0, 0);
        this.sketch.triangle(this.position.x - this.width/2, this.position.y, this.position.x, this.position.y - this.height, this.position.x + this.width/2, this.position.y);

		if (Globals.IS_EDITOR && Globals.IS_SHIFT_PRESSED) {
			float halfWidth = this.width / 2;
			float halfHeight = this.height / 2;
			this.sketch.fill(255, 255, 255);

			this.sketch.rect(this.position.x + halfWidth + 20, this.position.y + halfHeight - 20, 30, 30);
			this.sketch.image(this.trashImage, this.position.x + halfWidth + 20, this.position.y + halfHeight - 20);
		}
    }

    private float Inclination() {
       return ((this.position.y - this.height)-this.position.y)/((this.position.x)-(this.position.x - this.width/2));
    }
	
	@Override
    public boolean MouseDragged() {
		if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}
		if (this.drag.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(this.width, this.height, new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}
	
    @Override
	public boolean MouseExtending() { return false; }
}
