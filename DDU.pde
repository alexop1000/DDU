import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;

Character testCharacter;
JSONObject editorData = new JSONObject();
Button start = new Button(500, 9, 34, 45, "ting");

void setup() {
	fullScreen();
	testCharacter = new Character(1, 100, 100, new PVector(width/2, height/2));
	editorData.setString("background", "080808");
}

boolean IS_EDITOR = true;

boolean isLeft, isRight, isUp, isDown;

void draw() {
	clear();
	fill(255, 255, 255);
	background(unhex(editorData.getString("background")));

	// UPDATE CORE LOOPS
	if (!IS_EDITOR) {
		testCharacter.Update();
	} else {
		testCharacter.ResetPosition();
	}
	//
	
	if(testCharacter.charImage.width > 0) {
		image(testCharacter.charImage, testCharacter.position.x, testCharacter.position.y);
	} else {
		square(testCharacter.GetPosition().x, testCharacter.GetPosition().y, 100);
	}

	if (keyPressed) {
		if (isLeft) testCharacter.AddVelocity(new PVector(-0.5, 0));
		if (isRight) testCharacter.AddVelocity(new PVector(0.5, 0));
		if (isUp && testCharacter.onGround) testCharacter.AddVelocity(new PVector(0, -5));
	}
	
	start.display();
}

void mouseClicked() {
	IS_EDITOR = !start.clicked(mouseX, mouseY);
}

void mouseDragged() {
	if (IS_EDITOR) {
		if (mouseButton == LEFT) {
			if (testCharacter.collisionBox.IsInOver(new PVector(mouseX, mouseY)) || testCharacter.isDragging) {
				testCharacter.isDragging = true;
				testCharacter.UpdateStartPosition(new PVector(mouseX - (testCharacter.charWidth / 2), mouseY - (testCharacter.charHeight / 2)));
			}
		}
	}	
}

void mouseReleased() {
	testCharacter.isDragging = false;
}

color PickColor(int c) {
	JFrame jf = new JFrame("Dummy");
	jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	jf.setLocationRelativeTo(null);
	jf.setAlwaysOnTop(true);
	jf.setVisible(true);
	Color javaColor = new Color(c, true);
	javaColor = JColorChooser.showDialog(jf, "Java Color Chooser", javaColor); 
	jf.setVisible(false);
	if (javaColor != null ) {  
		c = (int) alpha(c) << 24 | (javaColor.getRed() << 16 ) | (javaColor.getGreen() << 8 ) | (javaColor.getBlue() << 0 );
		return c;
	}
	return color(0);
}

void keyPressed() {
	setMove(key, true);
	if (key == 'c') {
		editorData.setString("background", hex(PickColor(0)));
		background(unhex(editorData.getString("background")));
	}
}

void keyReleased() {
	setMove(key, false);
}

boolean setMove(int k, boolean b) {
	switch (k) {
		case 'w' :
			return isUp = b;
		case 's' :
			return isDown = b;
		case 'a' :
			return isLeft = b;
		case 'd' :
			return isRight = b;
		default :
			return b;
	}
}