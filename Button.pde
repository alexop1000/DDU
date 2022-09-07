class Button{
  float x,y;
  float w,h;
  boolean selected;
  color selectedColor, defaultColor, currentColor;
  String label; 

  Button(float x, float y, float w, float h, String label ){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.label = label;
    selected = false;
    selectedColor = color(280, 100, 100);
    defaultColor = color(280, 70, 70);
    currentColor = defaultColor; 
  }

  void display(){
    fill(currentColor);
    rect(x, y, w, h);
    fill(0);
    textAlign(CENTER);
    text(label, x + w/2, y + (h/2));
  }

  boolean clicked( int mx, int my){
	if(mx > x && mx < x + w  && my > y && my < y+h){
		selected = !selected;
		if (selected){
			currentColor = selectedColor;
		} else {
			currentColor = defaultColor;
		}
    }
    return selected;
  }

}