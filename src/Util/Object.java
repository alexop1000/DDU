package Util;

import processing.core.*;

public class Object {
	public PVector position = new PVector();
	public CollisionBox collisionBox;
	public boolean isDragging = false;
	public boolean isExtending = false;
	public int height;
	public int width;

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
			Globals.objects[i] = null;
		}
	}
}
