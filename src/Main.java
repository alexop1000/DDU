import javax.swing.JFrame;

import UI.Button;

import Util.Object;
import Util.Dragger;
import Util.Globals;

import java.awt.Color;
import javax.swing.JColorChooser;

import processing.core.*;
import processing.data.*;

import Objects.Character;
import Objects.Coin;
import Objects.Platform;


public class Main extends PApplet {
	JSONObject editorData = new JSONObject();
	Character testCharacter;
	boolean isLeft, isRight, isUp, isDown;
	Dragger dragger;
	Button startButton;
	Coin coin;
	Platform testPlatform;
	int score = 0;

	public void settings() {
		fullScreen(2);
		editorData.setString("background", "080808");

		delay(1000);

		dragger = new Dragger(this);

		// ! Initialize objects
		coin = new Coin(this, false, new PVector(100, 100), (int a) -> (score + 1));
		testCharacter = new Character(this, 1, 100, 100, new PVector(displayWidth / 2, displayHeight / 2));
		testPlatform = new Platform(this, displayWidth / 2 - 100, 100, new PVector(displayWidth / 2, (displayHeight / 2) + 110));
		startButton = new Button(this, 500, 9, 34, 45, new String[] { "Start", "Stop" }, new int[] { 255, 0, 0 },
				new int[] { 0, 255, 0 });
		Globals.objects = new Object[] { testPlatform, coin, testCharacter };
	}

	public void draw() {
		clear();
		fill(255, 255, 255);
		background(unhex(editorData.getString("background")));

		// ! UPDATE CORE LOOPS
		if (!Globals.IS_EDITOR) {
			testCharacter.Update();
		} else {
			testCharacter.Reset();
		}
		//
		testPlatform.Draw();
		testCharacter.Draw();

		if (keyPressed) {
			if (isLeft)
				testCharacter.AddVelocity(new PVector((float) -0.5, 0));
			if (isRight)
				testCharacter.AddVelocity(new PVector((float) 0.5, 0));
			if (isUp && testCharacter.onGround)
				testCharacter.AddVelocity(new PVector(0, -15));
		}

		startButton.display();
	}
	
	public void mouseClicked() {
		Globals.IS_EDITOR = !startButton.clicked(mouseX, mouseY);
	}

	public void mouseDragged() {
		dragger.mouseDragged();
	// 	if (Globals.IS_EDITOR) {
	// 		if (mouseButton == LEFT) {
	// 			if (testCharacter.collisionBox.IsInOver(new PVector(mouseX, mouseY)) || testCharacter.isDragging) {
	// 				testCharacter.isDragging = true;
	// 				testCharacter.UpdateStartPosition(new PVector(mouseX - (testCharacter.charWidth / 2),
	// 						mouseY - (testCharacter.charHeight / 2)));
	// 			}
	// 		}
	// 	}
	}

	public void mouseReleased() {
		dragger.mouseReleased();
	// 	testCharacter.isDragging = false;
	}

	private int PickColor(int c) {
		JFrame jf = new JFrame("Dummy");
		jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		jf.setLocationRelativeTo(null);
		jf.setAlwaysOnTop(true);
		jf.setVisible(true);
		Color javaColor = new Color(c, true);
		javaColor = JColorChooser.showDialog(jf, "Java Color Chooser", javaColor);
		jf.setVisible(false);
		if (javaColor != null) {
			c = (int) alpha(c) << 24 | (javaColor.getRed() << 16) | (javaColor.getGreen() << 8)
					| (javaColor.getBlue() << 0);
			return c;
		}
		return color(0);
	}

	public void keyPressed() {
		setMove(key, true);
		if (key == 'c') {
			editorData.setString("background", hex(PickColor(0)));
			background(unhex(editorData.getString("background")));
		}
	}

	public void keyReleased() {
		setMove(key, false);
	}

	private boolean setMove(int k, boolean b) {
		switch (k) {
			case 'w':
				return isUp = b;
			case 's':
				return isDown = b;
			case 'a':
				return isLeft = b;
			case 'd':
				return isRight = b;
			default:
				return b;
		}
	}

	public static void main(String[] args) {
		PApplet.runSketch(new String[] { "Main" }, new Main());
	}	
}