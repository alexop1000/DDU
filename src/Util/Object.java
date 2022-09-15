package Util;

import processing.core.*;

public class Object {
	public PVector position = new PVector();
	public PVector getPosition() {
		return position;
	}
	public void setPosition(PVector position) {
		this.position = position;
	}

	public CollisionBox collisionBox;
	public boolean isDragging = false;
	public boolean isExtending = false;
	protected int height;

	public float getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	protected int width;

	public float getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public ObjectControls controls;

	public void Reset() {
		
	}
	public void Draw() {

	}
	public void UpdateStartPosition(PVector v) {

	}

	public boolean MouseDragged() {
		return isDragging;
	}

	public boolean MouseExtending() {
		return isExtending;
	}

	public void Delete(PApplet sketch, int i) {
		if (this.controls != null && this.controls.delete.IsInOver(new PVector(sketch.mouseX, sketch.mouseY))) {
			if (Globals.objects[i] == Globals.selected) {
				Globals.selected = null;
			}
			Globals.objects[i] = null;
		}
	}

	public boolean GetCollision(PVector position) {
		if (this.collisionBox == null) return false;
		return this.collisionBox.IsInOver(position);
	}
}
