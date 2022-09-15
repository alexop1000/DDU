package Objects;

import processing.core.*;

import java.util.*;

import Util.CollisionBox;
import Util.Globals;
import Util.Object;
import Util.ObjectControls;

public class Spike extends Object {
    public PApplet sketch;
    public float size;
    public PVector position;
    public ArrayList<CollisionBox> CollisionBoxes = new ArrayList<CollisionBox>();
	public CollisionBox drag;

    public Spike(PApplet sketch, PVector position) {
        this.sketch = sketch;
        this.size = 50;
        this.position = position;

        float halfHeight = this.size/2;
        if (this.size < 50) {
            for (int i = 0, j = 5; i < 5; i++, j--) {
                if (i == 4) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.size/10 - this.size/5 * i + halfHeight, Math.abs(Inclination()/2 * this.size/5/2 * j), 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.size/10 - this.size/5 * i + halfHeight, Math.abs(Inclination()/2 * this.size/5 * j), this.size/5));
            }
        } else {
            for (int i = 0, j = (int)this.size/10; i < (int)this.size/10; i++, j--) {
                if (i >= (int)this.size/10 - 1) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i + (this.size % 10)/2 + halfHeight, Math.abs(Inclination()/2 * this.size/i * j), 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i + halfHeight, Math.abs(Inclination()/2 * 10 * j - 1), 10));
            }
        }

		this.drag = new CollisionBox(this.position.x, this.position.y, this.size, this.size);
		this.controls = new ObjectControls(this.sketch, (int) this.size, (int) this.size, this.position, false, true, true);
    }

	public void UpdateStartPosition(float width, float height, PVector position) {
		this.size = width;
		this.position.set(position);

		this.drag.Update(this.position.x, this.position.y, this.size, this.size);

		CollisionBoxes.clear();

        float halfHeight = this.size/2;
        if (this.size < 50) {
            for (int i = 0, j = 5; i < 5; i++, j--) {
                if (i == 4) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.size/10 - this.size/5 * i + halfHeight, Math.abs(Inclination()/2 * this.size/5/2 * j), 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - this.size/10 - this.size/5 * i + halfHeight, Math.abs(Inclination()/2 * this.size/5 * j), this.size/5));
            }
        } else {
            for (int i = 0, j = (int)this.size/10; i < (int)this.size/10; i++, j--) {
                if (i >= (int)this.size/10 - 1) {
                    CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i + (this.size % 10)/2 + halfHeight, Math.abs(Inclination()/2 * this.size/i * j), 10));
                    break;
                }
                CollisionBoxes.add(new CollisionBox(this.position.x, this.position.y - 10 * i + halfHeight, Math.abs(Inclination()/2 * 10 * j - 1), 10));
            }
        }

		this.controls.Update((int)this.size, (int)this.size, this.position);
	}

	@Override
    public void Draw() {
        //triangle
        this.sketch.fill(255, 0, 0);
        this.sketch.triangle(this.position.x - this.size/2, this.position.y + this.size/2, this.position.x, this.position.y - this.size + this.size/2, this.position.x + this.size/2, this.position.y + this.size/2);

        //debug hitbox drawing
        // this.sketch.fill(0, 0, 255);
        // if (this.size < 50) {
        //     for (int i = 0, j = 5; i < 5; i++, j--) {
        //         if (i == 4) {
        //             this.sketch.rect(this.position.x, this.position.y - this.size/10 - this.size/5 * i + this.size/2, Math.abs(Inclination()/2 * this.size/5/2 * j), 10);
        //             break;
        //         }
        //         this.sketch.rect(this.position.x, this.position.y - this.size/10 - this.size/5 * i + this.size/2, Math.abs(Inclination()/2 * this.size/5 * j), this.size/5);
        //     }
        // } else {
        //     for (int i = 0, j = (int)this.size/10; i < (int)this.size/10; i++, aj--) {
        //         if (i >= (int)this.size/10 - 1) {
        //             this.sketch.rect(this.position.x, this.position.y - 10 * i + (this.size % 10)/2 + this.size/2, Math.abs(Inclination()/2 * this.size/i * j), 10);
        //             break;
        //         }
        //         this.sketch.rect(this.position.x, this.position.y - 10 * i + this.size/2, Math.abs(Inclination()/2 * 10 * j - 1), 10);
        //     }
        // }

		
        this.controls.Draw();
    }

    private float Inclination() {
       return ((this.position.y - this.size)-this.position.y)/((this.position.x)-(this.position.x - this.size/2));
    }
	
	@Override
    public boolean MouseDragged() {
		if (!super.isDragging){
			Globals.startMouseX = this.position.x - this.sketch.mouseX;
			Globals.startMouseY = this.position.y - this.sketch.mouseY;
		}
		if (this.drag.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isDragging) {
			super.isDragging = true;
			this.UpdateStartPosition(this.size, this.size, new PVector(this.sketch.mouseX + Globals.startMouseX, this.sketch.mouseY + Globals.startMouseY));
			return true;
		}
		return false;
	}
	
    @Override
	public boolean MouseExtending() { 
		if (this.controls.scale.IsInOver(new PVector(this.sketch.mouseX, sketch.mouseY)) || super.isExtending) {
			super.isExtending = true;
            int constrainedSize = (int)PApplet.constrain(this.sketch.mouseX - this.position.x + this.size / 2, 40, 500);
			this.UpdateStartPosition(constrainedSize, constrainedSize, this.position);
			return true;
		}
		return false;
    }

	@Override
	public boolean GetCollision(PVector position) {
		return this.drag.IsInOver(position);
	}
	@Override
	public float getHeight() {
		return size;
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
		return size;
	}
}
