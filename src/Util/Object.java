package Util;

import processing.core.*;

public class Object {
	public PVector position = new PVector();
	public CollisionBox collisionBox;
	public boolean isDragging = false;
	public int height;
	public int width;

	public void UpdateStartPosition(PVector v) {

	}

	public boolean MouseDragged() {
		return isDragging;
	}
}
