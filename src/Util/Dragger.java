package Util;

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
			if (this.sketch.mouseButton == PConstants.LEFT && !Globals.IS_SHIFT_PRESSED && this.selected == null ) {
				for (Object object : Globals.objects) {
					if (object != null && object.MouseDragged()) {
						this.selected = object;
						break;
					}
				}
			} else if (selected != null && !Globals.IS_SHIFT_PRESSED) {
				selected.MouseDragged();
			}

			if (this.sketch.mouseButton == PConstants.LEFT && Globals.IS_SHIFT_PRESSED && this.selected == null) {
				for (Object object : Globals.objects) {
					if (object != null && object.MouseExtending()) {
						this.selected = object;
						break;
					}
				}
			} else if (selected != null && Globals.IS_SHIFT_PRESSED) {
				selected.MouseExtending();
			}
		}
	}

	public void mouseClicked() {
		if (this.sketch.mouseButton == PConstants.LEFT && Globals.IS_SHIFT_PRESSED) {
			for (int i = 0; i < Globals.objects.length; i++) {
				Object object = Globals.objects[i];
				if (object instanceof Platform) {
					Platform platform = (Platform) object;
					if (platform.MouseColor()){
						int color = Globals.PickColor(this.sketch, 0);
						platform.setColor(color);
						Globals.IS_SHIFT_PRESSED = false;
						break;
					}
				}
				if (object != null) {
					object.Delete(this.sketch, i);
				}
			}
		} else if (this.sketch.mouseButton == PConstants.LEFT) {
			for (int i = 0; i < Globals.objects.length; i++) {
				Object object = Globals.objects[i];
				if (object == null) continue;
				if (object.GetCollision(new PVector(this.sketch.mouseX, this.sketch.mouseY))) {
					System.out.println("Selected " + object.getClass().getSimpleName());
					Globals.selected = object;
					break;
				}
			}
		}
	}

	public void mouseReleased() {
		if (selected != null) {
			selected.isDragging = false;
			selected.isExtending = false;
			selected = null;
		}
	}
}
