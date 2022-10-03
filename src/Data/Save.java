package Data;

import java.io.*;
import java.sql.SQLException;

import javax.swing.*;

import Objects.Character;
import Objects.Coin;
import Objects.Goal;
import Objects.Platform;
import Objects.Spike;
import Objects.Water;
import Util.Encryption;
import Util.Globals;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Save {
	final JTextField nameInput = new JTextField(20);

	public Save(PApplet sketch) {
		JFrame frame = new JFrame("Save");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setBounds(10, 10, 50, 20);
		frame.add(nameLabel);

		nameInput.setBounds(60, 10, 200, 20);
		frame.add(nameInput);

		JButton saveButton = new JButton("Save");
		saveButton.setBounds(10, 40, 250, 20);
		
		saveButton.addActionListener(e -> {
			String TextInputName = nameInput.getText();
			if (TextInputName.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a name for the level.");
			} else {
				JSONObject json = new JSONObject();
				json.setInt("bgcolor", Globals.BG_COLOR);
				json.setString("nextlevel", Globals.NEXT_LEVEL_NAME);
				json.setBoolean("playonly", Globals.TEMP_PLAY_ONLY);
				json.setJSONArray("objects", new JSONArray());
				for (int i = 0; i < Globals.objects.length; i++) {
					if (Globals.objects[i] != null) {
						String name = Globals.objects[i].getClass().getSimpleName();
						JSONObject object = new JSONObject();
						switch (name) {
							case "Character":
								Character c = (Character)Globals.objects[i];
								object.setString("name", c.getClass().getSimpleName());
								object.setFloat("width", c.width);
								object.setFloat("height", c.height);
								object.setFloat("gravity", c.gravity);
								object.setFloat("speed", c.speed);
								object.setFloat("jumppower", c.jumpPower);
								object.setInt("x", (int) c.position.x);
								object.setInt("y", (int) c.position.y);
								json.getJSONArray("objects").append(object);
								break;
							case "Platform":
								Platform p = (Platform)Globals.objects[i];
								object.setString("name", p.getClass().getSimpleName());
								object.setFloat("width", p.width);
								object.setFloat("height", p.height);
								object.setInt("x", (int) p.position.x);
								object.setInt("y", (int) p.position.y);
								object.setInt("color", p.getColor());
								json.getJSONArray("objects").append(object);
								break;
							case "Water": 
								Water w = (Water)Globals.objects[i];
								object.setString("name", w.getClass().getSimpleName());
								object.setFloat("width", w.width);
								object.setFloat("height", w.height);
								object.setInt("x", (int) w.position.x);
								object.setInt("y", (int) w.position.y);
								json.getJSONArray("objects").append(object);
								break;
							case "Coin": 
								Coin c2 = (Coin)Globals.objects[i];
								object.setString("name", c2.getClass().getSimpleName());
								object.setInt("x", (int) c2.position.x);
								object.setInt("y", (int) c2.position.y);
								object.setInt("reward", c2.coinReward);
								json.getJSONArray("objects").append(object);
								break;
							case "Goal":
								Goal g = (Goal)Globals.objects[i];
								object.setString("name", g.getClass().getSimpleName());
								object.setFloat("width", g.width);
								object.setFloat("height", g.height);
								object.setInt("x", (int) g.position.x);
								object.setInt("y", (int) g.position.y);
								json.getJSONArray("objects").append(object);
								break;
							case "Spike":
								Spike s = (Spike)Globals.objects[i];
								object.setString("name", s.getClass().getSimpleName());
								object.setFloat("size", s.size);
								object.setInt("x", (int) s.position.x);
								object.setInt("y", (int) s.position.y);
								json.getJSONArray("objects").append(object);
								break;
							default:
								break;
						}
					}
				}
				String source = json.toString().replace(" ", "").replace("\n", "");
				try {
					Globals.conn.createStatement().executeUpdate("INSERT INTO levels (name, data) VALUES ('" + Encryption.Encrypt(TextInputName) + "', '" + source + "')");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				frame.dispose();
			}
		});
		frame.add(saveButton);
	}
}
