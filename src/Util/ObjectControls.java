package Util;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ObjectControls {
	public CollisionBox colorWheel;
	public CollisionBox delete;
	public CollisionBox scale;
	public PImage scaleImage;
	public PImage colorImage;
	public PImage trashImage;

	public PVector position;
	public int width;
	public int height;

	public PApplet sketch;

	public ObjectControls(PApplet sketch, int width, int height, PVector position, boolean doColor, boolean doDelete, boolean doScale) {
		this.sketch = sketch;
		this.width = width;
		this.height = height;
		this.position = position;
		
		float halfWidth = width / 2;
		float halfHeight = height / 2;

		int nextHeight = 20;
		if (doScale) {
			this.scale = new CollisionBox(position.x + halfWidth + 20, position.y + halfHeight - nextHeight, 30, 30);
			this.scaleImage = sketch.requestImage(ObjectControls.class.getClassLoader().getResource("ScalingIcon.png").toString());
			nextHeight += 35;
		}
		if (doColor) {
			this.colorWheel = new CollisionBox(position.x + halfWidth + 20, position.y + halfHeight - nextHeight, 30, 30);
			this.colorImage = sketch.requestImage(ObjectControls.class.getClassLoader().getResource("ColorWheel.png").toString());
			nextHeight += 35;
		}
		if (doDelete) {
			this.delete = new CollisionBox(position.x + halfWidth + 20, position.y + halfHeight - nextHeight, 30, 30);
			this.trashImage = sketch.requestImage(ObjectControls.class.getClassLoader().getResource("TrashCan.png").toString());
		}
	}

	public void Update(int width, int height, PVector position) {
		this.width = width;
		this.height = height;
		this.position = position;
		
		int nextHeight = 20;
		if (this.scale != null) {
			this.scale.Update(this.position.x + 20 + this.width / 2, this.position.y + this.height / 2 - nextHeight, 30, 30);
			nextHeight += 35;
		}
		if (this.colorWheel != null) {
			this.colorWheel.Update(this.position.x + 20 + this.width / 2, this.position.y + this.height / 2 - nextHeight, 30, 30);
			nextHeight += 35;
		}
		if (this.delete != null) {
			this.delete.Update(this.position.x + 20 + this.width / 2, this.position.y + this.height / 2 - nextHeight, 30, 30);
		}
	}

	public void Draw() {
		if (Globals.IS_EDITOR && Globals.IS_SHIFT_PRESSED) {
			float halfWidth = this.width / 2;
			float halfHeight = this.height / 2;
			this.sketch.fill(255, 255, 255);
			int nextHeight = 20;
			if (this.scale != null) {
				this.sketch.rect(this.position.x + halfWidth + 20, this.position.y + halfHeight - nextHeight, 30, 30);
				this.sketch.image(this.scaleImage, this.position.x + halfWidth + 20, this.position.y + halfHeight - nextHeight);
				nextHeight += 35;
			}
			if (this.colorWheel != null) {
				this.sketch.rect(this.position.x + halfWidth + 20, this.position.y + halfHeight - nextHeight, 30, 30);
				this.sketch.image(this.colorImage, this.position.x + halfWidth + 20, this.position.y + halfHeight - nextHeight);
				nextHeight += 35;
			}
			if (this.delete != null) {
				this.sketch.rect(this.position.x + halfWidth + 20, this.position.y + halfHeight - nextHeight, 30, 30);
				this.sketch.image(this.trashImage, this.position.x + halfWidth + 20, this.position.y + halfHeight - nextHeight);
				nextHeight += 35;
			}
		}
	}
}
