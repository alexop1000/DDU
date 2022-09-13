package Util;

import javax.swing.JColorChooser;
import javax.swing.JFrame;

import processing.core.PApplet;

import java.awt.Color;

public class Globals {
    public static boolean IS_EDITOR = true;
	public static boolean IS_SHIFT_PRESSED = false;
	public static Object[] objects;
    public static float startMouseX;
    public static float startMouseY;
	public static int Score;
	public static boolean isLeft, isRight, isUp, isDown;

	public static int PickColor(PApplet sketch, int c) {
		JFrame jf = new JFrame("Dummy");
		jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		jf.setLocationRelativeTo(null);
		jf.setAlwaysOnTop(true);
		jf.setVisible(true);
		Color javaColor = new Color(c, true);
		javaColor = JColorChooser.showDialog(jf, "Java Color Chooser", javaColor);
		jf.setVisible(false);
		if (javaColor != null) {
			c = (int) sketch.alpha(c) << 24 | (javaColor.getRed() << 16) | (javaColor.getGreen() << 8)
					| (javaColor.getBlue() << 0);
			return sketch.color(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
		}
		return sketch.color(0);
	}

}
