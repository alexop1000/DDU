package Util;

import Objects.Character;
import Objects.Platform;
import processing.core.*;

public class Dragger {
	Object selected;
	PApplet sketch;

	public Dragger(PApplet sketch) {
		this.sketch = sketch;
	}

	public void mouseDragged() {
		if (Globals.IS_EDITOR) {
			if (this.sketch.mouseButton == PConstants.LEFT && this.selected == null) {
				for (Object object : Globals.objects) {
					if (object instanceof Platform) {
						Platform platform = (Platform) object;
						if (platform.MouseDragged()) {
							this.selected = object;
							break;
						}
					} else if (object instanceof Character) {
						Character character = (Character) object;
						if (character.MouseDragged()) {
							this.selected = object;
							break;
						}
					}
				}
			} else if (selected != null) {
				selected.MouseDragged();
			}
		}
	}

	public void mouseReleased() {
		if (selected != null) {
			selected.isDragging = false;
			selected = null;
		}
	}
}
