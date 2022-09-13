package Util;

import Objects.Character;
import Objects.Platform;
import Objects.Coin;
import Objects.Water;
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
					} else if (object instanceof Coin) {
						Coin coin = (Coin) object;
						if (coin.MouseDragged()) {
							this.selected = object;
							break;
						}
					} else if (object instanceof Water) {
						Water water = (Water) object;
						if (water.MouseDragged()) {
							this.selected = object;
							break;
						}
					}
				}
			} else if (selected != null && !Globals.IS_SHIFT_PRESSED) {
				selected.MouseDragged();
			}

			if (this.sketch.mouseButton == PConstants.LEFT && Globals.IS_SHIFT_PRESSED && this.selected == null) {
				for (Object object : Globals.objects) {
					if (object instanceof Platform) {
						Platform platform = (Platform) object;
						if (platform.MouseExtending()) {
							this.selected = object;
							break;
						}
					} else if (object instanceof Water) {
						Water water = (Water) object;
						if (water.MouseExtending()) {
							this.selected = object;
							break;
						}
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
						platform.color = color;
						Globals.IS_SHIFT_PRESSED = false;
						break;
					} else if (platform.delete.IsInOver(new PVector(this.sketch.mouseX, this.sketch.mouseY))) {
						Globals.objects[i] = null;
						break;
					}
				} else if (object instanceof Water) {
					Water water = (Water) object;
					if (water.delete.IsInOver(new PVector(this.sketch.mouseX, this.sketch.mouseY))) {
						Globals.objects[i] = null;
						break;
					}
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
